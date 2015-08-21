package com.brandtnewtonsoftware.rep_check.models.increments;

import com.brandtnewtonsoftware.rep_check.models.Unit;

/**
 * Created by Brandt on 7/23/2015.
 */
public final class OlympicIncrementSet extends IncrementSet {

    private final double[] olympicSet = new double[] {
            2.5,
            5,
            10,
            22,
            33,
            44,
            55,
    };

    private final double[] metricSet = new double[] {
            1.25,
            2.5,
            5,
            10,
            15,
            20,
            25
    };

    public OlympicIncrementSet(Unit unit) {
        super(unit);
    }

    @Override
    protected double[] getImperialIncrements() {
        return olympicSet;
    }

    @Override
    protected double[] getMetricIncrements() {
        return metricSet;
    }

    @Override
    public int getDefaultWeightIndex() {
        return 1;
    }
}
