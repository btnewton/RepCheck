package com.example.brandt.repcheck.models.calculations.formulas;

/**
 * Created by Brandt on 8/2/2015.
 */
public class Lander extends OneRepMaxFormula {
    @Override
    protected double calculateMax(int reps, double weight) {
        return (100 * weight) / (101.3 - 2.67123 * reps);
    }

    @Override
    protected double calculateWeightForRep(int reps) {
        return max * (101.3 - 2.67123 * reps) / 100;
    }
}
