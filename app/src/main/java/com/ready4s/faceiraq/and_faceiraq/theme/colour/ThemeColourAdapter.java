package com.ready4s.faceiraq.and_faceiraq.theme.colour;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ready4s.faceiraq.and_faceiraq.R;
import com.ready4s.faceiraq.and_faceiraq.model.ColourListModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 20.04.2017.
 */

public class ThemeColourAdapter extends RecyclerView.Adapter<ThemeColourAdapter.ViewHolder>{

    private ArrayList<ColourListModel> mItemList = new ArrayList<>();
    private Context mContext;
    private int lastCheckedPosition = -1;
    private ColourListModel selectedColour;

    public ThemeColourAdapter(Context context) {
        this.mContext = context;

        iniColourItems();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.theme_colour_title)
        TextView mColourTitle;
        @Bind(R.id.theme_colour_colour)
        ImageView mColour;
        @Bind(R.id.theme_colour_check_box)
        CheckBox mColourCheckBox;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.theme_colour_check_box)
        public void onClick(View view) {
            if(lastCheckedPosition == -1) {
                lastCheckedPosition = getAdapterPosition();
                mItemList.get(lastCheckedPosition).setSelected(true);
                notifyItemChanged(lastCheckedPosition);
            } else {
                mItemList.get(lastCheckedPosition).setSelected(false);
                notifyItemChanged(lastCheckedPosition);
                lastCheckedPosition = getAdapterPosition();
                mItemList.get(lastCheckedPosition).setSelected(true);
                notifyItemChanged(lastCheckedPosition);
            }
        }
    }

    public void iniColourItems() {
        String[] colourItems = mContext.getResources().getStringArray(R.array.colors);
        String[] themeItems = mContext.getResources().getStringArray(R.array.theme_colours);

        mItemList.clear();

        mItemList.add(new ColourListModel(colourItems[0], R.color.beige, false, themeItems[0]));
        mItemList.add(new ColourListModel(colourItems[1], R.color.red, false, themeItems[1]));
        mItemList.add(new ColourListModel(colourItems[2], R.color.pink, false, themeItems[2]));
        mItemList.add(new ColourListModel(colourItems[3], R.color.purple, false, themeItems[3]));
        mItemList.add(new ColourListModel(colourItems[4], R.color.dark_blue, false, themeItems[4]));
        mItemList.add(new ColourListModel(colourItems[5], R.color.blue, false, themeItems[5]));
        mItemList.add(new ColourListModel(colourItems[6], R.color.turquoise, false, themeItems[6]));
        mItemList.add(new ColourListModel(colourItems[7], R.color.dark_green, false, themeItems[7]));
        mItemList.add(new ColourListModel(colourItems[8], R.color.green, false, themeItems[8]));
        mItemList.add(new ColourListModel(colourItems[9], R.color.yellow, false, themeItems[9]));
        mItemList.add(new ColourListModel(colourItems[10], R.color.orange, false, themeItems[10]));
    }

    public String getSelectedColour() {
        for (ColourListModel model : mItemList) {
            if (model.isSelected()) {
                return model.getThemeColour();
            }
        }
        return  mItemList.get(0).getThemeColour();
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
        holder.mColourCheckBox.setChecked(item.isSelected());
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}
