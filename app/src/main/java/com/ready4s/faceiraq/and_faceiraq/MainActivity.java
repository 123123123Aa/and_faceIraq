package com.ready4s.faceiraq.and_faceiraq;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.ready4s.faceiraq.and_faceiraq.controller.CardsActivity;
import com.ready4s.faceiraq.and_faceiraq.dialog.MainDialogFragment;
import com.ready4s.faceiraq.and_faceiraq.model.PageDetails;
import com.ready4s.faceiraq.and_faceiraq.model.SharedPreferencesHelper;
import com.ready4s.faceiraq.and_faceiraq.model.database.bookmarks.BookmarksDAOImplementation;
import com.ready4s.faceiraq.and_faceiraq.model.database.history.HistoryDAOImplementation;
import com.ready4s.faceiraq.and_faceiraq.model.database.opened_pages.OpenedPageModel;
import com.ready4s.faceiraq.and_faceiraq.model.database.opened_pages.OpenedPagesDAO;
import com.ready4s.faceiraq.and_faceiraq.model.database.previous_pages.PreviousPagesDAO;
import com.ready4s.faceiraq.and_faceiraq.model.utils.PageUrlValidator;
import com.ready4s.faceiraq.and_faceiraq.model.utils.ThemeChangeUtil;
import com.ready4s.faceiraq.and_faceiraq.view.NavigationBarFragment;
import com.ready4s.faceiraq.and_faceiraq.view.WebViewFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;

import static com.ready4s.faceiraq.and_faceiraq.controller.BookmarksActivity.BOOKMARKS_REQUEST_CODE;
import static com.ready4s.faceiraq.and_faceiraq.controller.CardsActivity.CARDS_REQUEST_CODE;
import static com.ready4s.faceiraq.and_faceiraq.controller.HistoryActivity.HISTORY_REQUEST_CODE;
import static com.ready4s.faceiraq.and_faceiraq.theme.colour.ThemeColourActivity.THEME_COLOUR_REQUEST_CODE;

public class MainActivity extends FragmentActivity
        implements NavigationBarFragment.OnNavigationBarActionListener,
                    WebViewFragment.OnWebViewActionListener,
                    MainDialogFragment.OnMainDialogActionsListener {

    private static final String TAG = "MainActivity";
    private int themeId;
    private HistoryDAOImplementation historyDAO;
    private BookmarksDAOImplementation bookmarksDAO;
    private OpenedPagesDAO openedPagesDAO;
    private PreviousPagesDAO previousPagesDAO;
    private EventBus mEventBus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        ThemeChangeUtil.onActivityCreateSetTheme(this);
        themeId = getThemeId();
        setContentView(R.layout.activity_main);
        Realm.init(this);
        historyDAO = new HistoryDAOImplementation();
        bookmarksDAO = new BookmarksDAOImplementation();
        openedPagesDAO = new OpenedPagesDAO();
        previousPagesDAO = new PreviousPagesDAO(this);
        goToHomePage(false);
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
    protected void onResume() {
        super.onResume();
        mEventBus.register(this);
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
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        openedPagesDAO.deleteAll();
        previousPagesDAO.deleteAll();
        super.onDestroy();
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

    /**
     * NavigationBar methods
     */
    @Override
    public void onPageSelected(String pageUrl) {
        goToPage(pageUrl, true);
    }

    @Override
    public void onHomeButtonPressed() {
        goToHomePage(true);
    }

    @Override
    public void onPreviousPageButtonPressed() {
        Log.d(TAG, "PREonPreviousPageButtonPressed: size=" + previousPagesDAO.getSize());
        if (!previousPagesDAO.isEmpty()) {
            goToPreviousPage();
        }
        Log.d(TAG, "POSTonPreviousPageButtonPressed: size=" + previousPagesDAO.getSize());
    }

    @Override
    public void onCardsButtonPressed() {
        Intent cardsIntent = new Intent(MainActivity.this, CardsActivity.class);
        startActivityForResult(cardsIntent, CARDS_REQUEST_CODE);
    }


    /**
     * WebView methods
     */

    @Override
    public void onPageStarted(String url) {
//        Log.d(TAG, "onPageStarted: url=" + url);
        SharedPreferencesHelper.setCardUrl(this, url);
    }

    @Override
    public void onPageFinished(PageDetails pageDetails) {
        Log.d(TAG, "onPageFinished: ");
        String url = pageDetails.getAddress();
        if (PageUrlValidator.isValid(url)) {
            historyDAO.insert(pageDetails);
            setPageAddressField(url);
            savePreviousPage();
            showPreviousPageButton(!previousPagesDAO.isEmpty());
        }
    }

    @Override
    public void onErrorReceived() {
        Log.d(TAG, "onErrorReceived: ");
        setAddressFieldError(true);
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
        goToPage(pageModel.getUrl(), false);
        showPreviousPageButton(false);
        updateCardsCount();
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
        goToPage(homePageAddress, isHomeButton);
    }

    private void goToPage(String rawUrl, boolean saveToPreviousPages) {
        Log.d(TAG, "Visiting page: " + rawUrl);
        String validUrlAddress = PageUrlValidator.validatePageUrl(rawUrl);
//        if (saveToPreviousPages) savePreviousPage();
        SharedPreferencesHelper.setCardUrl(this, validUrlAddress);
//        setPageAddressField(validUrlAddress);
        setAddressFieldError(false);
        loadPageToWebView(validUrlAddress);
        showPreviousPageButton(!previousPagesDAO.isEmpty());
    }

    private void goToPreviousPage() {
        String url = previousPagesDAO.removeLastAndGetNext();
        goToPage(url, false);
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
            navigationBar.showPreviousPageButton(show);
        }
    }

    private void updateCardsCount() {
        NavigationBarFragment navigationBar = (NavigationBarFragment) getSupportFragmentManager().findFragmentById(R.id.navigationBarFragment);
        if (navigationBar != null) {
            navigationBar.updateCardsCount();
        }
    }

    private void loadPageToWebView(String pageUrl) {
        WebViewFragment webView = (WebViewFragment) getSupportFragmentManager().findFragmentById(R.id.webViewFragment);
        if (webView != null) {
            webView.goToSelectedPage(pageUrl);
        }
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
                    loadSelectedCard();
//                    cardAmount = data.getIntExtra("cards_amount", 1);
                    break;
                case HISTORY_REQUEST_CODE:
                    loadSelectedCard();
                    break;
                case BOOKMARKS_REQUEST_CODE:
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
        goToPage(url, false);
    }

    private String getHomePageAddress() {
        return getString(R.string.HOME_PAGE_ADDRESS);
    }

    @Override
    public void onBackPressed() {
        if (!previousPagesDAO.isEmpty()) {
            goToPreviousPage();
        } else {
            super.onBackPressed();
            finish();
        }
    }
}
