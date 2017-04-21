package com.ready4s.faceiraq.and_faceiraq.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.ready4s.faceiraq.and_faceiraq.R;
import com.ready4s.faceiraq.and_faceiraq.model.database.bookmarks.BookmarkRecord;
import com.ready4s.faceiraq.and_faceiraq.model.database.bookmarks.BookmarksDAOImplementation;
import com.ready4s.faceiraq.and_faceiraq.model.database.history.HistoryRecord;
import com.ready4s.faceiraq.and_faceiraq.view.bookmarks.BookmarksFragment;
import com.ready4s.faceiraq.and_faceiraq.view.history.HistoryFragment;

import java.util.List;

import io.realm.Realm;

/**
 * Created by Paweł Sałata on 21.04.2017.
 * email: psalata9@gmail.com
 */

public class BookmarksActivity extends FragmentActivity
        implements BookmarksFragment.OnBookmarksActionsListener {

    private static final String TAG = "BookmarksActivity";

    private BookmarksDAOImplementation bookmarksDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        init();
        Realm.init(this);
        bookmarksDAO = new BookmarksDAOImplementation();
        initBookmarksData();
    }


    @Override
    public void onDeleteRecordClick(long id) {
        bookmarksDAO.delete(id);
//        initHistoryData();
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
