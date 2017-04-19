package com.ready4s.faceiraq.and_faceiraq.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ready4s.faceiraq.and_faceiraq.R;
import com.ready4s.faceiraq.and_faceiraq.model.database.PageDetails;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Paweł Sałata on 14.04.2017.
 * email: psalata9@gmail.com
 */

public class WebViewFragment extends Fragment {

    public interface OnWebViewActionListener {
        public void onPageFinished(PageDetails pageDetails);
    }

    OnWebViewActionListener onWebViewActionListener;

    @Bind(R.id.pageDisplay)
    WebView pageDisplay;

    public WebViewFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onWebViewActionListener = (OnWebViewActionListener) context;
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

    public void goToSelectedPage(String pageUrl) {
        pageDisplay.loadUrl(pageUrl);
    }

    private void init() {
        pageDisplay.setWebChromeClient(new BrowserChromeClient(onWebViewActionListener));
        pageDisplay.setWebViewClient(new BrowserClient());
        pageDisplay.getSettings().setLoadWithOverviewMode(true);
        pageDisplay.getSettings().setUseWideViewPort(true);
        pageDisplay.getSettings().setBuiltInZoomControls(true);
        pageDisplay.getSettings().setDisplayZoomControls(false);
    }
}
