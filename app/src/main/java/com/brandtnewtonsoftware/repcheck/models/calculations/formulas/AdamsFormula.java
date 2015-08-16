package com.brandtnewtonsoftware.repcheck.models.calculations.formulas;

/**
 * Created by Brandt on 8/2/2015.
 */
public class AdamsFormula extends OneRepMaxFormula {
    @Override
    protected double calculateMax(int reps, double weight) {
        return weight / (1 - 0.02 * reps);
    }

    @Override
    protected double calculateWeightForRep(int reps) {
        return max * (1 - 0.02 * reps);
    }

    @Override
    public FormulaType toEnum() {
        return FormulaType.Adams;
    }
}
