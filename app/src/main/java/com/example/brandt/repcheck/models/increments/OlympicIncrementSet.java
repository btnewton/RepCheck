package com.example.brandt.repcheck.models.increments;

import com.example.brandt.repcheck.models.Unit;

/**
 * Created by Brandt on 7/23/2015.
 */
public class OlympicIncrementSet extends IncrementSet {

    private double[] imperialSet = new double[] {
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
    public int getCount() {
        return 0;
    }

    @Override
    public double[] getIncrements() {
        return imperialSet;
    }
}
