package com.example.brandt.repcheck.models.increments;

import com.example.brandt.repcheck.models.Unit;

import java.util.ArrayList;

/**
 * Created by Brandt on 7/23/2015.
 */
public abstract class IncrementSet {

    protected Unit unit;

    public IncrementSet(Unit unit) {
        this.unit = unit;
    }

    public abstract double[] getIncrements();

    public ArrayList<String> getIncrementsAsStringArray() {
        double[] increments = getIncrements();
        ArrayList<String>  formattedIncrements = new ArrayList<>(increments.length);

        for (double increment : increments) {
            formattedIncrements.add(increment + " " + unit.displayUnit(increment));
        }

        return formattedIncrements;
    }

    public abstract int getDefaultWeightIndex();
}
