package com.brandtnewtonsoftware.rep_check.util.adapters.standard;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.brandtnewtonsoftware.rep_check.R;

/**
 * Created by Brandt on 9/25/2015.
 */
public class StandardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public TextView title;
    public TextView text;

    AdapterView.OnItemClickListener clickListener;
    AdapterView.OnItemLongClickListener longClickListener;

    public StandardViewHolder(View view, AdapterView.OnItemClickListener clickListener, AdapterView.OnItemLongClickListener longClickListener) {
        super(view);
        title = (TextView) view.findViewById(R.id.row_title);
        text = (TextView) view.findViewById(R.id.row_text);

        if (clickListener != null)
            view.setOnClickListener(this);
        if (longClickListener != null)
            view.setOnLongClickListener(this);

        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }


    @Override
    public void onClick(View view) {
        if (clickListener != null)
            clickListener.onItemClick(null, view, getLayoutPosition(), view.getId());
    }

    @Override
    public boolean onLongClick(View view) {
        return longClickListener != null && longClickListener.onItemLongClick(null, view, getLayoutPosition(), view.getId());
    }
}
