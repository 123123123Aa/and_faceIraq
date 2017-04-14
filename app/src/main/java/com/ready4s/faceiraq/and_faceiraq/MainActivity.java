package com.ready4s.faceiraq.and_faceiraq;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.ready4s.faceiraq.and_faceiraq.model.PageUrlValidator;
import com.ready4s.faceiraq.and_faceiraq.view.NavigationBarFragment;
import com.ready4s.faceiraq.and_faceiraq.view.WebViewFragment;

import java.util.ArrayList;
import java.util.List;

import static android.media.CamcorderProfile.get;

public class MainActivity extends FragmentActivity implements NavigationBarFragment.OnNavigationBarActionListener {

    private static final String TAG = "MainActivity";
    private String currentPage;
    private List<String> visitedPagesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
        init();
        currentPage = getHomePageAddress();
    }

    @Override
    protected void onStart() {
        super.onStart();
        goToPage(currentPage);
    }

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

    private void init() {
        initFragment(R.id.navigationBarFragment, new NavigationBarFragment());
        initFragment(R.id.webViewFragment, new WebViewFragment());
    }

    private void initFragment(int containerId, Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(containerId, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    private String getHomePageAddress() {
        return getResources().getString(R.string.HOME_PAGE_ADDRESS);
    }
}
