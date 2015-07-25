package com.example.brandt.repcheck.util.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.brandt.repcheck.R;

import java.util.List;

/**
 * Created by Brandt on 7/25/2015.
 */
public class StandardRowItemAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;
    List<StandardRowItem> standardRowItems;

    public StandardRowItemAdapter(Context context, LayoutInflater inflater, List<StandardRowItem> standardRowItems) {
        mContext = context;
        mInflater = inflater;
        this.standardRowItems = standardRowItems;
    }

    @Override
    public int getCount() {
        return (standardRowItems == null) ? 0 : standardRowItems.size();
    }

    @Override
    public StandardRowItem getItem(int position) {
        return standardRowItems.get(position);
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
            convertView = mInflater.inflate(R.layout.row_standard, null);

            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.text = (TextView) convertView.findViewById(R.id.text);

            convertView.setTag(holder);
        } else {

            // skip all the expensive inflation/findViewById
            // and just get the holder you already made
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the current book's data in JSON form
        StandardRowItem standardRowItemHolder = getItem(position);
        String title = standardRowItemHolder.getTitle();
        String text = standardRowItemHolder.getText();

        // Send these Strings to the TextViews for display
        holder.title.setText(title);
        holder.text.setText(text);

        return convertView;
    }

    public void updateData(List<StandardRowItem> standardRowItems) {
        // update the adapter's data set
        this.standardRowItems = standardRowItems;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        public TextView title;
        public TextView text;
    }
}

