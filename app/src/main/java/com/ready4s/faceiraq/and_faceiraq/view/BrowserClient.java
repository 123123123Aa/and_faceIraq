package com.ready4s.faceiraq.and_faceiraq.view;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ready4s.faceiraq.and_faceiraq.model.database.PageDetails;


/**
 * Created by Paweł Sałata on 18.04.2017.
 * email: psalata9@gmail.com
 */

public class BrowserClient extends WebViewClient {

    public static final String TAG = "BrowserClient";

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
}
