package com.ready4s.faceiraq.and_faceiraq.theme.colour;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.TextView;

import com.ready4s.faceiraq.and_faceiraq.R;
import com.ready4s.faceiraq.and_faceiraq.model.utils.ThemeChangeUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 20.04.2017.
 */

public class ThemeColourActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.theme_colour_rv)
    RecyclerView mColourRv;

    private ThemeColourAdapter mThemeColourAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChangeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.actiivity_theme_colour);
        ButterKnife.bind(this);

        mColourRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mThemeColourAdapter = new ThemeColourAdapter(this);
        mColourRv.setAdapter(mThemeColourAdapter);

        setupToolbar();

    }

    private void setupToolbar() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int themeColour = typedValue.data;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarTitle.setText(R.string.toolbar_theme_colour_title);
        mToolbar.setBackgroundColor(themeColour);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
