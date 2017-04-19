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
import android.widget.RelativeLayout;

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
    @Bind(R.id.address_focus_section)
    RelativeLayout mFocusSection;
    @Bind(R.id.address_section)
    RelativeLayout mAddressSection;
    @Bind(R.id.address_focus_field)
    EditText mFocusEt;


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
        onTextClick();
        return view;
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    private void onTextClick() {
        addressField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFocusSection.setVisibility(View.VISIBLE);
                mAddressSection.setVisibility(View.GONE);
                mFocusEt.setText(addressField.getText().toString());
                mFocusEt.setSelection(addressField.length());
                mFocusEt.requestFocus();
            }
        });
    }


    @OnEditorAction(R.id.address_focus_field)
    boolean onEditorAction(int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            String pageUrl = mFocusEt.getText().toString();
            onNavigationBarActionListener.onPageSelected(pageUrl);
            mFocusEt.clearFocus();
            addressField.setText(mFocusEt.getText().toString());
            mFocusSection.setVisibility(View.GONE);
            mAddressSection.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }







    @OnClick({R.id.homeButton, R.id.previousPageButton, R.id.cancel_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.homeButton:
                onNavigationBarActionListener.onHomeButtonPressed();
                break;
            case R.id.previousPageButton:
                onNavigationBarActionListener.onPreviousPageButtonPressed();
                break;
            case R.id.cancel_button:
                mFocusSection.setVisibility(View.GONE);
                mAddressSection.setVisibility(View.VISIBLE);

        }
    }

    public void setAddressField(String pageUrl) {
        addressField.setText(pageUrl);
    }
}
