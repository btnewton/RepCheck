package com.example.brandt.repcheck.models.calculations.formulas;

/**
 * Kemmler et al
 * Created by Brandt on 8/2/2015.
 */
public class KemmlerEtAl extends OneRepMaxFormula {
    @Override
    protected double calculateMax(int reps, double weight) {
        return weight * (0.988 - (0.0000584 * Math.pow(reps, 3)
                + 0.00190 * Math.pow(reps, 2) + 0.0104 * reps));
    }

    @Override
    protected double calculateWeightForRep(int reps) {
        return max / (0.988 - (0.0000584 * Math.pow(reps, 3)
                + 0.00190 * Math.pow(reps, 2) + 0.0104 * reps));
    }
}
