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
import com.ready4s.faceiraq.and_faceiraq.model.database.bookmarks.BookmarkRecord;
import com.ready4s.faceiraq.and_faceiraq.model.database.bookmarks.BookmarksDAOImplementation;
import com.ready4s.faceiraq.and_faceiraq.model.utils.ThemeChangeUtil;
import com.ready4s.faceiraq.and_faceiraq.view.bookmarks.BookmarksFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by Paweł Sałata on 21.04.2017.
 * email: psalata9@gmail.com
 */

public class BookmarksActivity extends AppCompatActivity
        implements BookmarksFragment.OnBookmarksActionsListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;

    private static final String TAG = "BookmarksActivity";
    public static final int BOOKMARKS_REQUEST_CODE = 12;

    private BookmarksDAOImplementation bookmarksDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChangeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_bookmarks);
        ButterKnife.bind(this);
//        init();
        setupToolbar();
        Realm.init(this);
        bookmarksDAO = new BookmarksDAOImplementation();
        initBookmarksData();
    }


    @Override
    public void onDeleteRecordClick(long id) {
        bookmarksDAO.delete(id);
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
        mToolbarTitle.setText(R.string.toolbar_my_bookmarks_title);
        mToolbar.setBackgroundColor(themeColour);
    }

    private void initBookmarksData() {
        BookmarksFragment bookmarksFragment = (BookmarksFragment) getSupportFragmentManager()
                .findFragmentById(R.id.bookmarksFragment);
        List<BookmarkRecord> bookmarkRecords = bookmarksDAO.getBookmarks();
        bookmarksFragment.setBookmarksRecords(bookmarkRecords);
    }


    private void init() {
//        initFragment(R.id.navigationBarFragment, new NavigationBarFragment());
        initFragment(R.id.bookmarksFragment, new BookmarksFragment());
    }

    private void initFragment(int containerId, Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(containerId, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }
}
