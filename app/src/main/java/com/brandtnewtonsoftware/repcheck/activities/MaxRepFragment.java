package com.brandtnewtonsoftware.repcheck.activities;

import android.animation.Animator;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.brandtnewtonsoftware.repcheck.R;
import com.brandtnewtonsoftware.repcheck.activities.barload.BarLoadDialog;
import com.brandtnewtonsoftware.repcheck.activities.saveslots.LoadSetDialog;
import com.brandtnewtonsoftware.repcheck.activities.saveslots.SaveSetDialog;
import com.brandtnewtonsoftware.repcheck.activities.saveslots.SetsListDialog;
import com.brandtnewtonsoftware.repcheck.models.SetSlot;
import com.brandtnewtonsoftware.repcheck.models.Unit;
import com.brandtnewtonsoftware.repcheck.models.WeightFormatter;
import com.brandtnewtonsoftware.repcheck.models.calculations.FormulaReflector;
import com.brandtnewtonsoftware.repcheck.models.calculations.formulas.BrzyckiFormula;
import com.brandtnewtonsoftware.repcheck.models.calculations.formulas.OneRepMaxFormula;
import com.brandtnewtonsoftware.repcheck.models.increments.IncrementFactory;
import com.brandtnewtonsoftware.repcheck.models.increments.IncrementSet;
import com.brandtnewtonsoftware.repcheck.util.UndoBarController;
import com.brandtnewtonsoftware.repcheck.util.adapters.detail.DetailRow;
import com.brandtnewtonsoftware.repcheck.util.adapters.detail.DetailRowListAdapter;
import com.brandtnewtonsoftware.repcheck.util.adapters.detail.IDetailRow;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Handles all actions for app.
 * <p/>
 * Created by brandt on 7/22/15.
 */
public class MaxRepFragment extends Fragment implements Observer, UndoBarController.UndoListener, View.OnLongClickListener {

    private static final String SET_ID = "setID";
    private static final String STATE_WEIGHT = "stateWeight";
    private static final String STATE_REPS = "stateReps";

    private static final String LOG_KEY = "MaxRepFragment";
    private int fabYTranslationLength;
    // Models
    public IncrementSet incrementSet;
    public WeightFormatter weightFormatter;
    public SetSlot setSlot;
    public OneRepMaxFormula formula;
    private static Handler handler;
    private AsyncCalculate asyncCalculate;
    // UI
    private DetailRowListAdapter weightListAdapter;
    private View floatingActionButton;
    private TextView setNameTextView;
    private EditText weightEditText;
    private Spinner repsSpinner;
    private Button subtractButton;
    private Button addButton;
    private UndoBarController mUndoBarController;
    @SuppressWarnings("FieldCanBeLocal")
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    private double incrementValue;
    private double barWeight;
    private Thread calculateThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            int setID = savedInstanceState.getInt(SET_ID);
            loadSet(setID);
            updateSet();

