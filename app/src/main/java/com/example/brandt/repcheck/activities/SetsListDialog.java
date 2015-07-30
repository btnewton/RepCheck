package com.example.brandt.repcheck.activities;

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
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.brandt.repcheck.R;
import com.example.brandt.repcheck.models.SetSlot;
import com.example.brandt.repcheck.models.Unit;
import com.example.brandt.repcheck.util.adapters.StandardRowItem;
import com.example.brandt.repcheck.util.adapters.StandardRowItemAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Brandt on 7/23/2015.
 */
public abstract class SetsListDialog extends DialogFragment implements Observer, DialogInterface.OnClickListener {

    protected StandardRowItemAdapter adapter;
    protected List<StandardRowItem> rowItems;
    private Handler asyncHandler;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        asyncHandler = new Handler();
        adapter = new StandardRowItemAdapter(getActivity(), getActivity().getLayoutInflater(), null);
        rowItems = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getTitle())
                .setAdapter(adapter, this);

        final AlertDialog mDialog = builder.create();
        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ListView listview = mDialog.getListView();
                listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        return longItemClickAction(parent, view, position, id);
                    }
                });
            }
        });

        return mDialog;
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

    public boolean longItemClickAction(AdapterView<?> parent, View view, int position, long id) {
        DialogFragment saveAsNewDialog = RenameSlotDialog.newInstance(new UpdateOnDismissHandler(this), adapter.getItem(position).getId());
        saveAsNewDialog.show(getActivity().getSupportFragmentManager(), "RenameSlotDialog");
        return true;
    }

    public static class RenameSlotDialog extends DialogFragment {

        private static final String SLOT_ID_KEY = "slot_id";
        public Handler updateHandler;
        SetSlot setSlot;

        public static RenameSlotDialog newInstance(Handler updateHandler, int slotId) {
            RenameSlotDialog dialog = new RenameSlotDialog();
            dialog.updateHandler = updateHandler;

            Bundle args = new Bundle();
            args.putInt(SLOT_ID_KEY, slotId);

            dialog.setArguments(args);

            return dialog;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            if (getArguments() != null) {
                int slotId = getArguments().getInt(SLOT_ID_KEY);
                setSlot = SetSlot.findById(getActivity(), slotId);
            } else {
                dismiss();
            }
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Rename Slot");

            if (setSlot == null) {
                getDialog().dismiss();
            }

            final EditText input = new EditText(getActivity());
            input.setText(setSlot.getName());
            input.setSelectAllOnFocus(true);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String name = input.getText().toString();
                    dialog.dismiss();

                    if (!name.isEmpty() && setSlot != null) {
                        setSlot.setName(name);

                        if (setSlot.nameUnique(getActivity())) {
                            setSlot.saveChanges(getActivity());
                            updateHandler.sendEmptyMessage(0);
                        } else {
                            Toast toast = Toast.makeText(getActivity(), "Name taken.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            return builder.create();
        }
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
                Unit unit = Unit.newUnitByString(unitType, getActivity());

                for (SetSlot setSlot : setSlots) {
                    rowItems.add(new StandardRowItem(setSlot.getId(), setSlot.getName(), setSlot.getReps() + " rep" + ((setSlot.getReps() != 1) ? "s" : "") + " at " + setSlot.getWeight() + unit.getUnit() + "s"));
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
}