package com.example.brandt.repcheck.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.brandt.repcheck.R;
import com.example.brandt.repcheck.activities.barconstruction.BarConstructionDialog;
import com.example.brandt.repcheck.activities.saveslots.SaveSetDialog;
import com.example.brandt.repcheck.activities.saveslots.SavedSetsDialog;
import com.example.brandt.repcheck.database.seeders.SetSeeder;
import com.example.brandt.repcheck.models.SetSlot;
import com.example.brandt.repcheck.models.Unit;
import com.example.brandt.repcheck.models.calculations.FormulaWrapper;
import com.example.brandt.repcheck.models.increments.IncrementFactory;
import com.example.brandt.repcheck.models.increments.IncrementSet;
import com.example.brandt.repcheck.util.adapters.WeightListAdapter;

import java.lang.ref.WeakReference;
import java.util.Observable;
import java.util.Observer;

/**
 * Handles all actions for app.
 *
 * Created by brandt on 7/22/15.
 */
public class MaxRepFragment extends Fragment implements Observer {

    FormulaWrapper formulaWrapper;
    private EditText weightEditText;
    private Spinner repsSpinner;
    private Button subtractButton;
    private Button addButton;
    private double incrementValue;
    private IncrementSet incrementSet;
    private WeightListAdapter weightListAdapter;
    private Unit unit;

    @SuppressWarnings("FieldCanBeLocal")
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // Populate table if missing
        if (SetSlot.getSlotCount(getActivity()) != getResources().getInteger(R.integer.set_slot_count)) {
            SetSlot.truncateTable(getActivity());
            new SetSeeder().seed(getActivity());
        }
        SetSlot setSlot = SetSlot.first(getActivity());

        formulaWrapper = new FormulaWrapper(setSlot, getResources().getInteger(R.integer.max_reps));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        loadPreferences(sharedPreferences);

        formulaWrapper.addObserver(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                loadPreferences(sharedPreferences);
            }
        };

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);

        formulaWrapper.calculateSets();
    }

    private void loadPreferences(SharedPreferences sharedPreferences) {
        String unitType = sharedPreferences.getString(getString(R.string.pref_units_key), getString(R.string.pref_units_metric));

        unit = Unit.newUnitByString(unitType, getActivity());
        formulaWrapper.setUnit(unit);

        String plateStyle = sharedPreferences.getString(getString(R.string.pref_plate_style_key), getString(R.string.pref_plate_style_classic));
        incrementSet = IncrementFactory.Make(getActivity(), plateStyle, unit);

        updateIncrement(incrementSet.getDefaultWeightIndex());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.max_rep, container, false);

        // Weight input
        weightEditText = (EditText) view.findViewById(R.id.weight);
        weightEditText.setText(Double.toString(formulaWrapper.getWeight()));
        weightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                double weight;
                try {
                    weight = Double.parseDouble(weightEditText.getText().toString());
                } catch (Exception e) {
                    //
                    weight = -1;
                }

                if (weight >= 0) {
                    formulaWrapper.setWeight(weight);
                } else {
                    // Pseudo-recursive call
                    weightEditText.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        weightEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        // Dismiss keyboard when enter is pressed
        weightEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    View focus = getActivity().getCurrentFocus();

                    if (focus != null) {

                        in.hideSoftInputFromWindow(focus.getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                        // Must return true here to consume event
                        return true;
                    }
                }
                return false;
            }
        });

        subtractButton = (Button) view.findViewById(R.id.quick_subtract);
        subtractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementWeight(-incrementValue);
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
                incrementWeight(incrementValue);
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
        String[] items = new String[getResources().getInteger(R.integer.max_reps)];
        for (int i = 0; i < items.length; i++) {
            items[i] = i + 1 + " reps";
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.big_spinner_item, items);
        repsSpinner = (Spinner) view.findViewById(R.id.rep_spinner);
        repsSpinner.setAdapter(adapter);
        repsSpinner.setSelection(formulaWrapper.getReps() - 1);

        repsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                formulaWrapper.setReps(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        weightListAdapter = new WeightListAdapter(getActivity(), getActivity().getLayoutInflater(), null);

        ListView listView = (ListView) view.findViewById(R.id.list_view);
        listView.setEmptyView(view.findViewById(android.R.id.empty));
        listView.setAdapter(weightListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BarConstructionDialog barConstructionDialog =
                        BarConstructionDialog.newInstance(formulaWrapper.getWeight());
                barConstructionDialog.show(getFragmentManager(), getTag());
            }
        });

        return view;
    }

    public void incrementWeight(double incrementValue) {
        double currentWeight = Double.parseDouble(weightEditText.getText().toString());
        currentWeight += incrementValue;
        weightEditText.setText(Double.toString(currentWeight));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item = menu.add(Menu.NONE, R.id.action_load, 10, R.string.action_load);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(Menu.NONE, R.id.action_save, 10, R.string.action_save);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_load:
                SavedSetsDialog savedSetsDialog =
                        SavedSetsDialog.newInstance(new LoadUpdateHandler(this));
                savedSetsDialog.show(getFragmentManager(), getTag());
                return true;
            case R.id.action_save:
                SaveSetDialog saveSetDialog =
                        SaveSetDialog.newInstance(formulaWrapper.getReps(), formulaWrapper.getWeight());
                saveSetDialog.show(getFragmentManager(), getTag());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        formulaWrapper.calculateSets();
    }

    private void showIncrementList() {
        ChangeIncrementDialog changeIncrementDialog =
                ChangeIncrementDialog.newInstance(new IncrementUpdateHandler(this));
        changeIncrementDialog.show(getFragmentManager(), getTag());
    }

    @Override
    public void update(Observable observable, Object o) {
        weightListAdapter.updateData(formulaWrapper.getSets());
    }

    public static class IncrementUpdateHandler extends Handler {
        private final WeakReference<MaxRepFragment> mActivity;

        public IncrementUpdateHandler(MaxRepFragment maxRepFragment) {
            mActivity = new WeakReference<>(maxRepFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MaxRepFragment maxRepFragment = mActivity.get();
            maxRepFragment.updateIncrement(msg.arg1);
        }
    }

    public static class LoadUpdateHandler extends Handler {
        private final WeakReference<MaxRepFragment> mActivity;

        public LoadUpdateHandler(MaxRepFragment maxRepFragment) {
            mActivity = new WeakReference<>(maxRepFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MaxRepFragment maxRepFragment = mActivity.get();
            maxRepFragment.loadSet(msg.arg1);
        }
    }

    public FormulaWrapper getFormulaWrapper() {
        return formulaWrapper;
    }

    public double getIncrementValue() {
        return incrementValue;
    }

    public void loadSet(int id) {
        SetSlot set = SetSlot.findById(getActivity(), id);
        repsSpinner.setSelection(set.getReps() - 1);
        weightEditText.setText(Double.toString(set.getWeight()));
    }

    public void updateIncrement(int index) {
        incrementValue = incrementSet.getIncrements()[index];

        // Update buttons
        if (subtractButton != null && addButton != null) {
            String incrementText = incrementValue + " " + unit.getUnit() + ((incrementValue != 1) ? "s" : "");
            subtractButton.setText("-" + incrementText);
            addButton.setText("+" + incrementText);
        }
    }
}
