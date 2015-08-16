package com.brandtnewtonsoftware.repcheck.models.calculations.formulas;

/**
 * Created by Brandt on 8/2/2015.
 */
public class EpleyFormula extends OneRepMaxFormula {

    @Override
    protected double calculateMax(int reps, double weight) {
        return (0.033 * reps * weight) + weight;
    }

    @Override
    protected double calculateWeightForRep(int reps) {
        return max /(0.033 * reps + 1);
    }

    @Override
    public FormulaType toEnum() {
        return FormulaType.Epley;
    }
}
