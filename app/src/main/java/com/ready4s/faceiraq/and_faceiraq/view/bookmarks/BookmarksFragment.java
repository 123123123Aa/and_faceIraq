package com.ready4s.faceiraq.and_faceiraq.view.bookmarks;

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

import com.ready4s.faceiraq.and_faceiraq.R;
import com.ready4s.faceiraq.and_faceiraq.controller.BookmarksActivity;
import com.ready4s.faceiraq.and_faceiraq.model.database.bookmarks.BookmarkRecord;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Paweł Sałata on 21.04.2017.
 * email: psalata9@gmail.com
 */

public class BookmarksFragment extends Fragment {

    private static final String TAG = "BookmarksFragment";

    public interface OnBookmarksActionsListener {
        public void onDeleteRecordClick(long id);
    }

    OnBookmarksActionsListener onBookmarksActionsListener;

    @Bind(R.id.bookmarksRecView)
    RecyclerView recyclerView;

    private BookmarksRecViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private BookmarksActivity mBookmarksActivity;


    public BookmarksFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onBookmarksActionsListener = (OnBookmarksActionsListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnBookmarksActionsListener");
        }
        layoutManager = new LinearLayoutManager(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_bookmarks_fragment, container, false);

        ButterKnife.bind(this, view);
        init();

        mBookmarksActivity = (BookmarksActivity) getActivity();

        return view;
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    public void setBookmarksRecords(List<BookmarkRecord> bookmarksRecords) {
        Log.d(TAG, "setBookmarksRecords: SET");
        adapter = new BookmarksRecViewAdapter(onBookmarksActionsListener,  mBookmarksActivity);
        adapter.setBookmarksRecords(bookmarksRecords);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(0);
    }


    private void init() {
        recyclerView.setLayoutManager(layoutManager);
    }
}
