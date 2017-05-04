package net.faceiraq.and_faceiraq.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import net.faceiraq.and_faceiraq.R;

import net.faceiraq.and_faceiraq.model.PageDetails;
import net.faceiraq.and_faceiraq.model.database.opened_pages.OpenedPageModel;
import net.faceiraq.and_faceiraq.model.utils.ImageUtil;
import net.faceiraq.and_faceiraq.model.utils.TimeUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.graphics.Bitmap.Config.ARGB_8888;

/**
 * Created by Paweł Sałata on 14.04.2017.
 * email: psalata9@gmail.com
 */

public class WebViewFragment extends Fragment {

    private static final String TAG = "WebViewFragment";

    public interface OnWebViewActionListener {
        public void onPageFinished(PageDetails pageDetails);
        public void onUpdatePageIcon(PageDetails pageDetails);
        public void onErrorReceived();
        void onPageStarted(String url);
    }

    OnWebViewActionListener onWebViewActionListener;
    net.faceiraq.and_faceiraq.view.BrowserChromeClient browserChromeClient;

    @Bind(R.id.pageDisplay)
    WebView pageDisplay;

    public WebViewFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onWebViewActionListener = (OnWebViewActionListener) context;
            browserChromeClient = new net.faceiraq.and_faceiraq.view.BrowserChromeClient(onWebViewActionListener);
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

        return view;
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }


    public OpenedPageModel getOpenedPage() {
        OpenedPageModel page= new OpenedPageModel();
        page.setTitle(pageDisplay.getTitle());
        page.setUrl(pageDisplay.getUrl());
        byte[] byteScreenshot = ImageUtil.convertToByteArray(takeScreenshot());
        page.setScreenshot(byteScreenshot);
        return page;
    }

    private Bitmap takeScreenshot() {
        View screenView = getView();
        Bitmap bitmap;
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
        pageDisplay.setWebViewClient(new net.faceiraq.and_faceiraq.view.BrowserClient(onWebViewActionListener));
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
