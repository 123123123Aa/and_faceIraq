package com.developersgroups.faceiraq.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.developersgroups.faceiraq.R;
import com.developersgroups.faceiraq.model.PageDetails;
import com.developersgroups.faceiraq.model.database.opened_pages.OpenedPageModel;
import com.developersgroups.faceiraq.utils.ImageUtil;
import com.developersgroups.faceiraq.utils.TimeUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.graphics.Bitmap.Config.ARGB_8888;

/**
 * Created by Paweł Sałata on 14.04.2017.
 * email: psalata9@gmail.com
 */

public class WebViewFragment extends Fragment implements View.OnTouchListener {

    public static final String TAG = "WebViewFragment";

    public interface OnWebViewActionListener {

        public void onPageFinished(PageDetails pageDetails);

        public void onUpdatePageIcon(PageDetails pageDetails);
        public void onErrorReceived();
        void onPageStarted(PageDetails pageDetails);
        void hideLoadingSpinner();

        void onNewPageStartedLoading(String previousUrl);

        void webViewClicked();
    }
    OnWebViewActionListener onWebViewActionListener;
    com.developersgroups.faceiraq.view.BrowserChromeClient browserChromeClient;

    @Bind(R.id.pageDisplay)
    WebView pageDisplay;

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeRefreshLayout;

    public WebViewFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onWebViewActionListener = (OnWebViewActionListener) context;
            browserChromeClient = new com.developersgroups.faceiraq.view.BrowserChromeClient(onWebViewActionListener);
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnWebViewActionListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.browser_web_view, container, false);
        ButterKnife.bind(this, view);

        init();
        pageDisplay.setOnTouchListener(this);

        Bundle args = getArguments();
        if (args != null) {
            String url = args.getString("url");
            pageDisplay.loadUrl(url);
        }

        initScrollListener();

        return view;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view.getId() == R.id.pageDisplay && motionEvent.getAction() == MotionEvent.ACTION_UP){
            onWebViewActionListener.webViewClicked();
        }
        return false;
    }


    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    public void initScrollListener() {
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipeRefreshLayout.isRefreshing() ) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                pageDisplay.reload();
            }
        });
//        pageDisplay.setOnScrollChangedCallback(new ObservableWebView.OnScrollChangedCallback() {
//            @Override
//            public void onScroll(int l, int t) {
//                if (t == 0)
//                pageDisplay.loadUrl(getCurrentPageDetails().getAddress());
//            }
//        });

    }

    public OpenedPageModel getOpenedPage() {
        OpenedPageModel page= new OpenedPageModel();
        page.setTitle(pageDisplay.getTitle());
        page.setUrl(pageDisplay.getUrl());
        byte[] byteScreenshot = ImageUtil.convertToByteArray(takeScreenshot());
        page.setScreenshot(byteScreenshot);
        return page;
    }

    public void scrollTo() {
        pageDisplay.scrollTo(0, 10);
    }

    private Bitmap takeScreenshot() {
        View screenView = getView();
        Bitmap bitmap;
        if (screenView == null) {
            return Bitmap.createBitmap(1080, 1593, ARGB_8888);
        }
        screenView.setDrawingCacheEnabled(true);
        if(screenView.getDrawingCache() != null)
            bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        else
            bitmap = Bitmap.createBitmap(1080, 1593, ARGB_8888);
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public void goToSelectedPage(String pageUrl) {
        Log.d(TAG, "goToSelectedPage");
        browserChromeClient.clearPageDetails();
        pageDisplay.loadUrl(pageUrl);
    }

    public void stopLoading() {
        pageDisplay.stopLoading();
    }


    public void goToPreviousPage() {
        pageDisplay.goBack();
    }

    public String getPreviousPage() {
        String historyUrl;
        WebBackForwardList webBackForwardList = pageDisplay.copyBackForwardList();
        if (webBackForwardList.getCurrentIndex() > 0) {
            historyUrl = webBackForwardList.getItemAtIndex(webBackForwardList.getCurrentIndex() - 1).getUrl();
            return historyUrl;
        } else
            return "";
    }

    public boolean canGoBack() {
        return pageDisplay.canGoBack();
    }

    public void clearHistory() {
        pageDisplay.clearHistory();
    }


    public PageDetails getCurrentPageDetails() {
        PageDetails pageDetails = new PageDetails();
        pageDetails.setTimestamp(TimeUtil.getCurrentTimestamp());
        pageDetails.setTitle(pageDisplay.getTitle());
        pageDetails.setAddress(pageDisplay.getUrl());
        pageDetails.setLogo(pageDisplay.getFavicon());
        return pageDetails;
    }

    private void init() {
        pageDisplay.setWebChromeClient(browserChromeClient);
        pageDisplay.setWebViewClient(new com.developersgroups.faceiraq.view.BrowserClient(onWebViewActionListener));
        pageDisplay.getSettings().setDomStorageEnabled(true);
        pageDisplay.getSettings().setAppCacheMaxSize(1024*1024*8);
        pageDisplay.getSettings().setJavaScriptEnabled(true);
        pageDisplay.getSettings().setAllowFileAccess(true);
        pageDisplay.getSettings().setAppCacheEnabled(true);
        pageDisplay.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        pageDisplay.getSettings().setLoadWithOverviewMode(true);
        pageDisplay.getSettings().setUseWideViewPort(true);
        pageDisplay.getSettings().setBuiltInZoomControls(true);
        pageDisplay.getSettings().setDisplayZoomControls(false);
    }

}
