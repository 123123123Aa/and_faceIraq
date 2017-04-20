package com.ready4s.faceiraq.and_faceiraq.view;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.ready4s.faceiraq.and_faceiraq.model.database.PageDetails;

/**
 * Created by Paweł Sałata on 18.04.2017.
 * email: psalata9@gmail.com
 */

class BrowserChromeClient extends WebChromeClient {

    public static final String TAG = "BrowserChromeClient";

    private WebViewFragment.OnWebViewActionListener onWebViewActionListener;
    private PageDetails pageDetails;
    private String pageUrl;

    public BrowserChromeClient(WebViewFragment.OnWebViewActionListener onWebViewActionListener) {
        this.onWebViewActionListener = onWebViewActionListener;
        this.pageDetails = new PageDetails();
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (newProgress == 100) {
            Log.d(TAG, "onProgressChanged: 100");
            pageDetails.setAddress(view.getUrl());
            pageUrl = view.getUrl();
            onWebViewActionListener.onPageFinished(pageDetails, pageUrl);
        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        pageDetails.setTitle(title);
    }

    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        super.onReceivedIcon(view, icon);
        Log.d(TAG, "onReceivedIcon: " + icon);
        pageDetails.setLogo(icon);
        onWebViewActionListener.onPageFinished(pageDetails, pageUrl);
    }
}
