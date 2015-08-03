package com.example.brandt.repcheck.models.calculations.formulas;

/**
 * O'Conner et al
 * Created by Brandt on 8/2/2015.
 */
public class OConnerEtAlFormula extends OneRepMaxFormula {
    @Override
    protected double calculateMax(int reps, double weight) {
        return weight * (1 + 0.025 * reps);
    }

    @Override
    protected double calculateWeightForRep(int reps) {
        return max / (1 + 0.025 * reps);
    }

    @Override
    public FormulaType toEnum() {
        return FormulaType.OConnerEtAl;
    }

    @Override
    public String toString() {
        return "O'Conner et al";
    }
}
