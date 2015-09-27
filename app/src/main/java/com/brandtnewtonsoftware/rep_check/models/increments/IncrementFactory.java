package com.brandtnewtonsoftware.rep_check.models.increments;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.brandtnewtonsoftware.rep_check.R;
import com.brandtnewtonsoftware.rep_check.models.Unit;

/**
 * Created by brandt on 8/1/15.
 */
public class IncrementFactory {

    public final static String TAG = "IncrementFactory";

    public static IncrementSet Make(Context context, Unit unit) {

        IncrementSet incrementSet;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String plateStyle = sharedPreferences.getString(context.getString(R.string.pref_plate_style_key), context.getString(R.string.pref_plate_style_classic));

        if (plateStyle.equals(context.getString(R.string.pref_plate_style_classic))) {
            incrementSet = new ClassicIncrementSet(unit);
        } else if (plateStyle.equals(context.getString(R.string.pref_plate_style_olympic))){
            incrementSet = new OlympicIncrementSet(unit);
        } else {
            Log.w(TAG, "The increment name '" + plateStyle + "' was not recognized. Using default.");
            incrementSet = new ClassicIncrementSet(unit);
        }

        return incrementSet;
    }
}
