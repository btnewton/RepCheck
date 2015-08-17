package com.brandtnewtonsoftware.repcheck.activities.preferences;

import android.content.Context;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.brandtnewtonsoftware.repcheck.R;

/**
 * Created by Brandt on 8/10/2015.
 */
class CustomPreferenceCategory extends PreferenceCategory {
    public CustomPreferenceCategory(Context context) {
        super(context);
    }

    public CustomPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomPreferenceCategory(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        TextView categoryTitle =  (TextView)super.onCreateView(parent);
        categoryTitle.setTextColor(parent.getResources().getColor(R.color.primary_700));

        return categoryTitle;
    }
}
