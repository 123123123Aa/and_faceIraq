package com.ready4s.faceiraq.and_faceiraq.dialog;

import android.app.Activity;
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
import com.ready4s.faceiraq.and_faceiraq.R;
import com.ready4s.faceiraq.and_faceiraq.contact.us.ContactUsActivity;
import com.ready4s.faceiraq.and_faceiraq.controller.BookmarksActivity;
import com.ready4s.faceiraq.and_faceiraq.controller.HistoryActivity;
import com.ready4s.faceiraq.and_faceiraq.model.ItemListModel;
import com.ready4s.faceiraq.and_faceiraq.theme.colour.ThemeColourActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ready4s.faceiraq.and_faceiraq.controller.BookmarksActivity.BOOKMARKS_REQUEST_CODE;
import static com.ready4s.faceiraq.and_faceiraq.controller.HistoryActivity.HISTORY_REQUEST_CODE;
import static com.ready4s.faceiraq.and_faceiraq.theme.colour.ThemeColourActivity.THEME_COLOUR_REQUEST_CODE;

/**
 * Created by user on 19.04.2017.
 */

public class MainDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<ItemListModel> mItemList = new ArrayList<>();
    private MainDialogFragment.OnMainDialogActionsListener listener;
    private Activity mActivity;
    private int themeColour;
    private IMainDialogFragment mView;

    public MainDialogAdapter(MainDialogFragment.OnMainDialogActionsListener context, int colour) {
        this.listener = context;
        this.mActivity = (Activity) context;
        this.themeColour = colour;
        iniDialogItems();
    }


    public void onViewAttached(IMainDialogFragment view) {
        mView = view;
    }

    public void onViewDetached() {
        mView = null;
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
                    listener.onOpenedNewPage();
                    mView.onPageSelected();
                    break;
                case 1:
                    if (!mItemList.get(getAdapterPosition()).isSelected()
                            && mActivity instanceof MainActivity) {
                        ((MainActivity) mActivity).onSaveBookmarkClick();
//                        mActivityView.onSaveBookmarkClick();
                        mItemList.get(getAdapterPosition()).setSelected(true);
                        notifyItemChanged(getAdapterPosition());
                    }
                    break;
                case 2:
                    Intent bookmarksIntent = new Intent(mActivity, BookmarksActivity.class);
                    mActivity.startActivityForResult(bookmarksIntent, BOOKMARKS_REQUEST_CODE);
                    mView.onPageSelected();
                    break;
                case 3:
                    Intent historyIntent = new Intent(mActivity, HistoryActivity.class);
                    mActivity.startActivityForResult(historyIntent, HISTORY_REQUEST_CODE);
                    mView.onPageSelected();
                    break;
                case 4:
                    //notifications
                    break;
                case 5:
                    Intent colourIntent = new Intent(mActivity, ThemeColourActivity.class);
                    mActivity.startActivityForResult(colourIntent, THEME_COLOUR_REQUEST_CODE);
                    break;
                case 6:
                    Intent intent = new Intent(mActivity, ContactUsActivity.class);
                    mActivity.startActivity(intent);
                    break;

            }
        }
        public void setInvisible() {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)itemView.getLayoutParams();
            params.height = 0;
            itemView.setVisibility(View.GONE);
        }
    }

    public class ButtonViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.dialog_button) Button mCancelButton;
//        @Bind(R.id.button_background) LinearLayout mButtoBackground;

        ButtonViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.dialog_button)
        public void onClick() {
            mView.onPageSelected();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 7)
            return 1;
        else
            return 0;
    }

    private void setVisibility(ViewHolder holder) {
        holder.setInvisible();
    }

    public void iniDialogItems() {
        String[] dialogItems = mActivity.getResources().getStringArray(R.array.dialog_item_list);

        mItemList.clear();

        mItemList.add(new ItemListModel(dialogItems[0], R.drawable.new_page, false));
        mItemList.add(new ItemListModel(dialogItems[1], R.drawable.my_bookmarks, false));
        mItemList.add(new ItemListModel(dialogItems[2], R.drawable.my_bookmarks, false));
        mItemList.add(new ItemListModel(dialogItems[3], R.drawable.history, false));
        mItemList.add(new ItemListModel(dialogItems[4], R.drawable.push_notifications, false));
        mItemList.add(new ItemListModel(dialogItems[5], R.drawable.theme_colour, false));
        mItemList.add(new ItemListModel(dialogItems[6], R.drawable.contact_us, false));


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dialog_list_row, parent, false);
                return new ViewHolder(view);
            case 1:
                View buttonView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dialog_button, parent, false);
                return new ButtonViewHolder(buttonView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case 0:
                ViewHolder holder = (ViewHolder) viewHolder;
                ItemListModel item = mItemList.get(position);
                holder.mDialogTv.setText(item.getTitle());
                holder.mDialogIv.setImageResource(item.getImageId());
                if (position == 1) {
                    if (item.isSelected()) {
                        holder.mDialogIv.setImageResource(R.drawable.bookmark_checked);
                    }
                }
                if (position == 6)
                    holder.mLine.setVisibility(View.GONE);
                if (position == 5) {
                    holder.mCircle.setVisibility(View.VISIBLE);
                    Drawable drawable = mActivity.getResources().getDrawable(R.drawable.circle);
                    drawable.setColorFilter(themeColour, PorterDuff.Mode.MULTIPLY);
                    holder.mCircle.setBackground(drawable);
//            holder.mCircle.setBackground(mActivity.getResources().getDrawable(R.drawable.circle));
//            holder.mCircle.setColorFilter(themeColour, PorterDuff.Mode.MULTIPLY);
                }
                if (position == 4)
                    holder.mSwitchToggle.setVisibility(View.VISIBLE);
                if (position == 1 && !(mActivity instanceof MainActivity)) {
                    setVisibility(holder);
                }
                break;
            case 1:
                ButtonViewHolder buttonHolder = (ButtonViewHolder) viewHolder;
                Drawable drawable = mActivity.getResources().getDrawable(R.drawable.btn_round_grey);
                drawable.setColorFilter(themeColour, PorterDuff.Mode.MULTIPLY);
                buttonHolder.mCancelButton.setBackground(drawable);
//                buttonHolder.mButtoBackground.setBackgroundColor(mActivity.getResources().getColor(android.R.color.transparent));
        }
    }

    @Override
    public int getItemCount() {
        return mItemList.size() + 1;
    }
}

