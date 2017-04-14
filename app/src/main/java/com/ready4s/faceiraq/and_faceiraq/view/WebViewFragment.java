package com.ready4s.faceiraq.and_faceiraq.view;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ready4s.faceiraq.and_faceiraq.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Paweł Sałata on 14.04.2017.
 * email: psalata9@gmail.com
 */

public class WebViewFragment extends Fragment {

    @Bind(R.id.pageDisplay)
    WebView pageDisplay;

    public WebViewFragment() {
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

    public void goToSelectedPage(String pageUrl) {
        pageDisplay.loadUrl(pageUrl);
    }

    private void init() {
        pageDisplay.setWebViewClient(new WebViewClient());
        pageDisplay.getSettings().setLoadWithOverviewMode(true);
        pageDisplay.getSettings().setUseWideViewPort(true);
        pageDisplay.getSettings().setBuiltInZoomControls(true);
        pageDisplay.getSettings().setDisplayZoomControls(false);
    }
}
