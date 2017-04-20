package com.ready4s.faceiraq.and_faceiraq.view.history;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ready4s.faceiraq.and_faceiraq.R;
import com.ready4s.faceiraq.and_faceiraq.model.database.history.HistoryRecord;
import com.ready4s.faceiraq.and_faceiraq.model.utils.ImageUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Paweł Sałata on 19.04.2017.
 * email: psalata9@gmail.com
 */

class HistoryRecViewAdapter extends RecyclerView.Adapter<HistoryRecViewAdapter.HistoryRecordHolder>
                            implements Filterable{

    private static final String TAG = "HistoryRecViewAdapter";
    private List<HistoryRecord> historyRecords;

    private List<HistoryRecord> filteredHistoryRecords;
    private HistoryFilter historyFilter;

    private DisplayHistoryFragment.OnHistoryActionsListener onHistoryActionsListener;

    public class HistoryRecordHolder extends RecyclerView.ViewHolder {

        long id;
        TextView pageTitle;
        TextView pageUrl;
        ImageView pageIcon;
        LinearLayout deleteButton;

        public HistoryRecordHolder(View itemView) {
            super(itemView);
            pageTitle = (TextView) itemView.findViewById(R.id.pageTitle);
            pageUrl = (TextView) itemView.findViewById(R.id.pageUrl);
            pageIcon = (ImageView) itemView.findViewById(R.id.pageIcon);
            deleteButton = (LinearLayout) itemView.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onDeleteHistory record click id: " + id);
                    onHistoryActionsListener.onDeleteRecordClick(id);
                    removeAt(getAdapterPosition());
                }
            });
        }

        public void removeAt(int position) {
            HistoryRecord deletedRecord = filteredHistoryRecords.get(position);
            historyRecords.remove(deletedRecord);
            filteredHistoryRecords.remove(deletedRecord);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, filteredHistoryRecords.size());
        }
    }

    public HistoryRecViewAdapter(DisplayHistoryFragment.OnHistoryActionsListener onHistoryActionsListener) {
        this.historyRecords = new ArrayList<>();
        this.filteredHistoryRecords = new ArrayList<>();
        this.onHistoryActionsListener = onHistoryActionsListener;
    }

    public List<HistoryRecord> getHistoryRecords() {
        return historyRecords;
    }

    public void setHistoryRecords(List<HistoryRecord> historyRecords) {
        this.historyRecords = historyRecords;
        this.filteredHistoryRecords = historyRecords;
    }

    @Override
    public HistoryRecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_record_view, parent, false);
        return new HistoryRecordHolder(v);
    }

    @Override
    public void onBindViewHolder(HistoryRecordHolder holder, int position) {
        holder.id = filteredHistoryRecords.get(position).getId();
        holder.pageTitle.setText(filteredHistoryRecords.get(position).getTitle());
        holder.pageUrl.setText(filteredHistoryRecords.get(position).getAddress());

        if (filteredHistoryRecords.get(position).getBase64Logo() != null) {
            Bitmap iconBitmap = ImageUtil.decodeBase64(filteredHistoryRecords.get(position).getBase64Logo());
            holder.pageIcon.setImageBitmap(iconBitmap);
        }
    }

    @Override
    public int getItemCount() {
        return filteredHistoryRecords.size();
    }

    @Override
    public Filter getFilter() {
        if(historyFilter == null)
            historyFilter = new HistoryFilter(this, historyRecords);
        return historyFilter;
    }

    private static class HistoryFilter extends Filter {

        private final HistoryRecViewAdapter adapter;

        private final List<HistoryRecord> originalList;

        private final List<HistoryRecord> filteredList;

        private HistoryFilter(HistoryRecViewAdapter adapter, List<HistoryRecord> originalList) {
            super();
            this.adapter = adapter;
            this.originalList = new LinkedList<>(originalList);
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (final HistoryRecord record : originalList) {
                    if (record.getAddress().contains(filterPattern)) {
                        filteredList.add(record);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.filteredHistoryRecords.clear();
            adapter.filteredHistoryRecords.addAll((ArrayList<HistoryRecord>) results.values);
            adapter.notifyDataSetChanged();
        }
    }
}
