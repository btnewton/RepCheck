package com.brandtnewtonsoftware.rep_check.util.adapters.SetRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.brandtnewtonsoftware.rep_check.R;
import com.brandtnewtonsoftware.rep_check.util.adapters.detail.ISetRowItem;

import java.util.List;

/**
 * Created by Brandt on 9/24/2015.
 */
public class SetRecyclerViewAdapter extends RecyclerView.Adapter<SetViewHolder> implements AdapterView.OnItemClickListener {

    private List<ISetRowItem> setRowItems;
    private AdapterView.OnItemClickListener itemClickListener;
    private boolean showHelperText;

    public SetRecyclerViewAdapter(boolean showHelperText) {
        this.showHelperText = showHelperText;
    }

    public void setShowHelperText(boolean showHelperText) {
        if (this.showHelperText != showHelperText) {
            this.showHelperText = showHelperText;
            notifyDataSetChanged();
        }
    }

    @Override
    public SetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_set, parent, false);
        return new SetViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(SetViewHolder holder, int position) {
        ISetRowItem set = setRowItems.get(position);
        holder.reps.setText(set.getReps());
        if (showHelperText)
            holder.helperText.setText(set.getHelperText());
        else
            holder.helperText.setText("");
        holder.weight.setText(set.getWeight());
        holder.percentMax.setText(set.getPercentMax());
        holder.clickListener = this;
    }

    public void updateData(List<ISetRowItem> simpleConsumables) {
        this.setRowItems = simpleConsumables;
        notifyDataSetChanged();
    }

    public ISetRowItem getItem(int position) {
        return setRowItems.get(position);
    }

    @Override
    public int getItemCount() {
        if (setRowItems != null)
            return setRowItems.size();
        else
            return 0;
    }

    public void setItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        itemClickListener.onItemClick(adapterView, view, i, l);
    }
}
