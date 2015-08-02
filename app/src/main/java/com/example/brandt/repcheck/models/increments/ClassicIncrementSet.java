package com.example.brandt.repcheck.models.increments;

import com.example.brandt.repcheck.models.Unit;

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

    public ClassicIncrementSet(Unit unit) {
        super(unit);
    }

    @Override
    public double[] getIncrements() {
        return imperialSet;
    }

    @Override
    public int getDefaultWeightIndex() {
        return 2;
    }
}
