package com.brandtnewtonsoftware.rep_check.activities.barload;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.brandtnewtonsoftware.rep_check.R;
import com.brandtnewtonsoftware.rep_check.models.Unit;
import com.brandtnewtonsoftware.rep_check.models.WeightFormatter;
import com.brandtnewtonsoftware.rep_check.models.increments.IncrementFactory;
import com.brandtnewtonsoftware.rep_check.models.increments.IncrementSet;
import com.brandtnewtonsoftware.rep_check.util.adapters.DividerItemDecoration;
import com.brandtnewtonsoftware.rep_check.util.adapters.NestableLinearLayoutManager;
import com.brandtnewtonsoftware.rep_check.util.adapters.standard.IStandardRowItem;
import com.brandtnewtonsoftware.rep_check.util.adapters.standard.StandardRowAdapter;
import com.brandtnewtonsoftware.rep_check.util.adapters.standard.StandardRowItem;

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
    private WeightFormatter weightFormatter;
    private double remainder;
    private StandardRowAdapter adapter;

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
            return;
        }

        // Load unit and plate style
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        try {
            barWeight = Double.parseDouble(sharedPreferences.getString(getString(R.string.pref_bar_weight_key), "45"));
        } catch (Exception e) {
            Log.e("BarLoadDialog", "Unable to convert bar weight. Set pref to 45");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.pref_bar_weight_key), "45");
            editor.apply();
            barWeight = 45;
        }
        String unitType = sharedPreferences.getString(getString(R.string.pref_units_key), getString(R.string.pref_units_metric));
        Unit unit = Unit.newUnitByString(unitType, getActivity());
        weightFormatter = new WeightFormatter(sharedPreferences.getBoolean(getString(R.string.pref_round_values_key), true), unit);
        String plateStyle = sharedPreferences.getString(getString(R.string.pref_plate_style_key), getString(R.string.pref_plate_style_classic));
        incrementSet = IncrementFactory.Make(getActivity(), plateStyle, unit);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_bar_load, null);
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                TextView totalWeight = (TextView) view.findViewById(R.id.total_weight);
                totalWeight.setText(weightFormatter.format(weight) + " " + weightFormatter.displayUnit(weight));

                TextView barWeightTextView = (TextView) view.findViewById(R.id.bar_weight);
                barWeightTextView.setText(weightFormatter.format(barWeight) + " " + weightFormatter.displayUnit(barWeight));
                
                adapter = StandardRowAdapter.newBarLoadAdapter(getBarConstruction());
                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.plate_list);
                recyclerView.setLayoutManager(new NestableLinearLayoutManager(getContext()));
                recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL_LIST));
                recyclerView.setAdapter(adapter);

                TextView remainderTextView = (TextView) view.findViewById(R.id.weight_remainder);
                remainderTextView.setText(weightFormatter.format(remainder) + ((remainder > 0) ? " " + weightFormatter.displayUnit(remainder) : ""));

                Button closeButton = (Button) view.findViewById(R.id.close_btn);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        return dialog;
    }

    private List<IStandardRowItem> getBarConstruction() {

        double[] increments = incrementSet.getIncrements();
        List<IStandardRowItem> weightHolders = new ArrayList<>(increments.length + 1);

        double weight = this.weight - barWeight;

        NumberFormat formatter = new DecimalFormat("0.#");

        for (int i = 0; i < increments.length; i++) {
            double plateWeight = increments[increments.length - i - 1];

            int plateCount = (int) (weight / (2 * plateWeight));

            if (plateCount > 0) {
                weight -= (2 * plateCount) * plateWeight;
                weightHolders.add(new StandardRowItem(formatter.format(plateWeight) + "'s", Integer.toString(2 * plateCount)));
            }
        }

        remainder = weight;

        return weightHolders;
    }
}
