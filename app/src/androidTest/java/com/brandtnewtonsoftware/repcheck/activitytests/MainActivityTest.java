package com.brandtnewtonsoftware.repcheck.activitytests;

import android.support.v7.widget.Toolbar;
import android.test.ActivityInstrumentationTestCase2;

import com.brandtnewtonsoftware.repcheck.R;

import com.brandtnewtonsoftware.repcheck.activities.MainActivity;
import com.google.android.gms.ads.AdView;

/**
 * Created by brandt on 8/1/15.
 */
public final class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mMainActivity;
    private Toolbar mToolbar;
    private AdView mAdView;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMainActivity = getActivity();
        mToolbar =
                (Toolbar) mMainActivity
                        .findViewById(R.id.toolbar);
//        mAdView = (AdView) mMainActivity.findViewById(R.id.adView);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testPreconditions() {
        assertNotNull(mMainActivity);
        assertNotNull(mToolbar);
        assertNotNull(mAdView);
    }

    public void testToolbar_titleText() {
        testPreconditions();
        // Test title
        final String expected =
                mMainActivity.getString(R.string.app_name);
        final String actual = mToolbar.getTitle().toString();
        assertEquals(expected, actual);
    }
}
