package com.ready4s.faceiraq.and_faceiraq;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.webkit.WebView;

import com.ready4s.faceiraq.and_faceiraq.controller.CardsActivity;
import com.ready4s.faceiraq.and_faceiraq.dialog.MainDialogAdapter;
import com.ready4s.faceiraq.and_faceiraq.model.PageDetails;
import com.ready4s.faceiraq.and_faceiraq.model.database.bookmarks.BookmarksDAOImplementation;
import com.ready4s.faceiraq.and_faceiraq.model.database.history.HistoryDAOImplementation;
import com.ready4s.faceiraq.and_faceiraq.model.utils.PageUrlValidator;
import com.ready4s.faceiraq.and_faceiraq.view.NavigationBarFragment;
import com.ready4s.faceiraq.and_faceiraq.view.WebViewFragment;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class MainActivity extends FragmentActivity
        implements NavigationBarFragment.OnNavigationBarActionListener,
                    WebViewFragment.OnWebViewActionListener {

    private static final String TAG = "MainActivity";
    private String currentPage;
    private List<String> visitedPagesList = new ArrayList<>();
    private HistoryDAOImplementation historyDAO;
    private BookmarksDAOImplementation bookmarksDAO;
    public String themeColour;
    private List<WebView> openedPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
//        init();
        currentPage = getHomePageAddress();
        Realm.init(this);
        openedPages = new ArrayList<>();
        historyDAO = new HistoryDAOImplementation();
        bookmarksDAO = new BookmarksDAOImplementation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        goToPage(currentPage);
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
        goToPreviousPage();
    }

    @Override
    public void onCardsButtonPressed() {
        Intent cardsIntent = new Intent(MainActivity.this, CardsActivity.class);
        startActivity(cardsIntent);
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
    }


    public void onOpenedNewPage() {

    }

    @Override
    public void onUpdatePageIcon(PageDetails pageDetails) {
        historyDAO.update(pageDetails);
    }


    private void goToPreviousPage() {
        if ( !visitedPagesList.isEmpty()) {
            String lastVisitedPage = getPreviousPage();
            goToPage(lastVisitedPage);
            removeLastVisitedPage();
        }
    }

    private void goToHomePage() {
        String homePageAddress = getResources().getString(R.string.HOME_PAGE_ADDRESS);
        goToPage(homePageAddress);
    }

    private void goToPage(String rawUrl) {
        Log.d(TAG, "Visiting page: " + rawUrl);
        String validUrlAddress = PageUrlValidator.validatePageUrl(rawUrl);
        saveVisitedPage(validUrlAddress);
        setPageAddressField(validUrlAddress);
        loadPageToWebView(validUrlAddress);
    }

    private String getPreviousPage() {
        return visitedPagesList.isEmpty() ? "" : visitedPagesList.get(visitedPagesList.size() - 1);
    }

    private void removeLastVisitedPage() {
        visitedPagesList.remove(visitedPagesList.size() - 1);
    }

    private void saveVisitedPage(String pageUrl) {
        if ( !getPreviousPage().equals(currentPage) ) {
            currentPage = pageUrl;
            visitedPagesList.add(currentPage);
        }
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
        if (requestCode == 1)
            if (resultCode == RESULT_OK) {
                themeColour = data.getStringExtra("Colour");
            }
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
