package com.brandtnewtonsoftware.rep_check.util.tutorial;

/**
 * Created by Brandt on 8/23/2015.
 */
public interface TutorialEventListener {
    void onTutorialStart();
    void onTutorialComplete();
    void onTutorialQuit(int topic);
    void onTopicChanged(int topic);
}
