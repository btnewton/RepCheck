package com.example.brandt.repcheck.models.calculations.formulas;

/**
 * Created by Brandt on 8/2/2015.
 */
public abstract class OneRepMaxFormula {

    protected double max;

    public void update(int reps, double weight) {
        calculateMax(reps, weight);
    }

    protected abstract double calculateMax(int reps, double weight);

    protected abstract double calculateWeightForRep(int reps);

    public double getWeightWeightForReps(int reps) {
        if (reps == 1) {
            return max;
        }

        if (reps < 0) {
            throw new UnsupportedOperationException("Invalid rep count");
        }

        return calculateWeightForRep(reps);
    }
}
