package com.brandtnewtonsoftware.repcheck.models.increments;

import com.brandtnewtonsoftware.repcheck.models.Unit;

/**
 * Created by Brandt on 7/23/2015.
 */
public class ClassicIncrementSet extends IncrementSet {

    private double[] imperialSet = new double[] {
            2.5,
            5,
            10,
            25,
            35,
            45
    };

    private double[] metricSet = new double[] {
            1.25,
            2.5,
            5,
            10,
            15,
            20,
            25
    };

    public ClassicIncrementSet(Unit unit) {
        super(unit);
    }

    @Override
    protected double[] getImperialIncrements() {
        return imperialSet;
    }

    @Override
    protected double[] getMetricIncrements() {
        return metricSet;
    }

    @Override
    public int getDefaultWeightIndex() {
        return 2;
    }
}
