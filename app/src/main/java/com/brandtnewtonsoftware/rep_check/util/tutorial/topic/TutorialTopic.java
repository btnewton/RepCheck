package com.brandtnewtonsoftware.rep_check.util.tutorial.topic;

import android.support.v4.app.Fragment;

import com.brandtnewtonsoftware.rep_check.util.tutorial.TutorialBuilder;

/**
 * Created by Brandt on 8/23/2015.
 */
public abstract class TutorialTopic extends Fragment {
    private static TutorialBuilder tutorialBuilder;

    public static final String LOG_TAG = "TutorialTopic";

    public abstract CharSequence getTitle();

    public abstract void onClickAction();

    public static void setTutorialBuilder(TutorialBuilder tutorialBuilder) {
        TutorialTopic.tutorialBuilder = tutorialBuilder;
    }

    protected void closeTutorial() {
        tutorialBuilder.closeButtonAction();
    }
}
