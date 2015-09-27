package com.brandtnewtonsoftware.rep_check.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.brandtnewtonsoftware.rep_check.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Brandt on 8/7/2015.
 */
public final class WeightFormatter {

    private final NumberFormat formatter;
    private final Unit unit;

    public WeightFormatter(Context context) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean roundCalculations = sharedPreferences.getBoolean(context.getString(R.string.pref_round_values_key), context.getResources().getBoolean(R.bool.pref_round_values_default));

        if (roundCalculations) {
            formatter = new DecimalFormat("#0");
        } else {
            formatter = new DecimalFormat("#0.0");
        }

        unit = Unit.newUnitByString(context);
    }

    public String format(double number) {
        return formatter.format(number);
    }

    public Unit getUnit() { return unit; }
    public String displayUnit(double number) { return unit.displayUnit(number); }
}
