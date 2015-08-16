package com.brandtnewtonsoftware.repcheck.models.increments;

import android.app.Activity;
import android.util.Log;

import com.brandtnewtonsoftware.repcheck.R;
import com.brandtnewtonsoftware.repcheck.models.Unit;

/**
 * Created by brandt on 8/1/15.
 */
public class IncrementFactory {

    public final static String TAG = "IncrementFactory";

    public static IncrementSet Make(Activity activity, String incrementName, Unit unit) {

        IncrementSet incrementSet;

        if (incrementName.equals(activity.getString(R.string.pref_plate_style_classic))) {
            incrementSet = new ClassicIncrementSet(unit);
        } else if (incrementName.equals(activity.getString(R.string.pref_plate_style_olympic))){
            incrementSet = new OlympicIncrementSet(unit);
        } else {
            Log.w(TAG, "The increment name '" + incrementName + "' was not recognized. Using default.");
            incrementSet = new ClassicIncrementSet(unit);
        }

        return incrementSet;
    }
}
