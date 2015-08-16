package com.brandtnewtonsoftware.repcheck.models;

import android.content.Context;

/**
 * Created by Brandt on 8/2/2015.
 */
public class RepFormulaConfigurationHolder {

    private FormulaConfiguration[] formulaConfigurations;
    private int reps;

    public RepFormulaConfigurationHolder(int reps) {
        this.reps = reps;
    }

    public void populate(Context context) {
        formulaConfigurations = FormulaConfiguration.getAllForReps(context, reps);
    }
}
