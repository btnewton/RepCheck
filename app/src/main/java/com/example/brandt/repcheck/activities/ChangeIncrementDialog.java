package com.example.brandt.repcheck.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.ArrayAdapter;

import com.example.brandt.repcheck.models.Unit;
import com.example.brandt.repcheck.models.increments.IronIncrementSet;

/**
 * Created by Brandt on 7/23/2015.
 */
public class ChangeIncrementDialog extends DialogFragment {

    private static Handler updateHandler;

    public static ChangeIncrementDialog newInstance(Handler updateHandler) {
        ChangeIncrementDialog fragment = new ChangeIncrementDialog();
        ChangeIncrementDialog.updateHandler = updateHandler;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // TODO get unit and increment type from preferences
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, new IronIncrementSet(Unit.ImperialUnit()).getIncrementsAsStringArray());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Set Slot Interval")
                .setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Message msg = new Message();
                        msg.arg1 = which;
                        updateHandler.sendMessage(msg);
                        dismiss();
                    }
                });

        return builder.create();
    }
}