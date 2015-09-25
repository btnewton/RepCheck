package com.brandtnewtonsoftware.rep_check.util.adapters.SetRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.brandtnewtonsoftware.rep_check.R;

/**
 * Created by Brandt on 9/24/2015.
 */
public class SetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView reps;
    public TextView helperText;
    public TextView weight;
    public TextView percentMax;
    AdapterView.OnItemClickListener clickListener;

    public SetViewHolder(View view, AdapterView.OnItemClickListener clickListener) {
        super(view);
        reps = (TextView) view.findViewById(R.id.set_reps);
        helperText = (TextView) view.findViewById(R.id.set_helper_text);
        weight = (TextView) view.findViewById(R.id.set_weight);
        percentMax = (TextView) view.findViewById(R.id.weight_percent_max);

        if (clickListener != null)
            view.setOnClickListener(this);
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View view) {
        if (clickListener != null)
            clickListener.onItemClick(null, view, getLayoutPosition(), view.getId());
    }
}
