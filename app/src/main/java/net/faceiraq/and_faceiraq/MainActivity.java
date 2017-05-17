package net.faceiraq.and_faceiraq;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import net.faceiraq.and_faceiraq.controller.CardsActivity;
import net.faceiraq.and_faceiraq.dialog.MainDialogFragment;
import net.faceiraq.and_faceiraq.model.PageDetails;
import net.faceiraq.and_faceiraq.model.SharedPreferencesHelper;
import net.faceiraq.and_faceiraq.model.database.bookmarks.BookmarksDAOImplementation;
import net.faceiraq.and_faceiraq.model.database.history.HistoryDAOImplementation;
import net.faceiraq.and_faceiraq.model.database.opened_pages.OpenedPageModel;
import net.faceiraq.and_faceiraq.model.database.opened_pages.OpenedPagesDAO;
import net.faceiraq.and_faceiraq.model.database.previous_pages.PreviousPagesDAO;
import net.faceiraq.and_faceiraq.model.utils.PageUrlValidator;
import net.faceiraq.and_faceiraq.model.utils.ThemeChangeUtil;
import net.faceiraq.and_faceiraq.push_notifications.RegistrationIntentService;
import net.faceiraq.and_faceiraq.view.NavigationBarFragment;
import net.faceiraq.and_faceiraq.view.WebViewFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;

import static net.faceiraq.and_faceiraq.controller.BookmarksActivity.BOOKMARKS_REQUEST_CODE;
import static net.faceiraq.and_faceiraq.controller.CardsActivity.CARDS_REQUEST_CODE;
import static net.faceiraq.and_faceiraq.controller.HistoryActivity.HISTORY_REQUEST_CODE;
import static net.faceiraq.and_faceiraq.theme.colour.ThemeColourActivity.THEME_COLOUR_REQUEST_CODE;

