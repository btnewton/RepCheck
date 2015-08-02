package com.example.brandt.repcheck.models.calculations.formulas;

/**
 * Created by Brandt on 8/2/2015.
 */
public class MayhewEtAl extends OneRepMaxFormula {
    @Override
    protected double calculateMax(int reps, double weight) {
        return (100 * weight) / (52.2 + 41.9 * Math.pow(Math.E, -0.055 * reps));
    }

    @Override
    protected double calculateWeightForRep(int reps) {
        return max * (52.2 + 41.9 * Math.pow(Math.E, -0.055 * reps)) / 100;
    }
}
