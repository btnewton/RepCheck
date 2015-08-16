package com.example.brandt.repcheck.models.increments;

import com.example.brandt.repcheck.models.Unit;

/**
 * Created by Brandt on 7/23/2015.
 */
public class OlympicIncrementSet extends IncrementSet {

    private double[] olympicSet = new double[] {
            2.5,
            5,
            10,
            22,
            33,
            44,
            55,
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
