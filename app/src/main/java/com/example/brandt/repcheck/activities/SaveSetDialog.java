package com.example.brandt.repcheck.activities;

import android.content.DialogInterface;
import android.os.Bundle;

import com.example.brandt.repcheck.models.SetSlot;

import java.util.Observer;

/**
 * Created by Brandt on 7/25/2015.
 */
public class SaveSetDialog extends SetsListDialog implements Observer {

    private static final String REPS_KEY = "reps";
    private static final String WEIGHT_KEY = "weight";

    private static int reps;
    private static double weight;

    public static SaveSetDialog newInstance(int reps, double weight) {
        SaveSetDialog fragment = new SaveSetDialog();

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
    public String getTitle() {
        return "Select a Save Slot";
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        SetSlot setSlot = SetSlot.findById(getActivity(), rowItems.get(which).getId());
        setSlot.setReps(reps);
        setSlot.setWeight(weight);
        setSlot.saveChanges(getActivity());
        dismiss();
    }
}
