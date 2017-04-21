package com.ready4s.faceiraq.and_faceiraq.theme.colour;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ready4s.faceiraq.and_faceiraq.R;
import com.ready4s.faceiraq.and_faceiraq.model.ColourListModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by user on 20.04.2017.
 */

public class ThemeColourAdapter extends RecyclerView.Adapter<ThemeColourAdapter.ViewHolder>{

    private ArrayList<ColourListModel> mItemList = new ArrayList<>();
    private Context mContext;

    public ThemeColourAdapter(Context context) {
        this.mContext = context;

        iniColourItems();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.theme_colour_rl)
        RelativeLayout mThemeRl;
        @Bind(R.id.theme_colour_title)
        TextView mColourTitle;
        @Bind(R.id.theme_colour_colour)
        ImageView mColour;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.theme_colour_rl)
        public void onClick(View view) {
            int pos = getAdapterPosition();
            Intent intent = new Intent();
            intent.putExtra("Colour", mItemList.get(pos).getTitle());
            ((Activity) mContext).setResult(RESULT_OK, intent);
            ((Activity) mContext).finish();
        }
    }

    public void iniColourItems() {
        String[] colourItems = mContext.getResources().getStringArray(R.array.colors);

        mItemList.clear();

        mItemList.add(new ColourListModel(colourItems[0], R.color.beige, false));
        mItemList.add(new ColourListModel(colourItems[1], R.color.red, false));
        mItemList.add(new ColourListModel(colourItems[2], R.color.pink, false));
        mItemList.add(new ColourListModel(colourItems[3], R.color.purple, false));
        mItemList.add(new ColourListModel(colourItems[4], R.color.dark_blue, false));
        mItemList.add(new ColourListModel(colourItems[5], R.color.blue, false));
        mItemList.add(new ColourListModel(colourItems[6], R.color.turquoise, false));
        mItemList.add(new ColourListModel(colourItems[7], R.color.dark_green, false));
        mItemList.add(new ColourListModel(colourItems[8], R.color.green, false));
        mItemList.add(new ColourListModel(colourItems[9], R.color.yellow, false));
        mItemList.add(new ColourListModel(colourItems[10], R.color.orange, false));
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_theme_colour_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ColourListModel item = mItemList.get(position);
        holder.mColourTitle.setText(item.getTitle());
        holder.mColour.setBackground(mContext.getResources().getDrawable(R.drawable.oval));
        holder.mColour.setColorFilter(ContextCompat.getColor(mContext, item.getColourId()));
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}
