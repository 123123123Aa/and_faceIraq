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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ready4s.faceiraq.and_faceiraq.MainActivity;
import com.ready4s.faceiraq.and_faceiraq.R;
import com.ready4s.faceiraq.and_faceiraq.dialog.MainDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

import static android.view.View.GONE;

/**
 * Created by Paweł Sałata on 14.04.2017.
 * email: psalata9@gmail.com
 */

public class NavigationBarFragment extends Fragment {

    public interface OnNavigationBarActionListener {
        public void onPageSelected(String pageUrl);
        public void onHomeButtonPressed();
        public void onPreviousPageButtonPressed();
        void onCardsButtonPressed();
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
    @Bind(R.id.menuDotsButton)
    ImageView mDotsMenu;

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
                mAddressSection.setVisibility(GONE);
                mFocusEt.setText(addressField.getText().toString());
                mFocusEt.setSelection(addressField.length());
                mFocusEt.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mFocusEt, InputMethodManager.SHOW_IMPLICIT);
            }
        });

    }



    @OnEditorAction(R.id.address_focus_field)
    boolean onEditorAction(int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            String pageUrl = mFocusEt.getText().toString();
            onNavigationBarActionListener.onPageSelected(pageUrl);
            addressField.setText(mFocusEt.getText().toString());
            mFocusSection.setVisibility(GONE);
            mAddressSection.setVisibility(View.VISIBLE);
            hideSoftKeyboard();
            return true;
        } else if (event.getAction() == KeyEvent.ACTION_UP) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mFocusEt.getWindowToken(), 0);
            mFocusSection.setVisibility(GONE);
            mAddressSection.setVisibility(View.VISIBLE);
            mFocusEt.clearFocus();
            return true;
        }
        return false;
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mFocusEt.getWindowToken(), 0);
        mFocusEt.clearFocus();
    }






    @OnClick({R.id.homeButton, R.id.previousPageButton, R.id.cancel_button, R.id.menuDotsButton})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.homeButton:
                onNavigationBarActionListener.onCardsButtonPressed();
//                onNavigationBarActionListener.onHomeButtonPressed();
                break;
            case R.id.previousPageButton:
                onNavigationBarActionListener.onPreviousPageButtonPressed();
                break;
            case R.id.cardsCountButton:
                onNavigationBarActionListener.onCardsButtonPressed();
                break;
            case R.id.cancel_button:
                mFocusSection.setVisibility(GONE);
                mAddressSection.setVisibility(View.VISIBLE);
                hideSoftKeyboard();
                break;
            case R.id.menuDotsButton:
                MainDialog dialogFragment = new MainDialog();
                dialogFragment.show(getActivity().getSupportFragmentManager(),"simple dialog");




        }
    }

    public void setAddressField(String pageUrl) {
        addressField.setText(pageUrl);
    }
}
