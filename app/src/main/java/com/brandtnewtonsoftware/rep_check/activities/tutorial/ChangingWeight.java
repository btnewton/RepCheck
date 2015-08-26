package com.brandtnewtonsoftware.rep_check.activities.tutorial;

import com.brandtnewtonsoftware.rep_check.util.tutorial.topic.InfoTopic;

/**
 * Created by Brandt on 8/25/2015.
 */
public class ChangingWeight extends InfoTopic {
    @Override
    public CharSequence getTitle() {
        return "Changing Weight";
    }

    @Override
    protected CharSequence getBody() {
        return "To change the weight of the current set use the keyboard or the quick add/remove " +
                "buttons. You can adjust the quick buttons by long pressing them. This menu lets you " +
                "choose a weight from the plate type you have selected.";
    }

    @Override
    public void onClickAction() {

    }
}
