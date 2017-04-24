package com.ready4s.faceiraq.and_faceiraq;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.ready4s.faceiraq.and_faceiraq.controller.CardsActivity;
import com.ready4s.faceiraq.and_faceiraq.model.PageDetails;
import com.ready4s.faceiraq.and_faceiraq.model.SharedPreferencesHelper;
import com.ready4s.faceiraq.and_faceiraq.model.database.bookmarks.BookmarksDAOImplementation;
import com.ready4s.faceiraq.and_faceiraq.model.database.history.HistoryDAOImplementation;
import com.ready4s.faceiraq.and_faceiraq.model.database.opened_pages.OpenedPageModel;
import com.ready4s.faceiraq.and_faceiraq.model.database.opened_pages.OpenedPagesDAO;
import com.ready4s.faceiraq.and_faceiraq.model.utils.PageUrlValidator;
import com.ready4s.faceiraq.and_faceiraq.model.utils.ThemeChangeUtil;
import com.ready4s.faceiraq.and_faceiraq.view.NavigationBarFragment;
import com.ready4s.faceiraq.and_faceiraq.view.WebViewFragment;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

import java.lang.reflect.Method;

import io.realm.Realm;

public class MainActivity extends FragmentActivity
        implements NavigationBarFragment.OnNavigationBarActionListener,
                    WebViewFragment.OnWebViewActionListener {

    private static final String TAG = "MainActivity";
    private static final int CARDS_REQUEST_CODE = 111;
    private static final int HISTORY_REQUEST_CODE = 11;
    private int themeId;
    private int cardAmount;
    private HistoryDAOImplementation historyDAO;
    private BookmarksDAOImplementation bookmarksDAO;
    private OpenedPagesDAO openedPagesDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        ThemeChangeUtil.onActivityCreateSetTheme(this);
        themeId = getThemeId();
        setContentView(R.layout.activity_main);
//        init();
        Realm.init(this);
        historyDAO = new HistoryDAOImplementation();
        bookmarksDAO = new BookmarksDAOImplementation();
        openedPagesDAO = new OpenedPagesDAO();
        goToHomePage();
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
    protected void onPause() {
        super.onPause();
        savePageToRealm();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        openedPagesDAO.deleteAll();
        super.onDestroy();
    }

    /**
     * NavigationBar methods
     */
    @Override
    public void onPageSelected(String pageUrl) {
        goToPage(pageUrl);
    }

    @Override
    public void onHomeButtonPressed() {
        goToHomePage();
    }

    @Override
    public void onPreviousPageButtonPressed() {
//        goToPreviousPage();
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
    public void onPageFinished(PageDetails pageDetails) {
        historyDAO.insert(pageDetails);
        setPageAddressField(pageDetails.getAddress());
    }

    public void onSaveBookmarkClick() {
        PageDetails pageDetails = getCurrentPageDetails();
        bookmarksDAO.insert(pageDetails);
        Toast.makeText(this, getString(R.string.bookmark_added), Toast.LENGTH_SHORT).show();
    }


    public void onOpenedNewPage() {
        OpenedPageModel pageModel = new OpenedPageModel();
        openedPagesDAO.insert(pageModel);
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

    private void goToHomePage() {
        String homePageAddress = getResources().getString(R.string.HOME_PAGE_ADDRESS);
        goToPage(homePageAddress);
    }

    private void goToPage(String rawUrl) {
        Log.d(TAG, "Visiting page: " + rawUrl);
        String validUrlAddress = PageUrlValidator.validatePageUrl(rawUrl);
        SharedPreferencesHelper.setCardUrl(this, validUrlAddress);
//        saveVisitedPage(validUrlAddress);
        setPageAddressField(validUrlAddress);
        loadPageToWebView(validUrlAddress);
    }

    private void setPageAddressField(String pageUrl) {
        NavigationBarFragment navigationBar = (NavigationBarFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navigationBarFragment);
        navigationBar.setAddressField(pageUrl);
    }

    private void loadPageToWebView(String pageUrl) {
        WebViewFragment webView = (WebViewFragment) getSupportFragmentManager()
                .findFragmentById(R.id.webViewFragment);
        webView.goToSelectedPage(pageUrl);
    }

    private PageDetails getCurrentPageDetails() {
        WebViewFragment webView = (WebViewFragment) getSupportFragmentManager()
                .findFragmentById(R.id.webViewFragment);
        return webView.getCurrentPageDetails();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch(requestCode) {
                case 1:
                    ThemeChangeUtil.changeToTheme(this, data.getStringExtra("Colour"));
                    break;
                case CARDS_REQUEST_CODE:
                    loadSelectedCard();
                    cardAmount = data.getIntExtra("cards_amount", 1);
                    break;
                case HISTORY_REQUEST_CODE:
                    loadSelectedCard();
                    break;
            }
        }
    }

    public int getCardsAmount() {
        return openedPagesDAO.getOpenedPages().size();
    }

    private void loadSelectedCard() {
        String url = SharedPreferencesHelper.getCardUrl(this);
        Log.d(TAG, "loadSelectedCard: url=" + url);
        goToPage(url);
    }

    private void init() {
        initFragment(R.id.navigationBarFragment, new NavigationBarFragment());
        initFragment(R.id.webViewFragment, new WebViewFragment());
    }

    private void initFragment(int containerId, Fragment newFragment) {
        Fragment fragment =  getSupportFragmentManager()
                .findFragmentById(containerId);
        if (fragment != null && fragment.getClass().equals(newFragment.getClass())) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(containerId, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    private String getHomePageAddress() {
        return getResources().getString(R.string.HOME_PAGE_ADDRESS);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
