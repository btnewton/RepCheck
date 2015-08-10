package com.example.brandt.repcheck.util.adapters.standard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.brandt.repcheck.R;

import java.util.List;

/**
 * Created by brandt on 2/6/15.
 */
public class StandardRowListAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;
    List<IStandardRowItem> standardRowItems;
    int layout;

    public static StandardRowListAdapter newStandardAdapter(Context context, LayoutInflater layoutInflater) {
        return new StandardRowListAdapter(context, layoutInflater, R.layout.row_standard);
    }

    public static StandardRowListAdapter newSaveSlotAdapter(Context context, LayoutInflater layoutInflater) {
        return new StandardRowListAdapter(context, layoutInflater, R.layout.row_save_slot);
    }

    public static StandardRowListAdapter newBarLoadAdapter(Context context, LayoutInflater layoutInflater) {
        return new StandardRowListAdapter(context, layoutInflater, R.layout.row_bar_load);
    }

    private StandardRowListAdapter(Context context, LayoutInflater inflater, int layout) {
        mContext = context;
        mInflater = inflater;
        standardRowItems = null;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return (standardRowItems == null) ? 0 : standardRowItems.size();
    }

    @Override
    public IStandardRowItem getItem(int position) {
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
            convertView = mInflater.inflate(layout, null);

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
        IStandardRowItem standardRowItem = getItem(position);
        String title = standardRowItem.getTitle();
        String detail = standardRowItem.getText();

        // Send these Strings to the TextViews for display
        holder.title.setText(title);
        holder.text.setText(detail);

        return convertView;
    }

    public void updateData(List<IStandardRowItem> standardRowItems) {
        // update the adapter's data set
        this.standardRowItems = standardRowItems;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        public TextView title;
        public TextView text;
    }
}

