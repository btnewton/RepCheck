package com.example.brandt.repcheck.util.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.brandt.repcheck.R;

/**
 * Created by Brandt on 7/25/2015.
 */
public class ImageOptionListAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;
    String[] optionNameList;
    int[] optionImageIDs;

    public ImageOptionListAdapter(Context context, LayoutInflater inflater, int[] optionImageIDs,
                                  String[] optionNameList) {
        mContext = context;
        mInflater = inflater;

        this.optionImageIDs = optionImageIDs;
        this.optionNameList = optionNameList;
    }

    @Override
    public int getCount() {
        return optionNameList.length;
    }

    @Override
    public String getItem(int position) {
        return optionNameList[position];
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
            convertView = mInflater.inflate(R.layout.row_image, null);

            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.optionImage = (ImageView) convertView.findViewById(R.id.option_image);
            holder.optionName = (TextView) convertView.findViewById(R.id.option_name);

            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {

            // skip all the expensive inflation/findViewById
            // and just get the holder you already made
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the current book's data in JSON form
        String optionName = getItem(position);
        int imageID = optionImageIDs[position];


        // Send these Strings to the TextViews for display
        if (imageID != 0) {
            holder.optionImage.setImageDrawable(mContext.getResources().getDrawable(imageID));
            holder.optionImage.setVisibility(View.VISIBLE);
        } else {
            holder.optionImage.setVisibility(View.INVISIBLE);
        }
        holder.optionName.setText(optionName);

        return convertView;
    }

    public void updateOptions(int[] optionImageIDs, String[] optionNameList) {
        this.optionImageIDs = optionImageIDs;
        this.optionNameList = optionNameList;

        notifyDataSetChanged();
    }

    private static class ViewHolder {
        public ImageView optionImage;
        public TextView optionName;
    }
}
