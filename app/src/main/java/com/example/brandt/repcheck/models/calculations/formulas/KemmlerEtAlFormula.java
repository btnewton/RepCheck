package com.example.brandt.repcheck.models.calculations.formulas;

/**
 * Kemmler et al
 * source: http://www.unm.edu/~rrobergs/478PredictionAccuracy.pdf
 * Created by Brandt on 8/2/2015.
 */
public class KemmlerEtAlFormula extends OneRepMaxFormula {
    @Override
    protected double calculateMax(int reps, double weight) {
        return weight * (0.988  + 0.0104 * reps  + 0.0019 * Math.pow(reps, 2)- 0.0000584 * Math.pow(reps, 3));
    }

    @Override
    protected double calculateWeightForRep(int reps) {
        return max / (0.988  + 0.0104 * reps  + 0.0019 * Math.pow(reps, 2)- 0.0000584 * Math.pow(reps, 3));
    }

    @Override
    public FormulaType toEnum() {
        return FormulaType.KemmlerEtAl;
    }

    @Override
    public String toString() {
        return "Kemmler et al";
    }
}
