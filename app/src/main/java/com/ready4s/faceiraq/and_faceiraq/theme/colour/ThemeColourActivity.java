package com.ready4s.faceiraq.and_faceiraq.theme.colour;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.ready4s.faceiraq.and_faceiraq.R;

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
        setContentView(R.layout.actiivity_theme_colour);
        ButterKnife.bind(this);

        mColourRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mThemeColourAdapter = new ThemeColourAdapter(this);
        mColourRv.setAdapter(mThemeColourAdapter);

        setupToolbar();

    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarTitle.setText(R.string.toolbar_theme_colour_title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra("Colour", mThemeColourAdapter.getSelectedColour());
                setResult(RESULT_OK, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
