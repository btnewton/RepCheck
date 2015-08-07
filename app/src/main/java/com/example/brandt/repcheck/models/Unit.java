package com.example.brandt.repcheck.models;

import android.content.Context;
import android.util.Log;

import com.example.brandt.repcheck.R;

/**
 * Created by Brandt on 7/23/2015.
 */
public class Unit {

    private String unit;
    private final static String LOG_TAG = "Unit";

    private Unit(String unit) {
        this.unit = unit;
    }

    public static Unit ImperialUnit() {
        return new Unit("LB");
    }

    public static Unit MetricUnit() {
        return new Unit("KG");
    }

    public static Unit newUnitByString(String unit, Context context) {
        Unit newUnit;

        if (unit.equals(context.getString(R.string.pref_units_metric))) {
            newUnit = Unit.MetricUnit();
        } else if (unit.equals(context.getString(R.string.pref_units_imperial))) {
            newUnit = Unit.ImperialUnit();
        } else {
            Log.e(LOG_TAG, "Could not match unit: " + unit + " using default.");
            newUnit = Unit.ImperialUnit();
        }

        return newUnit;
    }

    public String getUnit() {
        return unit;
    }
}