            // Update set with current information
            setSlot.setWeight(savedInstanceState.getInt(STATE_WEIGHT));
            setSlot.setReps(savedInstanceState.getInt(STATE_REPS));
        } else {
            setSlot = SetSlot.first(getActivity());
        }

        fabYTranslationLength = (int) (getResources().getDimension(R.dimen.undobar_height) + getResources().getDimension(R.dimen.standard));

        handler = new Handler();
        asyncCalculate = new AsyncCalculate();
        asyncCalculate.addObserver(this);

        // Update preferences
        loadPreferences();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPreferences();
    }

    private void loadPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        barWeight = Double.parseDouble(sharedPreferences.getString(getString(R.string.pref_bar_weight_key), "45"));

        // Get & apply unit type
        String unitType = sharedPreferences.getString(getString(R.string.pref_units_key), getString(R.string.pref_units_metric));
        Unit unit = Unit.newUnitByString(unitType, getActivity());
        boolean roundCalculations = sharedPreferences.getBoolean(getString(R.string.pref_round_values_key), true);
        weightFormatter = new WeightFormatter(roundCalculations, unit);

        String formulaName = sharedPreferences.getString(getString(R.string.pref_formula_key), getString(R.string.brzycki_formula_value));

        // Reflect formula or default to Brzycki
        try {
            formula = FormulaReflector.reflectOneRepMaxFormula(formulaName);
        } catch (Exception e) {
            Log.e(LOG_KEY, "Unable to convert \"" + formulaName + "\"! defaulting to Brzycki. " + e.getMessage());
            Log.e(LOG_KEY, BrzyckiFormula.class.getName());
            formula = new BrzyckiFormula();
        }

        String plateStyle = sharedPreferences.getString(getString(R.string.pref_plate_style_key), getString(R.string.pref_plate_style_classic));
        incrementSet = IncrementFactory.Make(getActivity(), plateStyle, unit);

        // Update display
        startCalculateSets();
        updateQuickPlateButtons(incrementSet.getDefaultWeightIndex());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            view = inflater.inflate(R.layout.max_rep_portrait, container, false);
        } else {
            view = inflater.inflate(R.layout.max_rep_landscape, container, false);
        }

        mUndoBarController = new UndoBarController(view.findViewById(R.id.undobar), this);

        setNameTextView = (TextView) view.findViewById(R.id.set_name);
        setNameTextView.setText(setSlot.getName());
        setNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSaveSetDialog();
            }
        });

        // Weight input
        weightEditText = (EditText) view.findViewById(R.id.detail);
        weightEditText.setText(Double.toString(setSlot.getWeight()));
        weightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateWeightFromInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        weightEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
        // Dismiss keyboard when enter is pressed
        weightEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (EditorInfo.IME_ACTION_DONE == actionId) {
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    View focus = getActivity().getCurrentFocus();

                    if (focus != null) {
                        in.hideSoftInputFromWindow(focus.getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    return true;
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

        floatingActionButton = view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (setSlot.getWeight() > 0 && setSlot.hasChanged()) {
                    floatingActionButton.animate().translationY(-fabYTranslationLength).setDuration(getActivity().getResources().getInteger(android.R.integer.config_shortAnimTime)).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mUndoBarController.showUndoBar(
                                    false,
                                    getString(R.string.undobar_sample_message),
                                    null);
                            floatingActionButton.animate().setListener(null);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });

                    setSlot.saveChanges(getActivity());
                    setNameTextView.setTypeface(null, Typeface.NORMAL);
                } else {
                    cannotSaveToast();
                }
            }
        });
        floatingActionButton.setOnLongClickListener(this);

        // Rep input
        String[] items = new String[getResources().getInteger(R.integer.max_reps)];
        for (int i = 0; i < items.length; i++) {
            items[i] = i + 1 + "  reps";
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.big_spinner_item, items);
        repsSpinner = (Spinner) view.findViewById(R.id.rep_spinner);
        repsSpinner.setAdapter(adapter);
        repsSpinner.setSelection(setSlot.getReps() - 1);
        repsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateRepsFromInput();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        weightListAdapter = DetailRowListAdapter.newSetListAdapter(getActivity(), getActivity().getLayoutInflater());

        ListView listView = (ListView) view.findViewById(R.id.list_view);
        listView.setEmptyView(view.findViewById(android.R.id.empty));
        listView.setAdapter(weightListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                double weight = formula.getWeightWeightForReps(position + 1);

                if (weight >= barWeight) {
                    BarLoadDialog barLoadDialog =
                            BarLoadDialog.newInstance(weight);
                    barLoadDialog.show(getFragmentManager(), getTag());
                } else {
                    Toast.makeText(getActivity(), "Weight cannot be less than the bar.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Update UI
        updateQuickPlateButtons(incrementSet.getDefaultWeightIndex());

        return view;
    }

    public void updateWeightFromInput() {
        double weight;

        try {
            weight = Double.parseDouble(weightEditText.getText().toString());

            if (weight < 0)
                throw new Exception("Invalid weight :" + weight);
        } catch (Exception e) {
            Log.e(LOG_KEY, e.getMessage());
            // Pseudo-recursive call
            weightEditText.setText("0");
            weightEditText.selectAll();
            return;
        }

        if (weight >= 0) {
            setSlot.setWeight(weight);
            updateSetNameStyle();
            startCalculateSets();
        }
    }

    public void updateRepsFromInput() {
        setSlot.setReps(repsSpinner.getSelectedItemPosition() + 1);
        startCalculateSets();
        updateSetNameStyle();
    }

    private void cannotSaveToast() {
        Toast.makeText(getActivity(), "Nothing to save.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(View v) {

        if (v.getId() == R.id.fab) {
            if (setSlot.getWeight() > 0) {
                showSaveSetDialog();
            } else {
                cannotSaveToast();
            }
        }

        return true;
    }

    public void showSaveSetDialog() {
        SaveSetDialog saveSetDialog =
                SaveSetDialog.newInstance(new LoadUpdateHandler(this), setSlot.getReps(), setSlot.getWeight());
        saveSetDialog.show(getFragmentManager(), getTag());
        SetsListDialog.setHandler(new UpdateOnDismissHandler(this));
    }

    private class AsyncCalculate extends Observable implements Runnable {

        private List<IDetailRow> weightHolders;
        private boolean stop;

        public List<IDetailRow> getWeightHolders() {
            return weightHolders;
        }

        @Override
        public void run() {
            formula.update(setSlot.getReps(), setSlot.getWeight());

            int maxReps = getResources().getInteger(R.integer.max_reps);
            List<IDetailRow> weightHolders = new ArrayList<>(maxReps);

            for (int i = 0; i < maxReps; i++) {
                int currentReps = i + 1;
                double currentWeight = formula.getWeightWeightForReps(currentReps);
                weightHolders.add(new DetailRow(currentReps, Integer.toString(currentReps),
                        weightFormatter.format(currentWeight) + " " + weightFormatter.displayUnit(currentWeight),
                        Integer.toString((int) formula.getPercentOfMax(currentWeight)) + "%"));
            }

            this.weightHolders = weightHolders;

            handler.post(new Runnable() {
                @Override
                public void run() {
                    setChanged();
                    notifyObservers();
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SET_ID, setSlot.getId());
        outState.putDouble(STATE_WEIGHT, setSlot.getWeight());
        outState.putInt(STATE_REPS, setSlot.getReps());
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
        item.setIcon(R.drawable.ic_storage);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_load:
                LoadSetDialog loadSetDialog =
                        LoadSetDialog.newInstance(new LoadUpdateHandler(this));
                loadSetDialog.show(getFragmentManager(), getTag());
                SetsListDialog.setHandler(new UpdateOnDismissHandler(this));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        startCalculateSets();
    }

    private void showIncrementList() {
        SetQuickPlateDialog setQuickPlateDialog =
                SetQuickPlateDialog.newInstance(new IncrementUpdateHandler(this));
        setQuickPlateDialog.show(getFragmentManager(), getTag());
    }

    @Override
    public void update(Observable observable, Object o) {
        if (asyncCalculate.getWeightHolders() != null) {
            weightListAdapter.updateData(asyncCalculate.getWeightHolders());
        } else {

        }
    }

    @Override
    public void onUndo(Parcelable token) {
        setSlot.rollbackChanges(getActivity());
        updateWeightFromInput();
        updateRepsFromInput();
        updateSetNameStyle();
        floatingActionButton.animate().translationY(0);
    }

    @Override
    public void onUndoTimeout() {
        setSlot.resetSnapshot();
        floatingActionButton.animate().translationY(0);
    }


    public void updateSetNameStyle() {
        if (setSlot.hasChanged()) {
            setNameTextView.setTypeface(null, Typeface.ITALIC);
        } else {
            setNameTextView.setTypeface(null, Typeface.NORMAL);
        }
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
            maxRepFragment.updateQuickPlateButtons(msg.arg1);
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
            maxRepFragment.updateSet();
        }
    }

    public static class UpdateOnDismissHandler extends Handler {
        private final WeakReference<MaxRepFragment> mService;

        UpdateOnDismissHandler(MaxRepFragment service) {
            mService = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg)
        {
            MaxRepFragment service = mService.get();
            if (service != null) {
                service.reloadSetName();
            }
        }
    }


    public void reloadSetName() {
        setSlot.reloadName(getActivity());
        setNameTextView.setText(setSlot.getName());
    }

    public double getIncrementValue() {
        return incrementValue;
    }

    public void loadSet(int id) {
        setSlot = SetSlot.findById(getActivity(), id);
    }

    public void startCalculateSets() {
        calculateThread = new Thread(asyncCalculate);
        calculateThread.start();
    }

    public void updateSet() {
        try {
            repsSpinner.setSelection(setSlot.getReps() - 1);
            weightEditText.setText(Double.toString(setSlot.getWeight()));
            setNameTextView.setText(setSlot.getName());
        } catch (NullPointerException exception) {
            Log.e(LOG_KEY, "updateSet() threw a NullPointerException:" + exception.getMessage());
        }
    }

    public void updateQuickPlateButtons(int index) {
        incrementValue = incrementSet.getIncrements(weightFormatter.getUnit())[index];
        NumberFormat formatter = new DecimalFormat("#.#");
        String incrementText = formatter.format(incrementValue) + " " + weightFormatter.displayUnit(incrementValue);

        try {
            // Update buttons
            subtractButton.setText("-" + incrementText);
            addButton.setText("+" + incrementText);
        } catch (NullPointerException exception) {
            Log.e(LOG_KEY, "updateQuickPlateButtons() threw a NullPointerException:" + exception.getMessage());
        }
    }
}
