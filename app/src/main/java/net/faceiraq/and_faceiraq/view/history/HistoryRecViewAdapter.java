package net.faceiraq.and_faceiraq.view.history;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.faceiraq.and_faceiraq.R;

import net.faceiraq.and_faceiraq.controller.HistoryActivity;
import net.faceiraq.and_faceiraq.model.SharedPreferencesHelper;
import net.faceiraq.and_faceiraq.model.database.history.HistoryRecord;
import net.faceiraq.and_faceiraq.model.utils.ImageUtil;
import net.faceiraq.and_faceiraq.model.utils.TimeUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

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

    private HistoryFragment.OnHistoryActionsListener onHistoryActionsListener;
    private HistoryActivity context;

    public class HistoryRecordHolder extends RecyclerView.ViewHolder {

        long id;
        @Bind(R.id.dateLabel) TextView dateLabel;
        @Bind(R.id.pageTitle) TextView pageTitle;
        @Bind(R.id.pageUrl) TextView pageUrl;
        @Bind(R.id.pageIcon) ImageView pageIcon;
        @Bind(R.id.deleteButton) LinearLayout deleteButton;
        @Bind(R.id.history_section) LinearLayout historySection;


        public HistoryRecordHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            pageTitle = (TextView) itemView.findViewById(R.id.pageTitle);
//            pageUrl = (TextView) itemView.findViewById(R.id.pageUrl);
//            pageIcon = (ImageView) itemView.findViewById(R.id.pageIcon);
//            historySection = (LinearLayout) itemView.findViewById(R.id.history_section);
//            deleteButton = (LinearLayout) itemView.findViewById(R.id.deleteButton);
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

        @OnClick(R.id.history_section)
        public void onClick() {
            int pos = getAdapterPosition();
            SharedPreferencesHelper.setCardNumber(
                    context,
                    historyRecords.get(pos).getId());
            SharedPreferencesHelper.setCardUrl(
                    context,
                    historyRecords.get(pos).getAddress());
            context.setResult(RESULT_OK);
            context.finish();
        }
    }

    public HistoryRecViewAdapter(HistoryFragment.OnHistoryActionsListener onHistoryActionsListener, HistoryActivity context) {
        this.historyRecords = new ArrayList<>();
        this.filteredHistoryRecords = new ArrayList<>();
        this.onHistoryActionsListener = onHistoryActionsListener;
        this.context = context;
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

        String recordDate = getDateLabel(position);
        if (shouldDateBeShown(position, recordDate)) {
            holder.dateLabel.setText(recordDate);
            holder.dateLabel.setVisibility(View.VISIBLE);
        } else {
            holder.dateLabel.setVisibility(View.GONE);
        }
        holder.id = filteredHistoryRecords.get(position).getId();
        holder.pageTitle.setText(filteredHistoryRecords.get(position).getTitle());
        holder.pageUrl.setText(filteredHistoryRecords.get(position).getAddress());

        String base64Icon = filteredHistoryRecords.get(position).getBase64Logo();
        byte[] iconByteArray = null;
        if (base64Icon != null) {
            iconByteArray = Base64.decode(base64Icon, Base64.DEFAULT);
        }

        Glide.with(context)
                .load(iconByteArray)
                .asBitmap()
                .fitCenter()
                .error(R.drawable.blank_favicon)
                .into(holder.pageIcon);

    }

    private boolean shouldDateBeShown(int position, String recordDate) {
        return position == 0 || !recordDate.equals(getDateLabel(position - 1));
    }

    private String getDateLabel(int position) {
        return TimeUtil.getFormattedDateWithPrefix(
                filteredHistoryRecords.get(position).getTimestamp(),
                TimeUtil.HISTORY_FORMAT);
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
