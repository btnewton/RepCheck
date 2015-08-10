package com.example.brandt.repcheck.activities.barload;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.brandt.repcheck.R;
import com.example.brandt.repcheck.models.Unit;
import com.example.brandt.repcheck.models.increments.IncrementFactory;
import com.example.brandt.repcheck.models.increments.IncrementSet;
import com.example.brandt.repcheck.util.adapters.standard.IStandardRowItem;
import com.example.brandt.repcheck.util.adapters.standard.StandardRowItem;
import com.example.brandt.repcheck.util.adapters.standard.StandardRowListAdapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brandt on 8/1/2015.
 */
public class BarLoadDialog extends DialogFragment {

    private IncrementSet incrementSet;
    private static final String WEIGHT_KEY = "weight";
    private double weight;
    private double barWeight;
    private Unit unit;
    private double remainder;

    public static BarLoadDialog newInstance(double weight) {
        BarLoadDialog fragment = new BarLoadDialog();

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
        try {
            barWeight = Double.parseDouble(sharedPreferences.getString(getString(R.string.pref_bar_weight_key), "45"));
        } catch (Exception e) {
            Log.e("BarLoadDialog", "Unable to convert bar weight. Set pref to 45");
            sharedPreferences.edit().putString(getString(R.string.pref_bar_weight_key), "45");
            barWeight = 45;
        }
        String plateStyle = sharedPreferences.getString(getString(R.string.pref_plate_style_key), getString(R.string.pref_plate_style_classic));
        String unitType = sharedPreferences.getString(getString(R.string.pref_units_key), getString(R.string.pref_units_metric));
        unit = Unit.newUnitByString(unitType, activity);
        incrementSet = IncrementFactory.Make(activity, plateStyle, unit);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View aboutView = inflater.inflate(R.layout.bar_load, null);
        builder.setView(aboutView);
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                NumberFormat formatter = new DecimalFormat("0.#");

                TextView barWeightTextView = (TextView) aboutView.findViewById(R.id.bar_weight);
                barWeightTextView.setText(formatter.format(barWeight) + " " + unit.displayUnit(barWeight));

                TextView remainderTextView = (TextView) aboutView.findViewById(R.id.weight_remainder);
                remainderTextView.setText(formatter.format(remainder) + ((remainder > 0)? " " + unit.displayUnit(remainder) : ""));

                StandardRowListAdapter adapter = StandardRowListAdapter.newBarLoadAdapter(getActivity(), getActivity().getLayoutInflater());
                adapter.updateData(getBarConstruction());

                ListView listView = (ListView) aboutView.findViewById(R.id.plate_list);
                listView.setAdapter(adapter);
            }
        });

        return dialog;
    }

    private List<IStandardRowItem> getBarConstruction() {

        double[] increments = incrementSet.getIncrements();
        List<IStandardRowItem> weightHolders = new ArrayList<>(increments.length + 1);

        weight -= barWeight;

        NumberFormat formatter = new DecimalFormat("0.#");

        for (int i = 0; i < increments.length; i++) {
            double plateWeight = increments[increments.length - i - 1];

            int plateCount = (int) weight / (int) (2 * plateWeight);

            if (plateCount > 0) {
                weight -= (2 * plateCount) * plateWeight;
                weightHolders.add(new StandardRowItem(0, formatter.format(plateWeight), Integer.toString(2 * plateCount)));
            }
        }

        remainder = weight;

        return weightHolders;
    }
}
