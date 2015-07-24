package com.example.brandt.repcheck.models.increments;

import com.example.brandt.repcheck.models.Unit;

/**
 * Created by Brandt on 7/23/2015.
 */
public class IronIncrementSet extends IncrementSet {

    private double[] imperialSet = new double[] {
            2.5,
            5,
            10,
            25,
            35,
            45,
            100
    };

    public IronIncrementSet(Unit unit) {
        super(unit);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public double[] getIncrements() {
        return imperialSet;
    }
}