public class MainActivity extends FragmentActivity
        implements NavigationBarFragment.OnNavigationBarActionListener,
                    WebViewFragment.OnWebViewActionListener,
                    MainDialogFragment.OnMainDialogActionsListener {

    private static final String TAG = "MainActivity";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private int themeId;
    private HistoryDAOImplementation historyDAO;
    private BookmarksDAOImplementation bookmarksDAO;
    private OpenedPagesDAO openedPagesDAO;
    private PreviousPagesDAO previousPagesDAO;
    private EventBus mEventBus = EventBus.getDefault();
    private BroadcastReceiver gcmRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    private boolean isEditTextSelected;
    private static final int REQUEST_READ_PHONE_STATE = 1;
    private boolean isCardSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        ThemeChangeUtil.onActivityCreateSetTheme(this);
        themeId = getThemeId();
        setContentView(R.layout.activity_main);
        registerForGCM();
        Realm.init(this);
        historyDAO = new HistoryDAOImplementation();
        bookmarksDAO = new BookmarksDAOImplementation();
        openedPagesDAO = new OpenedPagesDAO();
        previousPagesDAO = new PreviousPagesDAO(this);
//        requestPermission();
        onOpenedNewPage();
    }

    private void requestPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            //TODO
        }
    }

    private void registerForGCM() {
        gcmRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean sentToken = SharedPreferencesHelper.isTokenSentToServer(context);
                if (sentToken) {
                    Log.d(TAG, "onReceive: tokenSent=true");
                } else {
                    Log.d(TAG, "onReceive: tokenSent=false");
                }
            }
        };
        registerReceiver();
        if (checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(gcmRegistrationBroadcastReceiver,
                    new IntentFilter(SharedPreferencesHelper.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }


    private int getThemeId(){
        try {
            Class<?> wrapper = Context.class;
            Method method = wrapper.getMethod("getThemeResId");
            method.setAccessible(true);
            return (Integer) method.invoke(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEventBus.register(this);
        registerReceiver();
        showPreviousPageButton(!previousPagesDAO.isEmpty());
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getThemeColour(String themeColour) {
        ThemeChangeUtil.changeToTheme(this, themeColour);
        mEventBus.removeStickyEvent(themeColour);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mEventBus.unregister(this);
        savePageToRealm();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(gcmRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
    }

//    @Override
//    protected void onDestroy() {
//        Log.d(TAG, "onDestroy: ");
//        openedPagesDAO.deleteAll();
//        previousPagesDAO.deleteAll();
//        super.onDestroy();
//    }

    @Override
    protected void onResumeFragments() {
        Log.d(TAG, "onResumeFragments: ");
        super.onResumeFragments();
        instantiateFragments();
    }

    @Override
    protected void onPostResume() {
        Log.d(TAG, "onPostResume: ");
        super.onPostResume();
        instantiateFragments();
    }

    /**
     * NavigationBar methods
     */
    @Override
    public void onPageSelected(String pageUrl) {
        goToPage(pageUrl, false);
    }

    @Override
    public void onHomeButtonPressed() {
        goToHomePage(true);
    }

    @Override
    public void onPreviousPageButtonPressed() {
        if (canGoBack())
        goToPreviousPage();
        NavigationBarFragment navigationBar = (NavigationBarFragment) getSupportFragmentManager().findFragmentById(R.id.navigationBarFragment);
        PageDetails pageDetails = getCurrentPageDetails();
        String url = pageDetails.getAddress();
        navigationBar.setAddressField(url);
        navigationBar.setAddressFieldError(false);
        showPreviousPageButton(!previousPagesDAO.isEmpty());
    }

    @Override
    public void onCardsButtonPressed() {
        Intent cardsIntent = new Intent(MainActivity.this, CardsActivity.class);
        startActivityForResult(cardsIntent, CARDS_REQUEST_CODE);
        clearHistory();
        isCardSelected = true;
    }


    /**
     * WebView methods
     */

    @Override
    public void onPageStarted(String url) {
//        Log.d(TAG, "onPageStarted: url=" + url);
        NavigationBarFragment navigationBar = (NavigationBarFragment) getSupportFragmentManager().findFragmentById(R.id.navigationBarFragment);
            if (navigationBar != null) {
                navigationBar.setLoadingPageProgressBar(true);
                navigationBar.hideFocusField();
            }
    }

    @Override
    public void onPageFinished(PageDetails pageDetails) {
        Log.d(TAG, "onPageFinished: ");
        String url = pageDetails.getAddress();
        NavigationBarFragment navigationBar = (NavigationBarFragment) getSupportFragmentManager().findFragmentById(R.id.navigationBarFragment);
        isEditTextSelected = navigationBar != null && navigationBar.getEditTextSelection();
        WebViewFragment webView = (WebViewFragment) getSupportFragmentManager().findFragmentById(R.id.webViewFragment);
        String previousPage = webView != null ? webView.getPreviousPage() : "";
        if (PageUrlValidator.isValid(url)) {
            if (isEditTextSelected || previousPage.equals("") || isCardSelected
                    || !previousPage.equals(getResources().getString(R.string.HOME_PAGE_ADDRESS))) {
                historyDAO.insert(pageDetails);
                setPageAddressField(url);
                savePreviousPage();
                showPreviousPageButton(!previousPagesDAO.isEmpty());
                isCardSelected = false;
            } else {
                OpenedPageModel pageModel = new OpenedPageModel();
                pageModel.setUrl(url);
                long newCardId = openedPagesDAO.insert(pageModel);
                SharedPreferencesHelper.setCardNumber(this, newCardId);
                historyDAO.insert(pageDetails);
                goToPage(pageModel.getUrl(), true);
                setPageAddressField(url);
                updateCardsCount();
                showPreviousPageButton(!previousPagesDAO.isEmpty());
                clearHistory();

            }

        }
        if (navigationBar != null ) {
            navigationBar.setEditTextSelected();
            navigationBar.setLoadingPageProgressBar(false);
        }
    }

    @Override
    public void onErrorReceived() {
        if (haveNetworkConnection()) {
            Log.d(TAG, "onErrorReceived: ");
            setAddressFieldError(true);
        } else {
            return;
        }
    }


    public void onSaveBookmarkClick() {
        PageDetails pageDetails = getCurrentPageDetails();
        String url = pageDetails.getAddress();
        if (url != null && PageUrlValidator.isValid(url)) {
            bookmarksDAO.insert(pageDetails);
            Toast.makeText(this, getString(R.string.bookmark_added), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onOpenedNewPage() {
        OpenedPageModel pageModel = new OpenedPageModel();
        pageModel.setUrl(getResources().getString(R.string.HOME_PAGE_ADDRESS));
        long newCardId = openedPagesDAO.insert(pageModel);
        SharedPreferencesHelper.setCardNumber(this, newCardId);
        goToPage(pageModel.getUrl(), true);
        showPreviousPageButton(false);
        updateCardsCount();
        clearHistory();
        Log.d(TAG, "onOpenedNewPage: pages size=" + openedPagesDAO.getSize());
    }

    @Override
    public void onUpdatePageIcon(PageDetails pageDetails) {
        historyDAO.update(pageDetails);
    }

    private void savePageToRealm() {
        OpenedPageModel openedPage = getOpenedPage();
        long cardId = SharedPreferencesHelper.getCardNumber(this);
        openedPage.setId(cardId);
        openedPagesDAO.update(openedPage);
        Log.d(TAG, "savePageToRealm: id=" + cardId + ", url=" + openedPage.getUrl());
    }

    private OpenedPageModel getOpenedPage() {
        WebViewFragment webView = (WebViewFragment) getSupportFragmentManager()
                .findFragmentById(R.id.webViewFragment);
        return webView.getOpenedPage();
    }

    private void goToHomePage(boolean isHomeButton) {
        String homePageAddress = getResources().getString(R.string.HOME_PAGE_ADDRESS);
        goToPage(homePageAddress, false);
    }

    private void goToPage(String rawUrl, boolean clearHistory) {
        Log.d(TAG, "Visiting page: " + rawUrl);
        String validUrlAddress = PageUrlValidator.validatePageUrl(rawUrl);
        SharedPreferencesHelper.setCardUrl(this, validUrlAddress);
//        setPageAddressField(validUrlAddress);
        setAddressFieldError(false);
        loadPageToWebView(validUrlAddress);
        if (clearHistory) clearHistory();
        showPreviousPageButton(!previousPagesDAO.isEmpty());
    }

    private void goToPreviousPage() {
//        String url = previousPagesDAO.removeLastAndGetNext();
//        goToPage(url, false);
        WebViewFragment webView = (WebViewFragment) getSupportFragmentManager().findFragmentById(R.id.webViewFragment);
        if (webView != null) {
            webView.goToPreviousPage();
        }
    }

    private void savePreviousPage() {
        String url = SharedPreferencesHelper.getCardUrl(this);
//        if ( !previousPagesDAO.isEmpty() && url.equals(getHomePageAddress()) ||
//                !url.equals(getHomePageAddress())) {
            previousPagesDAO.insert(url);
            Log.d(TAG, "savePreviousPage: url=" + url);
//        }
    }


    private void setPageAddressField(String pageUrl) {
        NavigationBarFragment navigationBar = (NavigationBarFragment) getSupportFragmentManager().findFragmentById(R.id.navigationBarFragment);
        if (navigationBar != null ) {
            navigationBar.setAddressField(pageUrl);
        }
    }

    private void setAddressFieldError(boolean errorOccurred) {
        NavigationBarFragment navigationBar = (NavigationBarFragment) getSupportFragmentManager().findFragmentById(R.id.navigationBarFragment);
        if (navigationBar != null ) {
            navigationBar.setAddressFieldError(errorOccurred);
        }
    }

    private void showPreviousPageButton(boolean show) {
        NavigationBarFragment navigationBar = (NavigationBarFragment) getSupportFragmentManager().findFragmentById(R.id.navigationBarFragment);
        if (navigationBar != null) {
            navigationBar.showPreviousPageButton(canGoBack());
        }
    }

    private void updateCardsCount() {
        NavigationBarFragment navigationBar = (NavigationBarFragment) getSupportFragmentManager().findFragmentById(R.id.navigationBarFragment);
        if (navigationBar != null) {
            navigationBar.updateCardsCount();
        }
    }

    private void loadPageToWebView(String pageUrl) {
        if (haveNetworkConnection()) {
            WebViewFragment webView = (WebViewFragment) getSupportFragmentManager().findFragmentById(R.id.webViewFragment);
            if (webView != null) {
                webView.goToSelectedPage(pageUrl);
            }
        } else {
            Toast.makeText(this, "Check network connection", Toast.LENGTH_LONG).show();
        }
    }

    private boolean canGoBack() {
        WebViewFragment webView = (WebViewFragment) getSupportFragmentManager().findFragmentById(R.id.webViewFragment);
        return webView != null && webView.canGoBack();
    }

    private void clearHistory() {
        WebViewFragment webView = (WebViewFragment) getSupportFragmentManager().findFragmentById(R.id.webViewFragment);
        if (webView !=null)
        webView.clearHistory();
    }

    private PageDetails getCurrentPageDetails() {
        WebViewFragment webView = (WebViewFragment) getSupportFragmentManager().findFragmentById(R.id.webViewFragment);
        return webView != null ? webView.getCurrentPageDetails() : new PageDetails();
    }


    /**
     * Android 4.4 requires to manually add fragments on Activity restart
     * @return
     */
    private void instantiateFragments() {
        instantiateFragment(
                R.id.navigationBarFragment,
                new NavigationBarFragment());
        instantiateFragment(
                R.id.webViewFragment,
                new WebViewFragment());
    }

    private void instantiateFragment(int containerId, Fragment newInstance) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(containerId);
        if (fragment != null) return;
        fragmentManager.beginTransaction().replace(containerId, newInstance).addToBackStack(null).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch(requestCode) {
                case THEME_COLOUR_REQUEST_CODE:
                    ThemeChangeUtil.changeToTheme(this, data.getStringExtra("Colour"));
                    break;
                case CARDS_REQUEST_CODE:
                    isCardSelected = true;
                    loadSelectedCard();
//                    cardAmount = data.getIntExtra("cards_amount", 1);
                    break;
                case HISTORY_REQUEST_CODE:
                    isCardSelected = true;
                    loadSelectedCard();
                    break;
                case BOOKMARKS_REQUEST_CODE:
                    isCardSelected = true;
                    loadSelectedCard();
            }
        }
    }

    public int getCardsAmount() {
        return openedPagesDAO.getOpenedPages().size();
    }

    private void loadSelectedCard() {
        String url = SharedPreferencesHelper.getCardUrl(this);
        Log.d(TAG, "loadSelectedCard: url=" + url);
        goToPage(url, true);
    }

    @Override
    public void onBackPressed() {
        if (canGoBack()) {
            onPreviousPageButtonPressed();
        } else {
            super.onBackPressed();
            finish();
        }
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();

        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

}
