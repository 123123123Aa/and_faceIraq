package com.ready4s.faceiraq.and_faceiraq.dialog;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class MainDialog extends DialogFragment {

    @Bind(R.id.dialog_recycler_view)
    RecyclerView mDialogRecyclerView;
    @Bind(R.id.dialog_button)
    Button mCancelButton;

    private MainDialogAdapter mDialogAdapter;
    private MainActivity mMainActivity;

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
        mDialogAdapter = new MainDialogAdapter(mMainActivity);
        mDialogRecyclerView.setAdapter(mDialogAdapter);

        return view;

    }

    @OnClick(R.id.dialog_button)
    public void onClick(){
        dismiss();
    }

//    public Dialog DialogBuilder() {
//        Dialog builder = new Dialog(getActivity());
//        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        builder.setContentView(view);
//        return builder;
//    }
}
