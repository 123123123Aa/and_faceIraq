package com.ready4s.faceiraq.and_faceiraq;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
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
    private EventBus mEventBus = EventBus.getDefault();

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
    protected void onResume() {
        super.onResume();
        mEventBus.register(this);
//        if(!SharedPreferencesColourTheme.getThemeColour(this).equals("1")) {
//            ThemeChangeUtil.changeToTheme(this, SharedPreferencesColourTheme.getThemeColour(this));
//        }
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

    @Override
    public void onOpenedNewPage() {
        OpenedPageModel pageModel = new OpenedPageModel();
        pageModel.setUrl(getResources().getString(R.string.HOME_PAGE_ADDRESS));
        openedPagesDAO.insert(pageModel);
        goToPage(pageModel.getUrl());
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
