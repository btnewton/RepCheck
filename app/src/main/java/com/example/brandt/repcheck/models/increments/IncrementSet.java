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
        ArrayList<String>  formattedIncrements = new ArrayList<String>(increments.length);

        for (int i = 0; i < increments.length; i++) {
            formattedIncrements.add(increments[i] + " " + unit.displayUnit(increments[i]));
        }

        return formattedIncrements;
    }

    public abstract int getDefaultWeightIndex();
}
