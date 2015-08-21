package com.brandtnewtonsoftware.rep_check.models.increments;

import com.brandtnewtonsoftware.rep_check.models.Unit;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by Brandt on 7/23/2015.
 */
public abstract class IncrementSet {

    protected final Unit unit;

    public IncrementSet(Unit unit) {
        this.unit = unit;
    }

    public double[] getIncrements(Unit unit) {
        if (unit.equals(Unit.MetricUnit())) {
            return getMetricIncrements();
        } else {
            return getImperialIncrements();
        }
    }

    protected abstract double[] getImperialIncrements();
    protected abstract double[] getMetricIncrements();

    public ArrayList<String> getIncrementsAsStringArray() {
        double[] increments = getIncrements(unit);
        ArrayList<String>  formattedIncrements = new ArrayList<>(increments.length);
        NumberFormat formatter = new DecimalFormat("#.#");
        for (double increment : increments) {
            formattedIncrements.add(formatter.format(increment) + " " + unit.displayUnit(increment));
        }

        return formattedIncrements;
    }

    public abstract int getDefaultWeightIndex();
}
