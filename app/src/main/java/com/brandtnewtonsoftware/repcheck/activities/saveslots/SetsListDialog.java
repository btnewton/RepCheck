package com.brandtnewtonsoftware.repcheck.activities.saveslots;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.brandtnewtonsoftware.repcheck.R;
import com.brandtnewtonsoftware.repcheck.models.SetSlot;
import com.brandtnewtonsoftware.repcheck.models.Unit;
import com.brandtnewtonsoftware.repcheck.models.WeightFormatter;
import com.brandtnewtonsoftware.repcheck.util.adapters.standard.IStandardRowItem;
import com.brandtnewtonsoftware.repcheck.util.adapters.standard.StandardRowItem;
import com.brandtnewtonsoftware.repcheck.util.adapters.standard.StandardRowListAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Brandt on 7/23/2015.
 */
public abstract class SetsListDialog extends DialogFragment implements Observer {

    protected StandardRowListAdapter adapter;
    protected List<IStandardRowItem> rowItems;
    private Handler asyncHandler;
    private static Handler handler;
    private final static String LOG_KEY = "SetsListDialog";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        asyncHandler = new Handler();
        adapter = StandardRowListAdapter.newSaveSlotAdapter(getActivity(), getActivity().getLayoutInflater());
        rowItems = new ArrayList<>();

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View setSlotsView = inflater.inflate(R.layout.dialog_list, null);
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(setSlotsView)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                TextView titleTextView = (TextView) setSlotsView.findViewById(R.id.title);
                titleTextView.setText(getTitle());

                ListView listView = (ListView) setSlotsView.findViewById(R.id.list);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        onItemClickAction(parent, view, position, id);
                    }
                });
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        return longItemClickAction(parent, view, position, id);
                    }
                });

                Button closeButton = (Button) setSlotsView.findViewById(R.id.close_btn);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            }
        });

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    public void refreshData() {
        AsyncSelect asyncSelect = new AsyncSelect();
        asyncSelect.addObserver(this);
        new Thread(asyncSelect).start();
    }

    @Override
    public void update(Observable observable, Object o) {
        if (rowItems == null) {
            Toast toast = Toast.makeText(getActivity(), "No saved sets.",  Toast.LENGTH_SHORT);
            toast.show();
            dismiss();
        } else {
            adapter.updateData(rowItems);
        }
    }

    public abstract String getTitle();

    public abstract void onItemClickAction(AdapterView<?> parent, View view, int position, long id);

    public boolean longItemClickAction(AdapterView<?> parent, View view, int position, long id) {
        DialogFragment saveAsNewDialog = RenameSetDialog.newInstance(new UpdateOnDismissHandler(this), adapter.getItem(position).getId());
        saveAsNewDialog.show(getActivity().getSupportFragmentManager(), "RenameSetDialog");
        return true;
    }

    public static class RenameSetDialog extends DialogFragment {

        private static final String SLOT_ID_KEY = "slot_id";
        private static final String LOG_KEY = "RenameSetDialog";
        public static Handler updateHandler;
        private SetSlot setSlot;

        public static RenameSetDialog newInstance(Handler updateHandler, int slotId) {
            RenameSetDialog dialog = new RenameSetDialog();
            RenameSetDialog.updateHandler = updateHandler;

            Bundle args = new Bundle();
            args.putInt(SLOT_ID_KEY, slotId);

            dialog.setArguments(args);

            return dialog;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            if (getArguments() != null) {
                int setId = getArguments().getInt(SLOT_ID_KEY);
                setSlot = SetSlot.findById(getActivity(), setId);
            }

            if (setSlot == null){
                Log.e(LOG_KEY, "SetSlot was null! Dismissing dialog.");
                dismiss();
            }
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final View setSlotsView = getActivity().getLayoutInflater().inflate(R.layout.dialog_edittext, null);

            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setView(setSlotsView)
                    .create();

            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(final DialogInterface dialog) {
                    TextView titleTextView = (TextView) setSlotsView.findViewById(R.id.title);
                    titleTextView.setText("Rename Set");

                    final EditText inputField = (EditText) setSlotsView.findViewById(R.id.text_field);
                    inputField.setText(setSlot.getName());
                    inputField.setInputType(InputType.TYPE_CLASS_TEXT);
                    inputField.selectAll();

                    Button cancelButton = (Button) setSlotsView.findViewById(R.id.negative_btn);
                    cancelButton.setText("CANCEL");
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                        }
                    });
                    final Button renameButton = (Button) setSlotsView.findViewById(R.id.positive_btn);
                    renameButton.setText("RENAME");
                    renameButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String name = inputField.getText().toString().trim();

                            int maxLength = getResources().getInteger(R.integer.max_set_name_length);

                            if (!name.isEmpty() && name.length() <= maxLength) {
                                setSlot.setName(name);

                                if (setSlot.nameUnique(getActivity())) {
                                    setSlot.saveChanges(getActivity());
                                    dialog.dismiss();
                                    updateHandler.sendEmptyMessage(0);
                                } else {
                                    Toast toast = Toast.makeText(getActivity(), "Name taken.", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            } else {
                                Toast toast = Toast.makeText(getActivity(), "Name must have 1 to " + maxLength + " characters.", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    });

                    inputField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (EditorInfo.IME_ACTION_DONE == actionId) {
                                renameButton.callOnClick();
                                return true;
                            }
                            return false;
                        }
                    });

                }
            });
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            return dialog;
        }
    }

    public static void setHandler(Handler handler) {
        SetsListDialog.handler = handler;
    }

    static class UpdateOnDismissHandler extends Handler {
        private final WeakReference<SetsListDialog> mService;

        UpdateOnDismissHandler(SetsListDialog service) {
            mService = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg)
        {
            SetsListDialog service = mService.get();
            if (service != null) {
                service.refreshData();
            }
        }
    }

    private class AsyncSelect extends Observable implements Runnable {
        @Override
        public void run() {

            List<SetSlot> setSlots = SetSlot.selectAllByDate(getActivity());

            if (setSlots != null) {
                rowItems.clear();

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String unitType = sharedPreferences.getString(getActivity().getString(R.string.pref_units_key), getActivity().getString(R.string.pref_units_imperial));

                boolean shouldRound = sharedPreferences.getBoolean(getActivity().getString(R.string.pref_round_values_key), true);
                WeightFormatter formatter = new WeightFormatter(shouldRound, Unit.newUnitByString(unitType, getActivity()));

                for (SetSlot setSlot : setSlots) {
                    rowItems.add(new StandardRowItem(setSlot.getId(), setSlot.getName(), formatter.format(setSlot.getWeight()) + " " + formatter.displayUnit(setSlot.getWeight()) + " for " + setSlot.getReps() + " rep"
                            + ((setSlot.getReps() != 1) ? "s" : "")));
                }
            } else {
                rowItems = null;
            }

            asyncHandler.post(new Runnable() {
                @Override
                public void run() {
                    setChanged();
                    notifyObservers();
                }
            });
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (handler != null)
            handler.sendEmptyMessage(0);
    }
}