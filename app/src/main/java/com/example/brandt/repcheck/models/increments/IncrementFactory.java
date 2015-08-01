package com.example.brandt.repcheck.models.increments;

import android.app.Activity;

import com.example.brandt.repcheck.R;
import com.example.brandt.repcheck.models.Unit;

/**
 * Created by brandt on 8/1/15.
 */
public class IncrementFactory {

    public static IncrementSet Make(Activity activity, String incrementName, Unit unit) {

        IncrementSet incrementSet;

        if (incrementName.equals(activity.getString(R.string.pref_plate_style_classic))) {
            incrementSet = new ClassicIncrementSet(unit);
        } else {
            incrementSet = new OlympicIncrementSet(unit);
        }

        return incrementSet;
    }
}
