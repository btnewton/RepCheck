package com.example.brandt.repcheck.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.ArrayAdapter;

import com.example.brandt.repcheck.R;
import com.example.brandt.repcheck.models.Unit;
import com.example.brandt.repcheck.models.increments.IncrementFactory;

/**
 * Created by Brandt on 7/23/2015.
 */
public class ChangeIncrementDialog extends DialogFragment {

    private static Handler updateHandler;

    public static ChangeIncrementDialog newInstance(Handler updateHandler) {
        ChangeIncrementDialog fragment = new ChangeIncrementDialog();
        ChangeIncrementDialog.updateHandler = updateHandler;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity activity = getActivity();

        // Load unit and plate style
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String plateStyle = sharedPreferences.getString(getString(R.string.pref_plate_style_key), getString(R.string.pref_plate_style_classic));
        String unitType = sharedPreferences.getString(getString(R.string.pref_units_key), getString(R.string.pref_units_metric));
        Unit unit = Unit.newUnitByString(unitType, activity);

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, IncrementFactory.Make(activity, plateStyle, unit).getIncrementsAsStringArray());

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Set Slot Interval")
                .setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Message msg = new Message();
                        msg.arg1 = which;
                        updateHandler.sendMessage(msg);
                        dismiss();
                    }
                });

        return builder.create();
    }
}