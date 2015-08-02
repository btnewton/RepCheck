package com.example.brandt.repcheck.activities.barconstruction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.brandt.repcheck.R;
import com.example.brandt.repcheck.models.Unit;
import com.example.brandt.repcheck.models.increments.IncrementFactory;
import com.example.brandt.repcheck.models.increments.IncrementSet;
import com.example.brandt.repcheck.util.adapters.WeightHolder;
import com.example.brandt.repcheck.util.adapters.WeightListAdapter;

/**
 * Created by Brandt on 8/1/2015.
 */
public class BarConstructionDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private IncrementSet incrementSet;
    private static final String WEIGHT_KEY = "weight";
    private double weight;
    private Unit unit;

    public static BarConstructionDialog newInstance(double weight) {
        BarConstructionDialog fragment = new BarConstructionDialog();

        Bundle args = new Bundle();
        args.putDouble(WEIGHT_KEY, weight);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            weight = getArguments().getDouble(WEIGHT_KEY);
        } else {
            dismiss();
        }

        Activity activity = getActivity();

        // Load unit and plate style
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String plateStyle = sharedPreferences.getString(getString(R.string.pref_plate_style_key), getString(R.string.pref_plate_style_classic));
        String unitType = sharedPreferences.getString(getString(R.string.pref_units_key), getString(R.string.pref_units_metric));
        unit = Unit.newUnitByString(unitType, activity);

        incrementSet = IncrementFactory.Make(activity, plateStyle, unit);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        WeightListAdapter adapter = new WeightListAdapter(getActivity(), getActivity().getLayoutInflater(), getBarConstruction());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Set Slot Interval");
        builder.setAdapter(adapter, this);

        return builder.create();
    }

    private WeightHolder[] getBarConstruction() {

        double[] increments = incrementSet.getIncrements();
        WeightHolder[] weightHolders = new WeightHolder[increments.length + 1];

        for (int i = 0; i < increments.length; i++) {
            double plateWeight = increments[increments.length - i - 1];

            int plateCount = (int) weight / (int) plateWeight;
            weight -= plateCount * plateWeight;
            weightHolders[i] = new WeightHolder(Double.toString(plateWeight), Integer.toString(plateCount));
        }

        weightHolders[weightHolders.length - 1] = new WeightHolder("Remainder", Double.toString(weight));

        return weightHolders;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
