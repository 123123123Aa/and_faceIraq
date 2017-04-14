package com.ready4s.faceiraq.and_faceiraq.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.ready4s.faceiraq.and_faceiraq.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

/**
 * Created by Paweł Sałata on 14.04.2017.
 * email: psalata9@gmail.com
 */

public class NavigationBarFragment extends Fragment {

    public interface OnNavigationBarActionListener {
        public void onPageSelected(String pageUrl);
        public void onHomeButtonPressed();
        public void onPreviousPageButtonPressed();
    }

    OnNavigationBarActionListener onNavigationBarActionListener;

    @Bind(R.id.addressField)
    EditText addressField;

    public NavigationBarFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onNavigationBarActionListener = (OnNavigationBarActionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnNavigationBarActionListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.browser_navigation_bar, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @OnEditorAction(R.id.addressField)
    boolean onEditorAction(int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            String pageUrl = addressField.getText().toString();
            onNavigationBarActionListener.onPageSelected(pageUrl);
            return true;
        }
        return false;
    }

    @OnClick(R.id.homeButton)
    void onHomeButtonPressed() {
        onNavigationBarActionListener.onHomeButtonPressed();
    }

    @OnClick(R.id.previousPageButton)
    void onPreviousPageButtonPressed() {
        onNavigationBarActionListener.onPreviousPageButtonPressed();
    }

    public void setAddressField(String pageUrl) {
        addressField.setText(pageUrl);
    }
}
