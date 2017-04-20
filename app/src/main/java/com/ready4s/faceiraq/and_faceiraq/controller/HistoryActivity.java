package com.ready4s.faceiraq.and_faceiraq.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.ready4s.faceiraq.and_faceiraq.R;
import com.ready4s.faceiraq.and_faceiraq.model.database.history.HistoryDAOImplementation;
import com.ready4s.faceiraq.and_faceiraq.model.database.history.HistoryRecord;
import com.ready4s.faceiraq.and_faceiraq.view.history.DisplayHistoryFragment;

import java.util.List;

import io.realm.Realm;

/**
 * Created by Paweł Sałata on 19.04.2017.
 * email: psalata9@gmail.com
 */

public class HistoryActivity extends FragmentActivity implements DisplayHistoryFragment.OnHistoryActionsListener {

    private static final String TAG = "HistoryActivity";

    private HistoryDAOImplementation historyDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        init();
        Realm.init(this);
        historyDAO = new HistoryDAOImplementation();
        initHistoryData();
    }


    @Override
    public void onDeleteRecordClick(long id) {
        historyDAO.delete(id);
//        initHistoryData();
    }

    private void initHistoryData() {
        DisplayHistoryFragment displayHistoryFragment = (DisplayHistoryFragment) getSupportFragmentManager()
                .findFragmentById(R.id.displayHistoryFragment);
        List<HistoryRecord> historyRecords = historyDAO.getHistory();
        displayHistoryFragment.setHistoryRecords(historyRecords);
    }


    private void init() {
//        initFragment(R.id.navigationBarFragment, new NavigationBarFragment());
        initFragment(R.id.displayHistoryFragment, new DisplayHistoryFragment());
    }

    private void initFragment(int containerId, Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(containerId, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }
}
