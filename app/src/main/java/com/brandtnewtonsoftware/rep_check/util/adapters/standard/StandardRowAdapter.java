package com.brandtnewtonsoftware.rep_check.util.adapters.standard;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.brandtnewtonsoftware.rep_check.R;

import java.util.List;

/**
 * Created by Brandt on 9/25/2015.
 */
public class StandardRowAdapter extends RecyclerView.Adapter<StandardViewHolder> implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private List<IStandardRowItem> standardRowItems;
    private boolean showHelperText;
    private int layoutId;

    private AdapterView.OnItemClickListener itemClickListener;
    private AdapterView.OnItemLongClickListener itemLongClickListener;

    public static StandardRowAdapter newBarLoadAdapter(List<IStandardRowItem> standardRowItems) {
        return new StandardRowAdapter(standardRowItems, R.layout.row_bar_load);
    }

    public static StandardRowAdapter newSaveSlotAdapter(List<IStandardRowItem> standardRowItems) {
        return new StandardRowAdapter(standardRowItems, R.layout.row_save_slot);
    }

    private StandardRowAdapter(List<IStandardRowItem> standardRowItems, int layoutId) {
        this.standardRowItems = standardRowItems;
        this.layoutId = layoutId;
    }

    public void setShowHelperText(boolean showHelperText) {
        if (this.showHelperText != showHelperText) {
            this.showHelperText = showHelperText;
            notifyDataSetChanged();
        }
    }

    @Override
    public StandardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new StandardViewHolder(view, this, this);
    }

    @Override
    public void onBindViewHolder(StandardViewHolder holder, int position) {
        IStandardRowItem rowItem = standardRowItems.get(position);
        holder.title.setText(rowItem.getTitle());
        holder.text.setText(rowItem.getText());
    }

    public void updateData(List<IStandardRowItem> simpleRowItems) {
        this.standardRowItems = simpleRowItems;
        notifyDataSetChanged();
    }

    public IStandardRowItem getItem(int position) {
        return standardRowItems.get(position);
    }

    @Override
    public int getItemCount() {
        if (standardRowItems != null)
            return standardRowItems.size();
        else
            return 0;
    }

    public void setItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setItemLongClickListener(AdapterView.OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (itemClickListener != null)
            itemClickListener.onItemClick(parent, view, position, id);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (itemLongClickListener != null)
            return itemLongClickListener.onItemLongClick(parent, view, position, id);
        return false;
    }
}
