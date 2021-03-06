package com.developersgroups.faceiraq.view.bookmarks;

import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.developersgroups.faceiraq.R;
import com.developersgroups.faceiraq.controller.BookmarksActivity;
import com.developersgroups.faceiraq.model.SharedPreferencesHelper;
import com.developersgroups.faceiraq.model.database.bookmarks.BookmarkRecord;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Paweł Sałata on 21.04.2017.
 * email: psalata9@gmail.com
 */

public class BookmarksRecViewAdapter  extends RecyclerView.Adapter<BookmarksRecViewAdapter.BookmarkRecordHolder> {

    private static final String TAG = "BookmarksRecViewAdapter";
    private List<BookmarkRecord> bookmarksRecords;
    private BookmarksActivity mContext;

    private BookmarksFragment.OnBookmarksActionsListener onBookmarksActionsListener;

    public class BookmarkRecordHolder extends RecyclerView.ViewHolder {

        long id;
//        @Bind(R.id.dateLabel) TextView dateLabel;
        @Bind(R.id.pageTitle) TextView pageTitle;
        @Bind(R.id.pageUrl) TextView pageUrl;
        @Bind(R.id.pageIcon) ImageView pageIcon;
        @Bind(R.id.deleteButton) LinearLayout deleteButton;
        @Bind(R.id.history_section) LinearLayout historySection;

        public BookmarkRecordHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onDeleteHistory record click id: " + id);
                    onBookmarksActionsListener.onDeleteRecordClick(id);
                    removeAt(getAdapterPosition());
                }
            });
        }

        public void removeAt(int position) {
            bookmarksRecords.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, bookmarksRecords.size());
        }

        @OnClick(R.id.history_section)
        public void onClick() {
            int pos = getAdapterPosition();
            SharedPreferencesHelper.setCardNumber(
                    mContext,
                    bookmarksRecords.get(pos).getId());
            SharedPreferencesHelper.setCardUrl(
                    mContext,
                    bookmarksRecords.get(pos).getAddress());
            mContext.setResult(RESULT_OK);
            mContext.finish();
        }
    }

    public BookmarksRecViewAdapter(BookmarksFragment.OnBookmarksActionsListener onBookmarksActionsListener, BookmarksActivity context) {
        this.bookmarksRecords = new ArrayList<>();
        this.onBookmarksActionsListener = onBookmarksActionsListener;
        this.mContext = context;

    }

    public List<BookmarkRecord> getBookmarksRecords() {
        return bookmarksRecords;
    }

    public void setBookmarksRecords(List<BookmarkRecord> bookmarksRecords) {
        this.bookmarksRecords = bookmarksRecords;
    }

    @Override
    public BookmarkRecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_record_view, parent, false);
        return new BookmarkRecordHolder(v);
    }

    @Override
    public void onBindViewHolder(BookmarkRecordHolder holder, int position) {
        holder.id = bookmarksRecords.get(position).getId();
        holder.pageTitle.setText(bookmarksRecords.get(position).getTitle());
        holder.pageUrl.setText(bookmarksRecords.get(position).getAddress());

        String base64Icon = bookmarksRecords.get(position).getBase64Logo();
        byte[] iconByteArray = null;
        if (base64Icon != null) {
            iconByteArray = Base64.decode(base64Icon, Base64.DEFAULT);
        }

        Glide.with(mContext)
                .load(iconByteArray)
                .asBitmap()
                .fitCenter()
                .error(R.drawable.blank_favicon)
                .into(holder.pageIcon);
    }

    @Override
    public int getItemCount() {
        return bookmarksRecords.size();
    }
}
