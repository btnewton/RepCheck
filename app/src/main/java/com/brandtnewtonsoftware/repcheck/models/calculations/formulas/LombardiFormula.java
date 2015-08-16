package com.brandtnewtonsoftware.repcheck.models.calculations.formulas;

/**
 * Created by Brandt on 8/2/2015.
 */
public class LombardiFormula extends OneRepMaxFormula {
    @Override
    protected double calculateMax(int reps, double weight) {
        return weight * Math.pow(reps, 0.1);
    }

    @Override
    protected double calculateWeightForRep(int reps) {
        return max / Math.pow(reps, 0.1);
    }

    @Override
    public FormulaType toEnum() {
        return FormulaType.Lombardi;
    }
}
