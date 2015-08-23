package com.brandtnewtonsoftware.rep_check.util.tutorial;

import android.animation.Animator;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brandtnewtonsoftware.rep_check.R;
import com.brandtnewtonsoftware.rep_check.util.ClickableViewPager;
import com.brandtnewtonsoftware.rep_check.util.tutorial.topic.ConclusionTopic;
import com.brandtnewtonsoftware.rep_check.util.tutorial.topic.InfoTopic;
import com.brandtnewtonsoftware.rep_check.util.tutorial.topic.TutorialTopic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brandt on 8/23/2015.
 */
public class TutorialBuilder implements ViewPager.OnPageChangeListener, View.OnClickListener {

    List<TutorialEventListener> listeners = new ArrayList<>();
    TextView topicTitle;
    Activity activity;
    TopicsPagerAdapter pagerAdapter;
    View pointer;
    ProgressBar progressBar;
    private ClickableViewPager viewPager;

    List<TutorialTopic> topics = new ArrayList<>();

    public TutorialBuilder() {
        topics.add(new InfoTopic());
        topics.add(new InfoTopic());
        topics.add(new InfoTopic());
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

        viewPager = (ClickableViewPager) activity.findViewById(R.id.tutorial_viewpager);
        pagerAdapter = new TopicsPagerAdapter(activity.getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOnClickListener(this);
        progressBar = (ProgressBar) activity.findViewById(R.id.tutorial_progress);

        topicTitle.setText(((TutorialTopic)pagerAdapter.getItem(viewPager.getCurrentItem())).getTitle());

        onTutorialStart();

        return this;
    }

    /**
     * Animates the tutorial dialog down and then removes it once off screen.
     */
    public void closeButtonAction() {
        final View tutorialDialog = activity.findViewById(R.id.tutorial_dialog);
        int tutorialDialogDPHeight = tutorialDialog.getHeight();
        tutorialDialog.animate().translationY(tutorialDialogDPHeight).setDuration(activity.getResources().getInteger(android.R.integer.config_shortAnimTime)).setListener(new Animator.AnimatorListener() {
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int scrollProgress = 0;
        scrollProgress = (int) ((100 * positionOffset) / pagerAdapter.getCount());
        progressBar.setProgress(scrollProgress + 100 * (position + 1) / pagerAdapter.getCount());
    }

    @Override
    public void onPageSelected(int position) {
        topicTitle.setText(((TutorialTopic)pagerAdapter.getItem(position)).getTitle());
        onTopicChanged();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tutorial_viewpager) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }

    private class TopicsPagerAdapter extends FragmentStatePagerAdapter {

        public TopicsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position < topics.size())
                return topics.get(position);
            else
                return new ConclusionTopic();
        }

        @Override
        public int getCount() {
            return topics.size() + 1;
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