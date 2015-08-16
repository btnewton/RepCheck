package com.brandtnewtonsoftware.repcheck.util;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.brandtnewtonsoftware.repcheck.R;

/**
 * Created by Brandt on 8/16/2015.
 */
public abstract class ConfirmDialog extends DialogFragment {

    private static final String LOG_KEY = "ConfirmDialog";

    public static Handler responseHandler;

    protected abstract String getTitle();
    protected abstract String getBody();

    protected String getNegative() {
        return "CANCEL";
    }
    protected String getPositive() {
        return "CONFIRM";
    }

    protected void onCancel() {
        responseHandler.sendEmptyMessage(0);
    }

    protected void onConfirm() {
        responseHandler.sendEmptyMessage(1);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final View confirmView = getActivity().getLayoutInflater().inflate(R.layout.confirm_dialog, null);

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(confirmView)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                TextView titleTextView = (TextView) confirmView.findViewById(R.id.title);
                titleTextView.setText(getTitle());

                final TextView body = (TextView) confirmView.findViewById(R.id.body);
                body.setText(getBody());

                Button cancelButton = (Button) confirmView.findViewById(R.id.negative_btn);
                cancelButton.setText(getNegative());
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCancel();
                        dismiss();
                    }
                });
                final Button positiveButton = (Button) confirmView.findViewById(R.id.positive_btn);
                positiveButton.setText(getPositive());
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onConfirm();
                        dismiss();
                    }
                });
            }
        });

        return dialog;
    }
}
