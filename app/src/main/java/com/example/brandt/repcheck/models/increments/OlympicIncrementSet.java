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

    public OlympicIncrementSet(Unit unit) {
        super(unit);
    }

    @Override
    public double[] getIncrements() {
        return olympicSet;
    }

    @Override
    public int getDefaultWeightIndex() {
        return 1;
    }
}
