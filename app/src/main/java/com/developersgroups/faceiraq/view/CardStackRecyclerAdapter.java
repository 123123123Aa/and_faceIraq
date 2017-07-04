package com.developersgroups.faceiraq.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.developersgroups.faceiraq.R;
import com.developersgroups.faceiraq.model.database.opened_pages.OpenedPageModel;
import com.developersgroups.faceiraq.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Paweł Sałata on 25.04.2017.
 * email: psalata9@gmail.com
 */

public class CardStackRecyclerAdapter extends RecyclerView.Adapter<CardStackRecyclerAdapter.CardHolder> {

    private List<OpenedPageModel> openedPages;
    private Context context;

    public CardStackRecyclerAdapter(Context context, List<OpenedPageModel> openedPages) {
        this.openedPages = new ArrayList<>(openedPages);
        this.context = context;
    }

    @Override
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.opened_page_layout, parent, false);
        return new CardHolder(view);
    }

    @Override
    public void onBindViewHolder(CardHolder holder, int position) {
        holder.cardTitle.setText(openedPages.get(position).getTitle());
        byte[] screenshotByteArray = openedPages.get(position).getScreenshot();
        try {
            holder.cardContent.setImageBitmap(ImageUtil.convertToBitmap(screenshotByteArray));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return openedPages.size();
    }

    public class CardHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.cardTitle) TextView cardTitle;
        @Bind(R.id.cardContent) ImageView cardContent;
        @Bind(R.id.deleteIcon) ImageView deleteIcon;

        public CardHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
