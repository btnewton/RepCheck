package com.example.brandt.repcheck.models.calculations.formulas;

/**
 * Brzycki
 * Created by Brandt on 7/23/2015.
 */
public class Brzycki extends OneRepMaxFormula {

    @Override
    protected double calculateMax(int reps, double weight) {
        return weight * 36 / (37 - reps);
    }

    @Override
    protected double calculateWeightForRep(int reps) {
        return max * (37 - reps) / 36;
    }
}
