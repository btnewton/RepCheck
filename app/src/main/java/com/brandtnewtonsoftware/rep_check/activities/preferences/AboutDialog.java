package com.brandtnewtonsoftware.rep_check.activities.preferences;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brandtnewtonsoftware.rep_check.R;

/**
 * Created by Brandt on 8/8/2015.
 */
public class AboutDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final View aboutView = getActivity().getLayoutInflater().inflate(R.layout.dialog_about, null, false);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(aboutView).create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                try {
                    PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                    TextView versionTextView = (TextView) aboutView.findViewById(R.id.version);
                    versionTextView.setText("Version " + pInfo.versionName);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                ImageView brandtEmailLink = (ImageView) aboutView.findViewById(R.id.brandt_email_link);
                brandtEmailLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        makeEmailIntent(getString(R.string.brandt_email));
                    }
                });
                ImageView brandtWebsiteLink = (ImageView) aboutView.findViewById(R.id.brandt_website_link);
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

                Button closeButton = (Button) aboutView.findViewById(R.id.close_btn);
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

    private void makeWebsiteIntent(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    private void makeEmailIntent(String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Rep Check");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "Could not find email client.", Toast.LENGTH_SHORT).show();
        }
    }
}
