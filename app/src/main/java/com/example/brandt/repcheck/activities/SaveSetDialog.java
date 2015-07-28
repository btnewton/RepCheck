package com.example.brandt.repcheck.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.widget.EditText;

import com.example.brandt.repcheck.models.SetSlot;
import com.example.brandt.repcheck.util.adapters.StandardRowItem;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Brandt on 7/25/2015.
 */
public class SaveSetDialog extends SetsListDialog implements Observer {

    private static final String REPS_KEY = "reps";
    private static final String WEIGHT_KEY = "weight";

    private static int reps;
    private static double weight;
    private static SetSlot setSlot;

    public static SaveSetDialog newInstance(Handler updateHandler, int reps, double weight) {
        SaveSetDialog fragment = new SaveSetDialog();
        SaveSetDialog.updateHandler = updateHandler;

        Bundle args = new Bundle();
        args.putInt(REPS_KEY, reps);
        args.putDouble(WEIGHT_KEY, weight);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            reps = getArguments().getInt(REPS_KEY);
            weight = getArguments().getDouble(WEIGHT_KEY);
        } else {
            dismiss();
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if (rowItems == null) {
            DialogFragment saveAsNewDialog = new SaveAsNewDialog();
            saveAsNewDialog.show(getActivity().getSupportFragmentManager(), "SaveAsNewDialog");
            dismiss();
        } else {
            rowItems.add(0, new StandardRowItem(-1, "New Slot", "Save in a new set slot."));
            adapter.updateData(rowItems);
        }
    }

    @Override
    public String getTitle() {
        return "Select a Save Slot";
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == 0) {
            DialogFragment saveAsNewDialog = new SaveAsNewDialog();
            saveAsNewDialog.show(getActivity().getSupportFragmentManager(), "SaveAsNewDialog");
        } else {
            // Offset new row item
            which++;

            SetSlot setSlot = SetSlot.findById(getActivity(), rowItems.get(which).getId());
            setSlot.setReps(reps);
            setSlot.setWeight(weight);
            setSlot.saveChanges(getActivity());
        }
        dismiss();
    }

    public static class SaveAsNewDialog extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Save as New Slot");

            final EditText input = new EditText(getActivity());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String name = input.getText().toString();
                    dialog.dismiss();
                    setSlot = new SetSlot(reps, weight, new Date());
                    setSlot.setName(name);

                    if (setSlot.nameUnique(getActivity())) {
                        setSlot.saveChanges(getActivity());
                    } else {
                        DialogFragment confirmOverwriteDialog = new ConfirmOverwriteDialog();
                        confirmOverwriteDialog.show(getActivity().getSupportFragmentManager(), "ConfirmOverwriteDialog");
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            if (setSlot == null) {
                input.setText("");
            } else {
                input.setText(setSlot.getName());
            }

            return builder.create();
        }
    }

    public static class ConfirmOverwriteDialog extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final String name = setSlot.getName();
            builder.setTitle("Confirm Overwrite")
                    .setMessage("Do you want to overwrite " + name + "?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setSlot = SetSlot.findByName(getActivity(), name);
                            setSlot.setWeight(weight);
                            setSlot.setReps(reps);
                            setSlot.saveChanges(getActivity());
                            dialog.dismiss();
                        }

                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DialogFragment saveAsNewDialog = new SaveAsNewDialog();
                            saveAsNewDialog.show(getActivity().getSupportFragmentManager(), "SaveAsNewDialog");
                        }
                    });

            return builder.create();
        }
    }
}
