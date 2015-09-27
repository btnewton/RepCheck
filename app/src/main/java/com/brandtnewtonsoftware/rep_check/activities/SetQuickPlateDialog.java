package com.brandtnewtonsoftware.rep_check.activities;

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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.brandtnewtonsoftware.rep_check.R;
import com.brandtnewtonsoftware.rep_check.models.Unit;
import com.brandtnewtonsoftware.rep_check.models.increments.IncrementFactory;
import com.brandtnewtonsoftware.rep_check.util.adapters.DividerItemDecoration;
import com.brandtnewtonsoftware.rep_check.util.adapters.NestableLinearLayoutManager;
import com.brandtnewtonsoftware.rep_check.util.adapters.standard.StandardRowAdapter;

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
        Unit unit = Unit.newUnitByString(activity);
        final StandardRowAdapter adapter = StandardRowAdapter.newSaveSlotAdapter(IncrementFactory.Make(activity, unit).getIncrementsAsStringArray());
        adapter.setItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Message msg = new Message();
                msg.arg1 = position;
                updateHandler.sendMessage(msg);
                dismiss();
            }
        });
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View setSlotsView = inflater.inflate(R.layout.dialog_list, null);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(setSlotsView)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                TextView titleTextView = (TextView) setSlotsView.findViewById(R.id.dialog_title);
                titleTextView.setText("Quick Plate Size");

                RecyclerView recyclerView = (RecyclerView) setSlotsView.findViewById(R.id.recycler_view);
                recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL_LIST));
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new NestableLinearLayoutManager(getContext()));


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
