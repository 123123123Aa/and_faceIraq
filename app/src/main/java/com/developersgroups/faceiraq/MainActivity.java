package com.developersgroups.faceiraq;

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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.developersgroups.faceiraq.controller.CardsActivity;
import com.developersgroups.faceiraq.dialog.MainDialogFragment;
import com.developersgroups.faceiraq.model.PageDetails;
import com.developersgroups.faceiraq.model.SharedPreferencesHelper;
import com.developersgroups.faceiraq.model.database.bookmarks.BookmarksDAOImplementation;
import com.developersgroups.faceiraq.model.database.history.HistoryDAOImplementation;
import com.developersgroups.faceiraq.model.database.opened_pages.OpenedPageModel;
import com.developersgroups.faceiraq.model.database.opened_pages.OpenedPagesDAO;
import com.developersgroups.faceiraq.utils.PageUrlValidator;
import com.developersgroups.faceiraq.utils.ThemeChangeUtil;
import com.developersgroups.faceiraq.view.NavigationBarFragment;
import com.developersgroups.faceiraq.view.WebViewFragment;
import com.google.firebase.iid.FirebaseInstanceId;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;

import static com.developersgroups.faceiraq.R.id.webViewFragment;
import static com.developersgroups.faceiraq.controller.BookmarksActivity.BOOKMARKS_REQUEST_CODE;
import static com.developersgroups.faceiraq.controller.CardsActivity.CARDS_REQUEST_CODE;
import static com.developersgroups.faceiraq.controller.CardsActivity.HOME_BUTTON_CARDS_SELECTED;
import static com.developersgroups.faceiraq.controller.HistoryActivity.HISTORY_REQUEST_CODE;
import static com.developersgroups.faceiraq.theme.colour.ThemeColourActivity.THEME_COLOUR_REQUEST_CODE;

public class MainActivity extends FragmentActivity
        implements NavigationBarFragment.OnNavigationBarActionListener,
                    WebViewFragment.OnWebViewActionListener,
                    MainDialogFragment.OnMainDialogActionsListener {

    private static final String TAG = "MainActivity";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int WEB_VIEW_URL_CLICKED = 7;
    private static final int WEB_VIEW_CLICKED = 8;
    private int themeId;
    private HistoryDAOImplementation historyDAO;
    private BookmarksDAOImplementation bookmarksDAO;
    private OpenedPagesDAO openedPagesDAO;
    private EventBus mEventBus = EventBus.getDefault();
    private BroadcastReceiver fcmRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    private boolean isEditTextSelected;
    private static final int REQUEST_READ_PHONE_STATE = 1;
    private boolean isCardSelected;
    private MyApplication mApplication;
    private String previousPageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        ThemeChangeUtil.onActivityCreateSetTheme(this);
        themeId = getThemeId();
        setContentView(R.layout.activity_main);
//        registerFirebase();
        Realm.init(this);
        historyDAO = new HistoryDAOImplementation();
        bookmarksDAO = new BookmarksDAOImplementation();
        openedPagesDAO = new OpenedPagesDAO();
        mApplication = (MyApplication) getApplication();
//        requestPermission();

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getString("url") != null) {
            Log.d(TAG, "url from extras");
            String url = extras.getString("url");
            OpenedPageModel openedPageModel = new OpenedPageModel();
            openedPageModel.setUrl(url);
            openNewPage(openedPageModel, "");
        } else if (mApplication.isOpen()) {
            Log.d(TAG, "url home page");
            goToHomePage(false);
            mApplication.setOpen();
        } else {
            Log.d(TAG, "url from history");
            goToPage(historyDAO.getLastUrl(), false);
        }
        Log.d(TAG, "UUID: " + SharedPreferencesHelper.getUUID(this));
        WebViewFragment webView = (WebViewFragment) getSupportFragmentManager().findFragmentById(webViewFragment);
