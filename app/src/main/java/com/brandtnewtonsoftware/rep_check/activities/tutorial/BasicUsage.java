package com.brandtnewtonsoftware.rep_check.activities.tutorial;

import com.brandtnewtonsoftware.rep_check.util.tutorial.topic.InfoTopic;

/**
 * Created by Brandt on 8/25/2015.
 */
public class BasicUsage extends InfoTopic {
    @Override
    public CharSequence getTitle() {
        return "The Basics";
    }

    @Override
    protected CharSequence getBody() {
        return "Use Rep Check to estimate the weight you can lift for reps 1 to 20. To do this enter " +
                "the weight and reps completed at the top of the app.";
    }

    @Override
    public void onClickAction() {

    }
}
