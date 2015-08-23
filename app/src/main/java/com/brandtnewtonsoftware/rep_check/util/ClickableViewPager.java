package com.brandtnewtonsoftware.rep_check.util;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.Calendar;

/**
 * Created by Brandt on 8/23/2015.
 */
public class ClickableViewPager extends ViewPager {

    private static final int MAX_CLICK_DURATION = 200;
    private long startClickTime;
    private OnClickListener onClickListener;


    public ClickableViewPager(Context context) {
        super(context);
    }

    public ClickableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (MotionEventCompat.getActionMasked(ev)) {
            case MotionEvent.ACTION_DOWN: {
                startClickTime = Calendar.getInstance().getTimeInMillis();
                break;
            }
            case MotionEvent.ACTION_UP: {
                long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                if (clickDuration < MAX_CLICK_DURATION) {
                    onClickListener.onClick(this);
                    return true;
                }
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
