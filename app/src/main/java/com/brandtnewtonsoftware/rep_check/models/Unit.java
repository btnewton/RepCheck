package com.brandtnewtonsoftware.rep_check.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

    public static Unit newUnitByString(Context context) {
        Unit newUnit;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String unitType = sharedPreferences.getString(context.getString(R.string.pref_units_key), context.getString(R.string.pref_units_default));

        if (unitType.equals(context.getString(R.string.pref_units_metric))) {
            newUnit = Unit.MetricUnit();
        } else if (unitType.equals(context.getString(R.string.pref_units_imperial))) {
            newUnit = Unit.ImperialUnit();
        } else {
            Log.e(LOG_TAG, "Could not match unit: " + unitType + " using default.");
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
