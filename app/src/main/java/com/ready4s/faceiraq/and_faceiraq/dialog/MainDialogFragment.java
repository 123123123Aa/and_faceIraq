package com.ready4s.faceiraq.and_faceiraq.dialog;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.ready4s.faceiraq.and_faceiraq.MainActivity;
import com.ready4s.faceiraq.and_faceiraq.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 19.04.2017.
 */

public class MainDialogFragment extends DialogFragment {

    @Bind(R.id.dialog_recycler_view)
    RecyclerView mDialogRecyclerView;
    @Bind(R.id.dialog_button)
    Button mCancelButton;

    private MainDialogAdapter mDialogAdapter;
    private MainActivity mMainActivity;
    private int themeColour;

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();

        window.setLayout(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_main, container, false);
        ButterKnife.bind(this, view);


        mMainActivity = (MainActivity) getActivity();
        mDialogRecyclerView.setLayoutManager(new LinearLayoutManager(mMainActivity, LinearLayoutManager.VERTICAL, false));
        changeButtonColor();
        mDialogAdapter = new MainDialogAdapter(mMainActivity, themeColour);
        mDialogRecyclerView.setAdapter(mDialogAdapter);



        return view;

    }

    private void changeButtonColor() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = mMainActivity.getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        themeColour = typedValue.data;
        Drawable drawable = mMainActivity.getResources().getDrawable(R.drawable.btn_round_grey);
        drawable.setColorFilter(themeColour, PorterDuff.Mode.MULTIPLY);
        mCancelButton.setBackground(drawable);
    }

    @OnClick(R.id.dialog_button)
    public void onClick(){
        dismiss();
    }

}
