package com.developersgroups.faceiraq.view;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.developersgroups.faceiraq.model.PageDetails;
import com.developersgroups.faceiraq.utils.TimeUtil;

/**
 * Created by Paweł Sałata on 18.04.2017.
 * email: psalata9@gmail.com
 */

public class BrowserChromeClient extends WebChromeClient {

    public static final String TAG = "BrowserChromeClient";

    private com.developersgroups.faceiraq.view.WebViewFragment.OnWebViewActionListener onWebViewActionListener;
    private PageDetails pageDetails;
    private String pageUrl;

    public BrowserChromeClient(com.developersgroups.faceiraq.view.WebViewFragment.OnWebViewActionListener onWebViewActionListener) {
        this.onWebViewActionListener = onWebViewActionListener;
        this.pageDetails = new PageDetails();
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (newProgress == 100) {
            Log.d(TAG, "onProgressChanged: 100");
            pageUrl = view.getUrl();
        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        pageDetails.setTitle(title);
        pageDetails.setAddress(view.getUrl());
        pageDetails.setTimestamp(TimeUtil.getCurrentTimestamp());
        onWebViewActionListener.onPageFinished(pageDetails);
    }

    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        super.onReceivedIcon(view, icon);
        Log.d(TAG, "onReceivedIcon: " + icon);
        pageDetails.setTitle(view.getTitle());
        pageDetails.setAddress(view.getUrl());
        pageDetails.setLogo(icon);
        pageDetails.setTimestamp(TimeUtil.getCurrentTimestamp());
        onWebViewActionListener.onUpdatePageIcon(pageDetails);
    }

    public void clearPageDetails() {
        Log.d(TAG, "clearPageDetails");
        pageDetails = new PageDetails();
    }
}
