package com.brandtnewtonsoftware.rep_check.util.adapters.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.brandtnewtonsoftware.rep_check.R;


import java.util.List;

/**
 * Created by Brandt on 8/4/2015.
 */
public final class DetailRowListAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;
    List<ISetRowItem> detailRowList;
    int layout;

    public static DetailRowListAdapter newSetListAdapter(Context context, LayoutInflater layoutInflater) {
        return new DetailRowListAdapter(context, layoutInflater, R.layout.row_set);
    }

    private DetailRowListAdapter(Context context, LayoutInflater inflater, int layout) {
        mContext = context;
        mInflater = inflater;
        detailRowList = null;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return (detailRowList == null) ? 0 : detailRowList.size();
    }

    @Override
    public ISetRowItem getItem(int position) {
        return detailRowList.get(position);
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
            convertView = mInflater.inflate(layout, null);

            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.detail = (TextView) convertView.findViewById(R.id.detail);

            convertView.setTag(holder);
        } else {

            // skip all the expensive inflation/findViewById
            // and just get the holder you already made
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the current book's data in JSON form
        ISetRowItem detailRowItem = getItem(position);
        String title = detailRowItem.getReps();
        String text = detailRowItem.getWeight();
        String detail = detailRowItem.getPercentMax();

        // Send these Strings to the TextViews for display
        holder.title.setText(title);
        holder.text.setText(text);
        holder.detail.setText(detail);

        return convertView;
    }

    public void updateData(List<ISetRowItem> detailRowList) {
        // update the adapter's data set
        this.detailRowList = detailRowList;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        public TextView title;
        public TextView text;
        public TextView detail;
    }
}


