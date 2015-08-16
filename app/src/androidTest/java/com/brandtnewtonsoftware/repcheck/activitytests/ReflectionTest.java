package com.brandtnewtonsoftware.repcheck.activitytests;

import android.test.ActivityInstrumentationTestCase2;

import com.brandtnewtonsoftware.repcheck.R;
import com.brandtnewtonsoftware.repcheck.activities.MainActivity;
import com.brandtnewtonsoftware.repcheck.models.calculations.FormulaReflector;
import com.brandtnewtonsoftware.repcheck.models.calculations.formulas.OneRepMaxFormula;

/**
 * Created by Brandt on 8/16/2015.
 */
public class ReflectionTest extends ActivityInstrumentationTestCase2<MainActivity> {

    MainActivity mMainActivity;
    String[] formulas;

    public ReflectionTest(){ super(MainActivity.class); }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mMainActivity = getActivity();
        formulas = mMainActivity.getResources().getStringArray(R.array.one_rep_max_formula_option_values);
    }

    public void testPreconditions() {
        assertNotNull(mMainActivity);
        assertNotNull(formulas);
    }

    public void testReflection() {
        for (String formulaName : formulas) {
            OneRepMaxFormula formula = null;
            try {
                formula =  FormulaReflector.reflectOneRepMaxFormula(formulaName);
            } catch (Exception e) {
                e.printStackTrace();
            }

            assertNotNull("Unable to convert \"" + formulaName + "\"", formula);
        }
    }
}
