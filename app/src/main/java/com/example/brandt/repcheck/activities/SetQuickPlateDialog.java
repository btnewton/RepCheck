package com.example.brandt.repcheck.activities;

import android.app.Activity;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.brandt.repcheck.R;
import com.example.brandt.repcheck.models.Unit;
import com.example.brandt.repcheck.models.increments.IncrementFactory;

/**
 * Created by Brandt on 7/23/2015.
 */
public class SetQuickPlateDialog extends DialogFragment {

    private static Handler updateHandler;

    public static SetQuickPlateDialog newInstance(Handler updateHandler) {
        SetQuickPlateDialog fragment = new SetQuickPlateDialog();
        SetQuickPlateDialog.updateHandler = updateHandler;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity activity = getActivity();

        // Load unit and plate style
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String plateStyle = sharedPreferences.getString(getString(R.string.pref_plate_style_key), getString(R.string.pref_plate_style_classic));
        String unitType = sharedPreferences.getString(getString(R.string.pref_units_key), getString(R.string.pref_units_metric));
        Unit unit = Unit.newUnitByString(unitType, activity);

        final ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, IncrementFactory.Make(activity, plateStyle, unit).getIncrementsAsStringArray());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View setSlotsView = inflater.inflate(R.layout.list_dialog, null);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(setSlotsView)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                TextView titleTextView = (TextView) setSlotsView.findViewById(R.id.title);
                titleTextView.setText("Quick Plate Size");

                ListView listView = (ListView) setSlotsView.findViewById(R.id.list);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Message msg = new Message();
                        msg.arg1 = position;
                        updateHandler.sendMessage(msg);
                        dismiss();
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
}