package com.brandtnewtonsoftware.rep_check.activitytests;

import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.brandtnewtonsoftware.rep_check.R;

import com.brandtnewtonsoftware.rep_check.activities.MainActivity;
import com.brandtnewtonsoftware.rep_check.activities.MaxRepFragment;

/**
 * Created by brandt on 8/1/15.
 */
public class MaxRepFragmentUITest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mMainActivity;
    private MaxRepFragment maxRepFragment;
    private EditText weightEditText;
    private Spinner repsSpinner;
    private Button subtractButton;
    private Button addButton;

    public MaxRepFragmentUITest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(true);

        mMainActivity = getActivity();

        maxRepFragment = (MaxRepFragment) waitForFragment("MaxRepFragment", 5000);

        weightEditText = (EditText)
                mMainActivity.findViewById(R.id.detail);
        repsSpinner = (Spinner)
                mMainActivity.findViewById(R.id.rep_spinner);

        subtractButton = (Button) mMainActivity.findViewById(R.id.quick_subtract);
        addButton = (Button) mMainActivity.findViewById(R.id.quick_add);
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
        assertNotNull(maxRepFragment);
        assertNotNull(weightEditText);
        assertNotNull(repsSpinner);
        assertNotNull(subtractButton);
        assertNotNull(addButton);
    }

    public void testQuickButtons_AssertOnScreen() {
        final View decorView = mMainActivity.getWindow().getDecorView();
        ViewAsserts.assertOnScreen(decorView, subtractButton);
        ViewAsserts.assertOnScreen(decorView, addButton);
    }

    public void testSubtractButton_AssertIncrement() {
        double expectedWeight = Double.parseDouble(weightEditText.getText().toString());
        expectedWeight -= maxRepFragment.getIncrementValue();

        TouchUtils.clickView(this, subtractButton);

        double currentWeight = Double.parseDouble(weightEditText.getText().toString());

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(expectedWeight, currentWeight);
    }

    public void testWeightEditText_AssertOnScreen() {
        final View decorView = mMainActivity.getWindow().getDecorView();
        ViewAsserts.assertOnScreen(decorView, weightEditText);
    }

    // Assert negative values are reset to 0
    @UiThreadTest
    public void testWeightEditText_NegativeInputTest() {
        weightEditText.setText("-1");
        String weightText = weightEditText.getText().toString();
        assertEquals("0", weightText);
        assertEquals(0.0, maxRepFragment.setSlot.getWeight());
    }

    public void testRepsSpinner_AssertOnScreen() {
        final View decorView = mMainActivity.getWindow().getDecorView();
        ViewAsserts.assertOnScreen(decorView, repsSpinner);
    }

// TODO get spinner click working
//    public void testRepsSpinner_InputUpdatesModel() {
//        final int firstRepValue = 5;
//        final int secondRepValue = 8;
//
//        TouchUtils.clickView(this, repsSpinner);
//        TouchUtils.clickView(this, repsSpinner.getChildAt(firstRepValue));
//        int title = maxRepFragment.getFormulaWrapper().getReps() - 1;
//        assertEquals(firstRepValue, title);
//
//        TouchUtils.clickView(this, repsSpinner.getChildAt(secondRepValue));
//        title = maxRepFragment.getFormulaWrapper().getReps() - 1;
//        assertEquals(secondRepValue, title);
//    }
}
