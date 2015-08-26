package com.brandtnewtonsoftware.rep_check.activities.tutorial;

import com.brandtnewtonsoftware.rep_check.util.tutorial.topic.InfoTopic;

/**
 * Created by Brandt on 8/25/2015.
 */
public class LoadingSets extends InfoTopic {
    @Override
    public CharSequence getTitle() {
        return "Loading Sets";
    }

    @Override
    protected CharSequence getBody() {
        return "You can load saved sets to view or edit. While in the load screen you can long press " +
                "a set to rename it.";
    }

    @Override
    public void onClickAction() {

    }
}
