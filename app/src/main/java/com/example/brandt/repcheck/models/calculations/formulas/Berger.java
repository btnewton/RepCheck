package com.example.brandt.repcheck.models.calculations.formulas;

/**
 * Created by Brandt on 8/2/2015.
 */
public class Berger extends OneRepMaxFormula {
    @Override
    protected double calculateMax(int reps, double weight) {
        return weight / (1.0261 * Math.pow(Math.E, -0.00262 * reps));
    }

    @Override
    protected double calculateWeightForRep(int reps) {
        return max * (1.0261 * Math.pow(Math.E, -0.00262 * reps));
    }
}
