package com.brandtnewtonsoftware.rep_check.models.calculations.formulas;

/**
 * Created by Brandt on 8/2/2015.
 */
public class MayhewEtAlFormula extends OneRepMaxFormula {
    @Override
    protected double calculateMax(int reps, double weight) {
        return (100 * weight) / (52.2 + 41.9 * Math.pow(Math.E, -0.055 * reps));
    }

    @Override
    protected double calculateWeightForRep(int reps) {
        return max * (52.2 + 41.9 * Math.pow(Math.E, -0.055 * reps)) / 100;
    }

    @Override
    public FormulaType toEnum() {
        return FormulaType.MayhewEtAl;
    }

    @Override
    public String toString() {
        return "Mayhew et al";
    }
}
