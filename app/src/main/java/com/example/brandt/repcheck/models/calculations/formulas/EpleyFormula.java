package com.example.brandt.repcheck.models.calculations.formulas;

/**
 * Created by Brandt on 8/2/2015.
 */
public class EpleyFormula extends OneRepMaxFormula {

    @Override
    protected double calculateMax(int reps, double weight) {
        return weight * (1 + (reps / 30));
    }

    @Override
    protected double calculateWeightForRep(int reps) {
        return max / (1 + (reps / 30));
    }

    @Override
    public FormulaType toEnum() {
        return FormulaType.Epley;
    }
}
