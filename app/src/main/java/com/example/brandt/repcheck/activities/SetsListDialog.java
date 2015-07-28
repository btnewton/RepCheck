package com.example.brandt.repcheck.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.example.brandt.repcheck.R;
import com.example.brandt.repcheck.models.SetSlot;
import com.example.brandt.repcheck.models.Unit;
import com.example.brandt.repcheck.util.adapters.StandardRowItem;
import com.example.brandt.repcheck.util.adapters.StandardRowItemAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Brandt on 7/23/2015.
 */
public abstract class SetsListDialog extends DialogFragment implements Observer, DialogInterface.OnClickListener {

    protected static Handler updateHandler;
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

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
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