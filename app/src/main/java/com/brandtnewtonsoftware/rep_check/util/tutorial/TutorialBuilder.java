package com.brandtnewtonsoftware.rep_check.util.tutorial;

import android.animation.Animator;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brandtnewtonsoftware.rep_check.R;
import com.brandtnewtonsoftware.rep_check.util.tutorial.topic.ConclusionTopic;
import com.brandtnewtonsoftware.rep_check.util.tutorial.topic.TutorialTopic;

import java.util.List;

/**
 * Created by Brandt on 8/23/2015.
 */
public class TutorialBuilder {

    List<TutorialEventListener> listeners;
    TextView topicTitle;
    Activity activity;
    View pointer;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    List<TutorialTopic> topics;

    public TutorialBuilder() {
        topics.add(new ConclusionTopic());
        topics.add(new ConclusionTopic());
        topics.add(new ConclusionTopic());
        topics.add(new ConclusionTopic());
    }

    public TutorialBuilder setTopics(List<TutorialTopic> topics) {
        this.topics = topics;
        return this;
    }

    public TutorialBuilder start(FragmentActivity activity, RelativeLayout parent) {
        this.activity = activity;

        pointer = LayoutInflater.from(activity).inflate(R.layout.pointer, parent, false);
        parent.addView(pointer);

        View tutorialView = LayoutInflater.from(activity).inflate(R.layout.tutorial_dialog, parent, false);
        parent.addView(tutorialView);

        View closeButton = activity.findViewById(R.id.tutorial_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeButtonAction();
            }
        });

        topicTitle = (TextView) activity.findViewById(R.id.tutorial_title);
        viewPager = (ViewPager) activity.findViewById(R.id.tutorial_viewpager);
        pagerAdapter = new TopicsPagerAdapter(activity.getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        return this;
    }

    /**
     * Animates the tutorial dialog down and then removes it once off screen.
     */
    public void closeButtonAction() {
        final View tutorialDialog = activity.findViewById(R.id.tutorial_dialog);
        int tutorialDialogDPHeight = tutorialDialog.getHeight();
        tutorialDialog.animate().translationY(tutorialDialogDPHeight).setDuration(activity.getResources().getInteger(android.R.integer.config_mediumAnimTime)).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ((ViewManager) tutorialDialog.getParent()).removeView(tutorialDialog);
                onTutorialQuit();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private class TopicsPagerAdapter extends FragmentStatePagerAdapter {

        public TopicsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return topics.get(position);
        }

        @Override
        public int getCount() {
            return topics.size();
        }
    }

    //region Listeners
    public void addListener(TutorialEventListener listener) {
        listeners.add(listener);
    }

    public void removeListener(TutorialEventListener listener) {
        listeners.remove(listener);
    }

    public void onTopicChanged() {
        int currentTopicIndex = viewPager.getCurrentItem();

        for (TutorialEventListener listener : listeners) {
            listener.onTopicChanged(currentTopicIndex);
        }
    }

    public void onTutorialStart() {
        for (TutorialEventListener listener : listeners) {
            listener.onTutorialStart();
        }
    }

    public void onTutorialComplete() {
        for (TutorialEventListener listener : listeners) {
            listener.onTutorialComplete();
        }
    }

    public void onTutorialQuit() {
        int currentTopicIndex = viewPager.getCurrentItem();

        for (TutorialEventListener listener : listeners) {
            listener.onTutorialQuit(currentTopicIndex);
        }
    }
    //endregion
}