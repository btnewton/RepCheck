package com.brandtnewtonsoftware.repcheck.models.calculations.formulas;

/**
 * BrownFormula et al
 * Created by Brandt on 8/2/2015.
 */
public class BrownFormula extends OneRepMaxFormula {
    @Override
    protected double calculateMax(int reps, double weight) {
        return (reps * 0.0328 + 0.9849) * weight;
    }

    @Override
    protected double calculateWeightForRep(int reps) {
        return max / (reps * 0.0328 + 0.9849);
    }

    @Override
    public FormulaType toEnum() {
        return FormulaType.Brown;
    }
}
