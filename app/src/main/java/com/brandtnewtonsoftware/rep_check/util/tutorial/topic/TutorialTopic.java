package com.brandtnewtonsoftware.rep_check.util.tutorial.topic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brandtnewtonsoftware.rep_check.R;

/**
 * Created by Brandt on 8/23/2015.
 */
public abstract class TutorialTopic extends Fragment{
    public static final String LOG_TAG = "TutorialTopic";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.tutorial_topic, container, false);
        TextView body = (TextView) view.findViewById(R.id.topic_body);

        body.setText(getBody());
        return view;
    }

    public abstract CharSequence getTitle();
    protected abstract CharSequence getBody();
}
