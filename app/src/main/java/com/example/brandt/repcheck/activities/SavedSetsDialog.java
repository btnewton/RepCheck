package com.example.brandt.repcheck.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.ArrayAdapter;

import com.example.brandt.repcheck.models.SetSlot;
import com.example.brandt.repcheck.models.Unit;
import com.example.brandt.repcheck.models.increments.IronIncrementSet;
import com.example.brandt.repcheck.util.adapters.ImageOptionListAdapter;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Brandt on 7/23/2015.
 */
public abstract class SavedSetsDialog extends DialogFragment implements Observer, DialogInterface.OnClickListener {

    protected static Handler updateHandler;
    private ImageOptionListAdapter adapter;
    protected List<SetSlot> setSlots;
    private Handler asyncHandler;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // TODO get unit and increment type from preferences
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, new IronIncrementSet(Unit.ImperialUnit()).getIncrementsAsStringArray());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("SetSlot Interval")
                .setAdapter(arrayAdapter, this);

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        AsyncSelect asyncSelect = new AsyncSelect();
        asyncSelect.addObserver(this);
        new Thread(asyncSelect).start();
    }

    private class AsyncSelect extends Observable implements Runnable {
        @Override
        public void run() {

            setSlots = SetSlot.selectAllByDate(getActivity());

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