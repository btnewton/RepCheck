package com.example.brandt.repcheck.util.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.brandt.repcheck.R;

/**
 * Created by brandt on 2/6/15.
 */
public class WeightListAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;
    WeightHolder[] weightHolderList;

    public WeightListAdapter(Context context, LayoutInflater inflater, WeightHolder[] weightHolderList) {
        mContext = context;
        mInflater = inflater;
        this.weightHolderList = weightHolderList;
    }

    @Override
    public int getCount() {
        return (weightHolderList == null) ? 0 : weightHolderList.length;
    }

    @Override
    public WeightHolder getItem(int position) {
        return weightHolderList[position];
    }

    @Override
    public long getItemId(int position) {
        // Currently not used
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // check if the view already exists
        // if so, no need to inflate and findViewById again!
        if (convertView == null) {

            // Inflate the custom row layout from your XML.
            convertView = mInflater.inflate(R.layout.row_weight, null);

            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.reps = (TextView) convertView.findViewById(R.id.reps);
            holder.weight = (TextView) convertView.findViewById(R.id.weight);

            convertView.setTag(holder);
        } else {

            // skip all the expensive inflation/findViewById
            // and just get the holder you already made
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the current book's data in JSON form
        WeightHolder weightHolder = getItem(position);
        String title = weightHolder.getReps();
        String detail1 = weightHolder.getWeight();

        // Send these Strings to the TextViews for display
        holder.reps.setText(title);
        holder.weight.setText(detail1);

        return convertView;
    }

    public void updateData(WeightHolder[] weightHolderList) {
        // update the adapter's data set
        this.weightHolderList = weightHolderList;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        public TextView reps;
        public TextView weight;
    }
}

