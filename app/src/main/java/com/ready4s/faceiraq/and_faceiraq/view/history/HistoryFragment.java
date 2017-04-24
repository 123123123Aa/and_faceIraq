package com.ready4s.faceiraq.and_faceiraq.view.history;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ready4s.faceiraq.and_faceiraq.R;
import com.ready4s.faceiraq.and_faceiraq.controller.HistoryActivity;
import com.ready4s.faceiraq.and_faceiraq.model.database.history.HistoryRecord;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

/**
 * Created by Paweł Sałata on 19.04.2017.
 * email: psalata9@gmail.com
 */

public class HistoryFragment extends Fragment {

    public static final String TAG = "HistoryFragment";

    public interface OnHistoryActionsListener {
        public void onDeleteRecordClick(long timestamp);
    }

    OnHistoryActionsListener onHistoryActionsListener;

    @Bind(R.id.historyRecyclerView)
    RecyclerView historyRecyclerView;

    @Bind(R.id.searchBox)
    EditText searchBox;

    private HistoryRecViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private HistoryActivity mHistoryActivity;


    public HistoryFragment() {
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: ");
        super.onAttach(context);
        try {
            onHistoryActionsListener = (OnHistoryActionsListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnHistoryActionsListener");
        }
        layoutManager = new LinearLayoutManager(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_history_fragment, container, false);

        ButterKnife.bind(this, view);
        init();

        mHistoryActivity = (HistoryActivity) getActivity();

        return view;
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @OnTextChanged(value = R.id.searchBox, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChanged(CharSequence text) {
        adapter.getFilter().filter(text);
    }

    public void setHistoryRecords(List<HistoryRecord> historyRecords) {
        Log.d(TAG, "setHistoryRecords: SET");
        adapter = new HistoryRecViewAdapter(onHistoryActionsListener, mHistoryActivity);
        adapter.setHistoryRecords(historyRecords);
        historyRecyclerView.setAdapter(adapter);
        historyRecyclerView.scrollToPosition(0);
    }


    private void init() {
        historyRecyclerView.setLayoutManager(layoutManager);
    }
}
