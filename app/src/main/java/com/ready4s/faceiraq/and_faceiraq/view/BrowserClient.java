package com.ready4s.faceiraq.and_faceiraq.view;

import android.annotation.TargetApi;
import android.os.Build;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;


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
        view.loadUrl(url);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean shouldOverrideUrlLoading(
            WebView view, WebResourceRequest request) {
        view.loadUrl(request.getUrl().toString());
        return true;
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        listener.onErrorReceived();
        super.onReceivedError(view, request, error);
    }
}
