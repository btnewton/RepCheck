package com.example.brandt.repcheck;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Brandt on 6/26/2015.
 */
public class NavListAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;
    NavRowItem[] navRowItems;

    public NavListAdapter(Context context, LayoutInflater inflater, NavRowItem[] navRowItems) {
        mContext = context;
        mInflater = inflater;

        this.navRowItems = navRowItems;
    }

    @Override
    public int getCount() {
        return (navRowItems == null) ? 0 : navRowItems.length;
    }

    @Override
    public NavRowItem getItem(int position) {
        return navRowItems[position];
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
            convertView = mInflater.inflate(R.layout.row_drawer_item, null);

            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.text = (TextView) convertView.findViewById(R.id.text);

            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {

            // skip all the expensive inflation/findViewById
            // and just get the holder you already made
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the current book's data in JSON form
        NavRowItem navRowItem = getItem(position);
        int icon = navRowItem.getIcon();
        String text = navRowItem.getText();

        // Send these Strings to the TextViews for display
        holder.icon.setImageDrawable(mContext.getResources().getDrawable(icon));
        holder.text.setText(text);

        return convertView;
    }

    public void updateData(NavRowItem[] navRowItems) {
        // update the adapter's dataset
        this.navRowItems = navRowItems;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        public ImageView icon;
        public TextView text;
    }
}

class NavRowItem {

    int icon;
    String text;

    public NavRowItem(int icon, String text) {
        this.icon = icon;
        this.text = text;
    }

    public int getIcon() {
        return icon;
    }

    public String getText() {
        return text;
    }
}
