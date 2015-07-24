package com.example.brandt.repcheck.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.brandt.repcheck.R;
import com.example.brandt.repcheck.models.Unit;
import com.example.brandt.repcheck.models.calculations.FormulaWrapper;
import com.example.brandt.repcheck.models.increments.IncrementSet;
import com.example.brandt.repcheck.models.increments.IronIncrementSet;
import com.example.brandt.repcheck.util.adapters.WeightListAdapter;

import java.lang.ref.WeakReference;

/**
 * Created by brandt on 7/22/15.
 */
public class MaxRepFragment extends Fragment {

    final int MAX_REPS = 20;
    FormulaWrapper formulaWrapper;
    private EditText weightEditText;
    private Button subtractButton;
    private Button addButton;
    private double incrementValue;
    private WeightListAdapter weightListAdapter;
    private Unit unit;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // TODO select most recent entries.

        int reps = 10;
        double weight = 135;
        incrementValue = 5;

        formulaWrapper = new FormulaWrapper(reps, weight);
        formulaWrapper.setShouldFormat(true);
        formulaWrapper.setBaseWeight(0);
        formulaWrapper.setIsHalfWeight(false);
    }

    @Override
    public void onResume() {
        super.onResume();

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                String unitType = sharedPreferences.getString(getString(R.string.pref_units_key), getString(R.string.pref_units_metric));

                unit = Unit.newUnitByString(unitType, getActivity());
                formulaWrapper.setUnit(unit);
                updateCalculations();
                updateButtons();
            }
        };

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);

        if (unit == null) {
            listener.onSharedPreferenceChanged(sharedPreferences, null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.max_rep, container, false);

        // Weight input
        weightEditText = (EditText) view.findViewById(R.id.weight);
        weightEditText.setText(Double.toString(formulaWrapper.getTotalWeight()));
        weightEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    formulaWrapper.setWeight(Double.parseDouble(weightEditText.getText().toString()));
                    updateCalculations();
                }
            }
        });

        subtractButton = (Button) view.findViewById(R.id.quick_subtract);
        subtractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formulaWrapper.incrementWeight( -incrementValue);
                weightEditText.setText(Double.toString(formulaWrapper.getWeight()));
                updateCalculations();
            }
        });
        subtractButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showIncrementList();
                return true;
            }
        });
        addButton = (Button) view.findViewById(R.id.quick_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formulaWrapper.incrementWeight(incrementValue);
                weightEditText.setText(Double.toString(formulaWrapper.getWeight()));
                updateCalculations();
            }
        });
        addButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showIncrementList();
                return true;
            }
        });

        // Rep input
        Integer[] items = new Integer[MAX_REPS];
        for (int i = 0; i < MAX_REPS; i++) {
            items[i] = i + 1;
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getActivity(), R.layout.big_spinner_item, items);
        Spinner repsSpinner = (Spinner) view.findViewById(R.id.rep_spinner);
        repsSpinner.setAdapter(adapter);
        repsSpinner.setSelection(formulaWrapper.getReps() - 1);

        repsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                formulaWrapper.setReps(position + 1);
                updateCalculations();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        weightListAdapter = new WeightListAdapter(getActivity(), getActivity().getLayoutInflater(), null);

        ListView listView = (ListView) view.findViewById(R.id.list_view);
        listView.setEmptyView(view.findViewById(android.R.id.empty));
        listView.setAdapter(weightListAdapter);

        // Populates list
        updateCalculations();

        return view;
    }

    private void updateCalculations() {
        weightListAdapter.updateData(formulaWrapper.getWeightForRepRangeAsWeightHolderArray(MAX_REPS));
    }

    private void showIncrementList() {
        ChangeIncrementDialog changeIncrementDialog =
                ChangeIncrementDialog.newInstance(new IncrementUpdateHandler(this));
        changeIncrementDialog.show(getFragmentManager(), getTag());
    }

    public static class IncrementUpdateHandler extends Handler {
        private final WeakReference<MaxRepFragment> mActivity;

        public IncrementUpdateHandler(MaxRepFragment maxRepFragment) {
            mActivity = new WeakReference<MaxRepFragment>(maxRepFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MaxRepFragment maxRepFragment = mActivity.get();
            maxRepFragment.updateIncrement(msg.arg1);
        }
    }

    public void updateIncrement(int index) {
        IncrementSet incrementSet = new IronIncrementSet(unit);
        incrementValue = incrementSet.getIncrements()[index];
        updateButtons();
    }

    private void updateButtons() {
        String incrementText = incrementValue + " " + unit.getUnit() + ((incrementValue != 1)? "s" : "");
        subtractButton.setText("-" + incrementText);
        addButton.setText("+" + incrementText);
    }
}
