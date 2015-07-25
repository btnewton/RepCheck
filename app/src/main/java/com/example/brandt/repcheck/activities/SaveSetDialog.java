package com.example.brandt.repcheck.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.brandt.repcheck.models.SetSlot;
import com.example.brandt.repcheck.util.adapters.ImageOptionListAdapter;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Brandt on 7/25/2015.
 */
public class SaveSetDialog extends SavedSetsDialog implements Observer {

    private static final String REPS_KEY = "reps";
    private static final String WEIGHT_KEY = "weight";

    private int reps;
    private double weight;

    public static SavedSetsDialog newInstance(Handler updateHandler, int reps, double weight) {
        SavedSetsDialog fragment = new SaveSetDialog();
        SavedSetsDialog.updateHandler = updateHandler;

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

        if (setSlots == null) {
            Toast toast = Toast.makeText(getActivity(), "No saved sets.",  Toast.LENGTH_SHORT);
            toast.show();
            dismiss();
        } else {
            setSlots.add(0, );
            simpleRowListAdapter.updateData(setSlots);
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == 0) {
            // TODO launch new slot dialog
        } else {
            // TODO save set to slot "which"
        }
        dismiss();
    }
}
