package com.example.brandt.repcheck.activities.saveslots;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.brandt.repcheck.models.SetSlot;

import java.util.Observer;

/**
 * Created by Brandt on 7/25/2015.
 */
public class SaveSetDialog extends SetsListDialog implements Observer {

    private static final String REPS_KEY = "reps";
    private static final String WEIGHT_KEY = "weight";

    private int reps;
    private double weight;
    private static Handler updateHandler;

    public static SaveSetDialog newInstance(Handler handler, int reps, double weight) {
        SaveSetDialog fragment = new SaveSetDialog();
        SaveSetDialog.updateHandler = handler;

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

        Message msg = new Message();
        msg.arg1 = setSlot.getId();
        updateHandler.sendMessage(msg);

        dismiss();
    }
}
