package com.brandtnewtonsoftware.rep_check.activitytests;

import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;

import com.brandtnewtonsoftware.rep_check.activities.MainActivity;

/**
 * Created by brandt on 8/28/15.
 */
public class FormulaTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mMainActivity;

    public FormulaTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(true);

        mMainActivity = getActivity();
    }

    protected Fragment waitForFragment(String tag, int timeout) {
        long endTime = SystemClock.uptimeMillis() + timeout;
        while (SystemClock.uptimeMillis() <= endTime) {

            Fragment fragment = mMainActivity.getSupportFragmentManager().findFragmentByTag(tag);
            if (fragment != null) {
                return fragment;
            }
        }
        return null;
    }

    public void testPreconditions() {
        assertNotNull(mMainActivity);
    }
}

