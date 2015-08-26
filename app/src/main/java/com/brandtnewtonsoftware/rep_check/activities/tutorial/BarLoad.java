package com.brandtnewtonsoftware.rep_check.activities.tutorial;

import com.brandtnewtonsoftware.rep_check.util.tutorial.topic.InfoTopic;

/**
 * Created by Brandt on 8/25/2015.
 */
public class BarLoad extends InfoTopic {
    @Override
    public CharSequence getTitle() {
        return "Bar Load";
    }

    @Override
    protected CharSequence getBody() {
        return "Tap one of the sets in the calculated list to view the bar load for that weight. The " +
                "bar load is the configuration of weights that you should put on the bar. The plate " +
                "weights change depending on units and plate type. You can change plate type and " +
                "units in the settings screen.";
    }

    @Override
    public void onClickAction() {

    }
}
