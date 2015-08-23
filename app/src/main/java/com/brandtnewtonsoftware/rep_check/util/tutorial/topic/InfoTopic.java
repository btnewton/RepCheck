package com.brandtnewtonsoftware.rep_check.util.tutorial.topic;

/**
 * Created by Brandt on 8/23/2015.
 */
public class InfoTopic extends TutorialTopic {


    @Override
    public CharSequence getTitle() {
        return "Welcome to the tutorial!";
    }

    @Override
    public CharSequence getBody() {
        return "You can quit the tutorial at any time and start it again from Settings.";
    }
}
