package com.example.brandt.repcheck.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
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

    private int reps;
    private double weight;

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
        if (rowItems != null) {
            rowItems.add(0, new StandardRowItem(-1, "New Slot", "Save in a new set slot."));
            adapter.updateData(rowItems);
        } else {
            super.update(observable, data);
        }
    }

    @Override
    public String getTitle() {
        return "Select a Save Slot";
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == 0) {
            SaveAsNewDialog();
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

    private void SaveAsNewDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Save as New Slot");

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();
                SetSlot setSlot = new SetSlot(reps, weight, new Date());
                setSlot.setName(name);
                setSlot.saveChanges(getActivity());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
