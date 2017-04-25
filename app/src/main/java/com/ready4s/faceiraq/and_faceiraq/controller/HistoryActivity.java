package com.ready4s.faceiraq.and_faceiraq.controller;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.TextView;

import com.ready4s.faceiraq.and_faceiraq.R;
import com.ready4s.faceiraq.and_faceiraq.model.database.history.HistoryDAOImplementation;
import com.ready4s.faceiraq.and_faceiraq.model.database.history.HistoryRecord;
import com.ready4s.faceiraq.and_faceiraq.model.utils.ThemeChangeUtil;
import com.ready4s.faceiraq.and_faceiraq.view.history.HistoryFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by Paweł Sałata on 19.04.2017.
 * email: psalata9@gmail.com
 */

public class HistoryActivity extends AppCompatActivity implements HistoryFragment.OnHistoryActionsListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;

    private static final String TAG = "HistoryActivity";

    private HistoryDAOImplementation historyDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChangeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
//        init();
        Realm.init(this);
        historyDAO = new HistoryDAOImplementation();
        initHistoryData();
        setupToolbar();
    }


    @Override
    public void onDeleteRecordClick(long id) {
        historyDAO.delete(id);
//        initHistoryData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void setupToolbar() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int themeColour = typedValue.data;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarTitle.setText(R.string.toolbr_history_title);
        mToolbar.setBackgroundColor(themeColour);
    }


    private void initHistoryData() {
        HistoryFragment historyFragment = (HistoryFragment) getSupportFragmentManager()
                .findFragmentById(R.id.historyFragment);
        List<HistoryRecord> historyRecords = historyDAO.getHistory();
        historyFragment.setHistoryRecords(historyRecords);
    }


    private void init() {
//        initFragment(R.id.navigationBarFragment, new NavigationBarFragment());
        initFragment(R.id.historyFragment, new HistoryFragment());
    }

    private void initFragment(int containerId, Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(containerId, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }
}
