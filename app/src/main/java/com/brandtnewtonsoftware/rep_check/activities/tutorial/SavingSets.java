package com.brandtnewtonsoftware.rep_check.activities.tutorial;

import com.brandtnewtonsoftware.rep_check.util.tutorial.topic.InfoTopic;

/**
 * Created by Brandt on 8/25/2015.
 */
public class SavingSets extends InfoTopic {
    @Override
    public CharSequence getTitle() {
        return "Saving Sets";
    }

    @Override
    protected CharSequence getBody() {
        return "You can save sets for reference later. Press the save button to save your set under the " +
                "currently displayed name. Long press the save button to save your set under a different " +
                "name.";
    }

    @Override
    public void onClickAction() {
    }
}
