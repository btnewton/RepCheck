package com.brandtnewtonsoftware.rep_check.util.tutorial.topic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brandtnewtonsoftware.rep_check.R;

/**
 * Created by Brandt on 8/23/2015.
 */
public class ConclusionTopic extends TutorialTopic {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.tutorial_topic, container, false);
        TextView body = (TextView) view.findViewById(R.id.topic_body);

        body.setText(getBody());
        return view;
    }

    @Override
    public CharSequence getTitle() {
        return "All done!";
    }

    @Override
    public CharSequence getBody() {
        return "Congratulations! You finished the tutorial.";
    }
}
