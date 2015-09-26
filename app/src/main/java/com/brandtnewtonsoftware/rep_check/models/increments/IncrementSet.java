package com.brandtnewtonsoftware.rep_check.models.increments;

import com.brandtnewtonsoftware.rep_check.models.Unit;
import com.brandtnewtonsoftware.rep_check.util.adapters.standard.IStandardRowItem;
import com.brandtnewtonsoftware.rep_check.util.adapters.standard.StandardRowItem;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brandt on 7/23/2015.
 */
public abstract class IncrementSet {

    protected final Unit unit;

    public IncrementSet(Unit unit) {
        this.unit = unit;
    }

    public double[] getIncrements() {
        if (unit.equals(Unit.MetricUnit())) {
            return getMetricIncrements();
        } else {
            return getImperialIncrements();
        }
    }

    protected abstract double[] getImperialIncrements();
    protected abstract double[] getMetricIncrements();

    public List<IStandardRowItem> getIncrementsAsStringArray() {
        double[] increments = getIncrements();
        List<IStandardRowItem>  formattedIncrements = new ArrayList<>(increments.length);
        NumberFormat formatter = new DecimalFormat("#.#");
        for (double increment : increments) {
            formattedIncrements.add(new StandardRowItem(formatter.format(increment) + " " + unit.displayUnit(increment), ""));
        }

        return formattedIncrements;
    }

    public abstract int getDefaultWeightIndex();
}
