package com.ready4s.faceiraq.and_faceiraq.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ready4s.faceiraq.and_faceiraq.MainActivity;
import com.ready4s.faceiraq.and_faceiraq.contact.us.ContactUsActivity;
import com.ready4s.faceiraq.and_faceiraq.R;
import com.ready4s.faceiraq.and_faceiraq.contact.us.ContactUsActivity;
import com.ready4s.faceiraq.and_faceiraq.controller.BookmarksActivity;
import com.ready4s.faceiraq.and_faceiraq.controller.HistoryActivity;
import com.ready4s.faceiraq.and_faceiraq.model.ItemListModel;
import com.ready4s.faceiraq.and_faceiraq.model.database.history.HistoryRecord;
import com.ready4s.faceiraq.and_faceiraq.theme.colour.ThemeColourActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 19.04.2017.
 */

public class MainDialogAdapter extends RecyclerView.Adapter<MainDialogAdapter.ViewHolder>{

    private ArrayList<ItemListModel> mItemList = new ArrayList<>();
    private Context mContext;
    private int themeColour;

    public MainDialogAdapter(Context context, int colour) {
        this.mContext = context;
        this.themeColour = colour;
        iniDialogItems();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.dialog_rl)
        RelativeLayout mDialogRl;
        @Bind(R.id.dialog_tv)
        TextView mDialogTv;
        @Bind(R.id.dialog_iv)
        ImageView mDialogIv;
        @Bind(R.id.line)
        View mLine;
        @Bind(R.id.dialog_theme_circle)
        Button mCircle;
        @Bind(R.id.dialog_switch_toggle)
        SwitchCompat mSwitchToggle;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        @OnClick(R.id.dialog_rl)
        public void onClick() {
            switch (getAdapterPosition()) {
                case 0:
                    ((MainActivity) mContext).onOpenedNewPage();
                    break;
                case 1:
                    ((MainActivity) mContext).onSaveBookmarkClick();
                    break;
                case 2:
                    Intent bookmarksIntent = new Intent(mContext, BookmarksActivity.class);
                    mContext.startActivity(bookmarksIntent);
                    break;
                case 3:
                    Intent historyIntent = new Intent(mContext, HistoryActivity.class);
                    mContext.startActivity(historyIntent);
                    break;
                case 4:
                    //notifications
                    break;
                case 5:
                    Intent colourIntent = new Intent(mContext, ThemeColourActivity.class);
                    ((Activity) mContext).startActivityForResult(colourIntent, 1);
                    break;
                case 6:
                    Intent intent = new Intent(mContext, ContactUsActivity.class);
                    mContext.startActivity(intent);
                    break;

            }
        }
    }

    public void iniDialogItems() {
        String[] dialogItems = mContext.getResources().getStringArray(R.array.dialog_item_list);

        mItemList.clear();

        mItemList.add(new ItemListModel(dialogItems[0], R.drawable.new_page));
        mItemList.add(new ItemListModel(dialogItems[1], R.drawable.my_bookmarks));
        mItemList.add(new ItemListModel(dialogItems[2], R.drawable.my_bookmarks));
        mItemList.add(new ItemListModel(dialogItems[3], R.drawable.history));
        mItemList.add(new ItemListModel(dialogItems[4], R.drawable.push_notifications));
        mItemList.add(new ItemListModel(dialogItems[5], R.drawable.theme_colour));
        mItemList.add(new ItemListModel(dialogItems[6], R.drawable.contact_us));


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dialog_list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemListModel item = mItemList.get(position);
        holder.mDialogTv.setText(item.getTitle());
        holder.mDialogIv.setImageResource(item.getImageId());
        if(position == 6)
            holder.mLine.setVisibility(View.GONE);
        if(position == 5) {
            holder.mCircle.setVisibility(View.VISIBLE);
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.circle);
            drawable.setColorFilter(themeColour, PorterDuff.Mode.MULTIPLY);
            holder.mCircle.setBackground(drawable);
//            holder.mCircle.setBackground(mContext.getResources().getDrawable(R.drawable.circle));
//            holder.mCircle.setColorFilter(themeColour, PorterDuff.Mode.MULTIPLY);
        }
        if(position == 4)
            holder.mSwitchToggle.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}

