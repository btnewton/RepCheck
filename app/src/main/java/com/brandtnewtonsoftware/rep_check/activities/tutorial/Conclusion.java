package com.brandtnewtonsoftware.rep_check.activities.tutorial;

import com.brandtnewtonsoftware.rep_check.util.tutorial.topic.InfoTopic;

/**
 * Created by Brandt on 8/25/2015.
 */
public class Conclusion extends InfoTopic {

    @Override
    public CharSequence getTitle() {
        return "All done!";
    }

    @Override
    protected CharSequence getBody() {
        return "That's it! Have a great and safe workout!";
    }

    @Override
    public void onClickAction() {
    }
}
