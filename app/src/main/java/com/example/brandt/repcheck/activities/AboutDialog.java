package com.example.brandt.repcheck.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.brandt.repcheck.R;

/**
 * Created by Brandt on 8/8/2015.
 */
public class AboutDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View aboutView = inflater.inflate(R.layout.about, null);
        builder.setView(aboutView);
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ImageView brandtEmailLink = (ImageView) aboutView.findViewById(R.id.brandt_email_link);
                brandtEmailLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        makeEmailIntent("");
                    }
                });
                ImageView brandtWebsiteLink= (ImageView) aboutView.findViewById(R.id.brandt_website_link);
                brandtWebsiteLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        makeWebsiteIntent(getString(R.string.brandt_website));
                    }
                });
                ImageView brandtResumeLink = (ImageView) aboutView.findViewById(R.id.brandt_resume_link);
                brandtResumeLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        makeWebsiteIntent(getString(R.string.brandt_resume));
                    }
                });
                ImageView konnorEmailLink = (ImageView) aboutView.findViewById(R.id.konnor_email_link);
                konnorEmailLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        makeEmailIntent(getString(R.string.konnor_email));
                    }
                });
            }
        });

        return dialog;
    }

    private void makeWebsiteIntent(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    private void makeEmailIntent(String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Rep Check");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            dismiss();
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "Could not find email client.", Toast.LENGTH_SHORT).show();
        }
    }
}
