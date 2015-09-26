package com.brandtnewtonsoftware.rep_check.models;

import android.content.Context;
import android.util.Log;

import com.brandtnewtonsoftware.rep_check.R;


/**
 * Created by Brandt on 7/23/2015.
 */
public class Unit {

    private final String unit;
    private final static String LOG_TAG = "Unit";

    private Unit(String unit) {
        this.unit = unit;
    }

    public static Unit ImperialUnit() {
        return new Unit("lb");
    }

    public static Unit MetricUnit() {
        return new Unit("kg");
    }

    public static Unit newUnitByString(String unitName, Context context) {
        Unit newUnit;

        if (unitName.equals(context.getString(R.string.pref_units_metric))) {
            newUnit = Unit.MetricUnit();
        } else if (unitName.equals(context.getString(R.string.pref_units_imperial))) {
            newUnit = Unit.ImperialUnit();
        } else {
            Log.e(LOG_TAG, "Could not match unit: " + unitName + " using default.");
            newUnit = Unit.ImperialUnit();
        }

        return newUnit;
    }

    protected String getUnit() {
        return unit;
    }

    public boolean equals(String unit) {
        return this.unit.equals(unit);
    }
    public boolean equals(Unit unit) {
        return equals(unit.getUnit());
    }

    public String displayUnit() {
        return displayUnit(1);
    }

    public String displayUnit(double weight) {
        return unit + ((weight != 1)? "s" : "");
    }
}