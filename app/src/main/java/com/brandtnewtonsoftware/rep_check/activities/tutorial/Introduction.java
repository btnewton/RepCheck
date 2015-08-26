package com.brandtnewtonsoftware.rep_check.activities.tutorial;

import com.brandtnewtonsoftware.rep_check.util.tutorial.topic.InfoTopic;

/**
 * Created by Brandt on 8/25/2015.
 */
public class Introduction extends InfoTopic {

    @Override
    public CharSequence getTitle() {
        return "Welcome to Rep Check!";
    }

    @Override
    protected CharSequence getBody() {
        return "Rep Check is a One Rep Max calculator that also helps you track your progress. This " +
                "tutorial will help you get the most out of Rep Check. ";
    }

    @Override
    public void onClickAction() {

    }
}
