package com.brandtnewtonsoftware.repcheck.models.calculations.formulas;

/**
 * Source: https://www.inetsolutions.com.au/workhab/help/Equations.htm
 * Created by Brandt on 8/2/2015.
 */
public class BergerFormula extends OneRepMaxFormula {
    @Override
    protected double calculateMax(int reps, double weight) {
        return weight * (0.034 * reps + 0.966);
    }

    @Override
    protected double calculateWeightForRep(int reps) {
        return max /(0.034 * reps + 0.966);
    }

    @Override
    public FormulaType toEnum() {
        return FormulaType.Berger;
    }
}
