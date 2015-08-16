package com.brandtnewtonsoftware.repcheck.models.calculations.formulas;

/**
 * BrzyckiFormula
 * Created by Brandt on 7/23/2015.
 */
public class BrzyckiFormula extends OneRepMaxFormula {

    @Override
    protected double calculateMax(int reps, double weight) {
        return weight * 36 / (37 - reps);
    }

    @Override
    protected double calculateWeightForRep(int reps) {
        return max * (37 - reps) / 36;
    }

    @Override
    public FormulaType toEnum() {
        return FormulaType.Brzycki;
    }
}
