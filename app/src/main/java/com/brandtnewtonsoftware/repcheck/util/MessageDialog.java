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
public abstract class MessageDialog  extends DialogFragment {

    private static final String LOG_KEY = "MessageDialog";

    public static Handler responseHandler;
    protected boolean canClose = false;
    protected abstract String getTitle();
    protected abstract String getBody();

    protected String getButtonText() {
        return "CLOSE";
    }

    protected String getCannotDismissMessage() {
        return "Please accept the conditions";
    }

    protected void onClose() {
        responseHandler.sendEmptyMessage(1);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final View confirmView = getActivity().getLayoutInflater().inflate(R.layout.message_dialog, null);

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

                final Button closeButton = (Button) confirmView.findViewById(R.id.close_btn);
                closeButton.setText(getButtonText());
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClose();
                        canClose = true;
                        dismiss();
                    }
                });
            }
        });

        return dialog;
    }
}