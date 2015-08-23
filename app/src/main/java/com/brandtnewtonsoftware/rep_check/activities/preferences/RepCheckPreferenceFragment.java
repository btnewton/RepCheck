package com.brandtnewtonsoftware.rep_check.activities.preferences;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.brandtnewtonsoftware.rep_check.R;
import com.brandtnewtonsoftware.rep_check.util.ConfirmDialog;

import java.lang.ref.WeakReference;

/**
 * Created by brandt on 8/17/15.
 */
public class RepCheckPreferenceFragment extends PreferenceFragment {

    public final static String LOG_KEY = "RepCheckPreferenceFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupSimplePreferencesScreen();
    }

    /**
     * Shows the simplified settings UI if the device configuration if the
     * device configuration dictates that a simplified, single-pane UI should be
     * shown.
     */
    private void setupSimplePreferencesScreen() {

        addPreferencesFromResource(R.xml.pref_weight);

        final Activity activity = getActivity();

        CustomPreferenceCategory fakeHeader = new CustomPreferenceCategory(activity);
        fakeHeader = new CustomPreferenceCategory(activity);
        fakeHeader.setTitle(R.string.pref_header_about);
        getPreferenceScreen().addPreference(fakeHeader);
        addPreferencesFromResource(R.xml.pref_about);

        // Bind the summaries of EditText/List/Dialog/Ringtone preferences to
        // their values. When their values change, their summaries are updated
        // to reflect the new value, per the Android Design guidelines.
        bindPreferenceSummaryToValue(findPreference(getResources().getString(R.string.pref_units_key)));
        bindPreferenceSummaryToValue(findPreference(getResources().getString(R.string.pref_plate_style_key)));
        bindPreferenceSummaryToValue(findPreference(getResources().getString(R.string.pref_bar_weight_key)));
        bindPreferenceSummaryToValue(findPreference(getResources().getString(R.string.pref_formula_key)));

        Preference tutorialButton = (Preference)findPreference(getString(R.string.pref_tutorial_button));
        tutorialButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();

                editor.putBoolean(getString(R.string.pref_prompt_tutorial_flag), true);
                editor.apply();
                getActivity().finish();

                return true;
            }
        });

        Preference aboutButton = (Preference)findPreference(getString(R.string.pref_about_button));
        aboutButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AboutDialog aboutDialog = new AboutDialog();
                aboutDialog.show(getFragmentManager(), "AboutDialog");
                return true;
            }
        });

        Preference rateButton = (Preference)findPreference(getString(R.string.pref_rate_button));
        rateButton .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + activity.getPackageName())));
                }
                return true;
            }
        });

        final RepCheckPreferenceFragment repCheckPreferenceFragment = this;

        Preference resetButton = (Preference)findPreference(getString(R.string.pref_reset_button));
        resetButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                DialogFragment saveAsNewDialog = ConfirmResetDialog.newInstance(new ResetResponseHandler(repCheckPreferenceFragment));
                saveAsNewDialog.show(activity.getFragmentManager(), "RenameSetDialog");
                return true;
            }
        });
    }

    public static class ConfirmResetDialog extends ConfirmDialog {

        public static ConfirmResetDialog newInstance(Handler updateHandler) {
            ConfirmResetDialog dialog = new ConfirmResetDialog();
            ConfirmResetDialog.responseHandler = updateHandler;

            return dialog;
        }
        @Override
        protected String getTitle() {
            return "Reset Settings";
        }
        @Override
        protected String getBody() {
            return "Do you want to reset your settings?";
        }
        @Override
        protected String getPositive() {
            return "RESET";
        }
    }

    @SuppressLint("CommitPrefEdits")
    public void reset() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getString(R.string.pref_units_key), getString(R.string.pref_units_default));
        editor.putBoolean(getString(R.string.pref_round_values_key), getResources().getBoolean(R.bool.pref_round_values_default));
        editor.putString(getString(R.string.pref_plate_style_key), getString(R.string.pref_plate_style_default));
        editor.putString(getString(R.string.pref_bar_weight_key), getString(R.string.pref_bar_weight_default));
        editor.putString(getString(R.string.pref_formula_key), getString(R.string.pref_formula_default));
        editor.commit();
        getActivity().recreate();
    }

    static class ResetResponseHandler extends Handler {
        private final WeakReference<RepCheckPreferenceFragment> mService;

        ResetResponseHandler(RepCheckPreferenceFragment service) {
            mService = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg)
        {
            RepCheckPreferenceFragment service = mService.get();
            if (service != null && msg.what != 0) {
                service.reset();
            }
        }
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // SetSlot the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // SetSlot the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

}
