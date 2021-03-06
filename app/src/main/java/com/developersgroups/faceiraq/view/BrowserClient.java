package com.developersgroups.faceiraq.view;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.developersgroups.faceiraq.model.PageDetails;


/**
 * Created by Paweł Sałata on 18.04.2017.
 * email: psalata9@gmail.com
 */

public class BrowserClient extends WebViewClient {

    public static final String TAG = "BrowserClient";
    private WebViewFragment.OnWebViewActionListener listener;

    public BrowserClient(WebViewFragment.OnWebViewActionListener listener) {
        this.listener = listener;
    }

    @SuppressWarnings("deprecated")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        listener.onNewPageStartedLoading(view.getUrl());
        view.loadUrl(url);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean shouldOverrideUrlLoading(
            WebView view, WebResourceRequest request) {
        listener.onNewPageStartedLoading(view.getUrl());
        view.loadUrl(request.getUrl().toString());
        return true;
    }

    @SuppressWarnings("deprecated")
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        listener.onErrorReceived();
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        listener.onErrorReceived();
        super.onReceivedError(view, request, error);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        PageDetails pageDetails = new PageDetails();
        pageDetails.setAddress(view.getUrl());
        listener.onPageStarted(pageDetails);
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        listener.hideLoadingSpinner();
        super.onPageFinished(view, url);
    }
}