//webView.initScrollListener();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent: ");
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if (extras != null && extras.getString("url") != null) {
            Log.d(TAG, "url from extras");
            String url = extras.getString("url");
            OpenedPageModel openedPageModel = new OpenedPageModel();
            openedPageModel.setUrl(url);
            openNewPage(openedPageModel, "");
        }
    }

    private void requestPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            //TODO
        }
    }

    private void registerFirebase() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "registerFirebase: token: " + token);
        fcmRegistrationBroadcastReceiver = new BroadcastReceiver() {
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
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(fcmRegistrationBroadcastReceiver,
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
        showPreviousPageButton(canGoBack());
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
        savePageToRealm("");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fcmRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
    }


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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        outState.putString("url", historyDAO.getLastUrl());
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
        setAddressFieldError(false);
        showPreviousPageButton(canGoBack());
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
    public void onPageStarted(PageDetails pageDetails) {
//        Log.d(TAG, "onPageStarted: url=" + url);
        NavigationBarFragment navigationBar = (NavigationBarFragment) getSupportFragmentManager().findFragmentById(R.id.navigationBarFragment);
        if (navigationBar != null) {
            navigationBar.setLoadingPageProgressBar(true);
            navigationBar.hideFocusField();
        }

        String url = pageDetails.getAddress();

        if (previousPageWasFaceiraq && webViewClicked) {
            OpenedPageModel pageModel = new OpenedPageModel();
            pageModel.setUrl(url);
            openNewPage(pageModel, previousPageUrl);
        } else {
            showPreviousPageButton(canGoBack());
        }

        previousPageWasFaceiraq = false;
        webViewClicked = false;

    }

    @Override
    public void onPageFinished(PageDetails pageDetails) {
        Log.d(TAG, "onPageFinished: ");
        NavigationBarFragment navigationBar = (NavigationBarFragment) getSupportFragmentManager().findFragmentById(R.id.navigationBarFragment);
        if (navigationBar != null ) {
            navigationBar.setEditTextSelected();
            navigationBar.setLoadingPageProgressBar(false);
            navigationBar.setAddressField(pageDetails.getAddress());
        }
        historyDAO.insert(pageDetails);
    }

    @Override
    public void hideLoadingSpinner() {
//        setAddressFieldError(false);
        hideLoadingPageProgressBar();
        WebViewFragment webView = (WebViewFragment) getSupportFragmentManager().findFragmentById(webViewFragment);
        if (webView != null) {
            webView.scrollTo();
        }
        if (urlOpenedInNewCard) {
            clearHistory();
            urlOpenedInNewCard = false;
        }
        showPreviousPageButton(canGoBack());
    }

    boolean webViewClicked = false;
    boolean previousPageWasFaceiraq = false;
    boolean urlOpenedInNewCard = false;
    @Override
    public void onNewPageStartedLoading(String previousUrl) {
        if (previousUrl.equals(getResources().getString(R.string.HOME_PAGE_ADDRESS))) {
            previousPageWasFaceiraq = true;
            previousPageUrl = previousUrl;
        }
    }

    @Override
    public void webViewClicked() {
        webViewClicked = true;
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
        openNewPage(pageModel, "");
        Log.d(TAG, "onOpenedNewPage: pages size=" + openedPagesDAO.getSize());
    }

    private void openNewPage(OpenedPageModel pageModel, String urlToSave) {
        savePageToRealm(urlToSave);
        openNewWebViewFragmentWithUrl(pageModel.getUrl(), R.anim.enter_to_left, R.anim.exit_to_left);
        long newCardId = openedPagesDAO.insert(pageModel);
        SharedPreferencesHelper.setCardNumber(this, newCardId);
        updateCardsCount();
        urlOpenedInNewCard = true;
        showPreviousPageButton(false);
        clearHistory();
        goToPage(pageModel.getUrl(), true);
    }

    @Override
    public void onUpdatePageIcon(PageDetails pageDetails) {
        historyDAO.update(pageDetails);
    }

    private void savePageToRealm(String urlToSave) {
        OpenedPageModel openedPage = getOpenedPage();
        long cardId = SharedPreferencesHelper.getCardNumber(this);
        if (!urlToSave.isEmpty()) {
            openedPage.setUrl(urlToSave);
        }
        openedPage.setId(cardId);
        openedPagesDAO.update(openedPage);
        Log.d(TAG, "savePageToRealm: id=" + cardId + ", url=" + openedPage.getUrl());
    }

    private OpenedPageModel getOpenedPage() {
        WebViewFragment webView = (WebViewFragment) getSupportFragmentManager()
                .findFragmentByTag(WebViewFragment.TAG);
        return webView != null ? webView.getOpenedPage() : new OpenedPageModel();
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
        showPreviousPageButton(canGoBack());
    }

    private void goToPreviousPage() {
        WebViewFragment webView = (WebViewFragment) getSupportFragmentManager().findFragmentById(webViewFragment);
        if (webView != null) {
            webView.goToPreviousPage();
        }
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
            navigationBar.showPreviousPageButton(show);
        }
    }

    private void updateCardsCount() {
        NavigationBarFragment navigationBar = (NavigationBarFragment) getSupportFragmentManager().findFragmentById(R.id.navigationBarFragment);
        if (navigationBar != null) {
            navigationBar.updateCardsCount();
        }
    }

    private void hideLoadingPageProgressBar() {
        NavigationBarFragment navigationBar = (NavigationBarFragment) getSupportFragmentManager().findFragmentById(R.id.navigationBarFragment);
        if (navigationBar != null) {
            navigationBar.setLoadingPageProgressBar(false);
        }
    }

    private void loadPageToWebView(String pageUrl) {
        if (haveNetworkConnection()) {
            WebViewFragment webView = (WebViewFragment) getSupportFragmentManager().findFragmentById(webViewFragment);
            if (webView != null) {
                webView.goToSelectedPage(pageUrl);
            }
        } else {
            Toast.makeText(this, "Check network connection", Toast.LENGTH_LONG).show();
        }
    }

    private boolean canGoBack() {
        WebViewFragment webView = (WebViewFragment) getSupportFragmentManager().findFragmentById(webViewFragment);
        return webView != null && webView.canGoBack();
    }

    private void stopLoadingCurrentUrl() {
        WebViewFragment webView = (WebViewFragment) getSupportFragmentManager().findFragmentById(webViewFragment);
        if (webView != null) {
            webView.stopLoading();
        }
    }

    private void clearHistory() {
        WebViewFragment webView = (WebViewFragment) getSupportFragmentManager().findFragmentById(webViewFragment);
        if (webView !=null)
        webView.clearHistory();
    }

    private PageDetails getCurrentPageDetails() {
        WebViewFragment webView = (WebViewFragment) getSupportFragmentManager().findFragmentById(webViewFragment);
        return webView != null ? webView.getCurrentPageDetails() : new PageDetails();
    }


    /**
     * Android 4.4 requires to manually add fragments on Activity restart
     * @return
     */
    private void instantiateFragments() {
        instantiateFragment(
                R.id.navigationBarFragment,
                new NavigationBarFragment(),
                NavigationBarFragment.TAG);
        instantiateFragment(
                webViewFragment,
                new WebViewFragment(),
                WebViewFragment.TAG);
    }

    private void instantiateFragment(int containerId, Fragment newInstance, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(containerId);
        if (fragment != null) return;
        Bundle bundle = new Bundle();
        String url = mApplication.isOpen() ? getResources().getString(R.string.HOME_PAGE_ADDRESS) : historyDAO.getLastUrl();
        bundle.putString("url", url);
        newInstance.setArguments(bundle);
        fragmentManager.beginTransaction().replace(containerId, newInstance, tag).addToBackStack(null).commitAllowingStateLoss();
    }

    private void openNewWebViewFragmentWithUrl(String url, int enterAnimId, int exitAnimId) {
        int containerId = webViewFragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(enterAnimId, exitAnimId);
        WebViewFragment webViewFragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        webViewFragment.setArguments(bundle);
        transaction.replace(containerId, webViewFragment, WebViewFragment.TAG).addToBackStack(null).commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( resultCode == HOME_BUTTON_CARDS_SELECTED) {
            isCardSelected = false;
            goToHomePage(true);
        }
        else if (resultCode == RESULT_OK) {
            switch(requestCode) {
                case THEME_COLOUR_REQUEST_CODE:
                    ThemeChangeUtil.changeToTheme(this, data.getStringExtra("Colour"));
                    break;
                case CARDS_REQUEST_CODE:
                    isCardSelected = true;
                    String url = SharedPreferencesHelper.getCardUrl(this);
                    if (url.equals("") ){
                        goToPage(getResources().getString(R.string.HOME_PAGE_ADDRESS), false);
                        clearHistory();
                    } else {
                        loadSelectedCard();
                        clearHistory();}
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
        openNewWebViewFragmentWithUrl(url, R.anim.enter_to_right, R.anim.exit_to_right);
//        Log.d(TAG, "loadSelectedCard: url=" + url);
//        goToPage(url, true);
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
