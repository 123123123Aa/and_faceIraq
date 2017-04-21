package com.ready4s.faceiraq.and_faceiraq.view.bookmarks;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ready4s.faceiraq.and_faceiraq.R;
import com.ready4s.faceiraq.and_faceiraq.model.database.bookmarks.BookmarkRecord;
import com.ready4s.faceiraq.and_faceiraq.model.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paweł Sałata on 21.04.2017.
 * email: psalata9@gmail.com
 */

public class BookmarksRecViewAdapter  extends RecyclerView.Adapter<BookmarksRecViewAdapter.BookmarkRecordHolder> {

    private static final String TAG = "BookmarksRecViewAdapter";
    private List<BookmarkRecord> bookmarksRecords;

    private BookmarksFragment.OnBookmarksActionsListener onBookmarksActionsListener;

    public class BookmarkRecordHolder extends RecyclerView.ViewHolder {

        long id;
        TextView pageTitle;
        TextView pageUrl;
        ImageView pageIcon;
        LinearLayout deleteButton;

        public BookmarkRecordHolder(View itemView) {
            super(itemView);
            pageTitle = (TextView) itemView.findViewById(R.id.pageTitle);
            pageUrl = (TextView) itemView.findViewById(R.id.pageUrl);
            pageIcon = (ImageView) itemView.findViewById(R.id.pageIcon);
            deleteButton = (LinearLayout) itemView.findViewById(R.id.deleteButton);
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
    }

    public BookmarksRecViewAdapter(BookmarksFragment.OnBookmarksActionsListener onBookmarksActionsListener) {
        this.bookmarksRecords = new ArrayList<>();
        this.onBookmarksActionsListener = onBookmarksActionsListener;
    }

    public List<BookmarkRecord> getBookmarksRecords() {
        return bookmarksRecords;
    }

    public void setBookmarksRecords(List<BookmarkRecord> bookmarksRecords) {
        this.bookmarksRecords = bookmarksRecords;
    }

    @Override
    public BookmarkRecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_record_view, parent, false);
        return new BookmarkRecordHolder(v);
    }

    @Override
    public void onBindViewHolder(BookmarkRecordHolder holder, int position) {
        holder.id = bookmarksRecords.get(position).getId();
        holder.pageTitle.setText(bookmarksRecords.get(position).getTitle());
        holder.pageUrl.setText(bookmarksRecords.get(position).getAddress());

        if (bookmarksRecords.get(position).getBase64Logo() != null) {
            Bitmap iconBitmap = ImageUtil.decodeBase64(bookmarksRecords.get(position).getBase64Logo());
            holder.pageIcon.setImageBitmap(iconBitmap);
        }
    }

    @Override
    public int getItemCount() {
        return bookmarksRecords.size();
    }
}
