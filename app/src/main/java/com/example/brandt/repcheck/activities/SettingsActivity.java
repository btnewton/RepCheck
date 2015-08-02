package com.example.brandt.repcheck.activities;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.example.brandt.repcheck.R;
import com.example.brandt.repcheck.database.seeders.SetSeeder;
import com.example.brandt.repcheck.util.database.DBHandler;

/**
 * Created by brandt on 7/22/15.
 */
public class SettingsActivity extends PreferenceActivity {

    /**
     * Determines whether to always show the simplified settings UI, where
     * settings are presented in a single list. When false, settings are shown
     * as a master/detail two-pane view on tablets. When true, a single pane is
     * shown on tablets.
     */
    private static final boolean ALWAYS_SIMPLE_PREFS = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.action_settings));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setupSimplePreferencesScreen();
    }

    /**
     * Shows the simplified settings UI if the device configuration if the
     * device configuration dictates that a simplified, single-pane UI should be
     * shown.
     */
    private void setupSimplePreferencesScreen() {

        // In the simplified UI, fragments are not used at all and we instead
        // use the older PreferenceActivity APIs.

        addPreferencesFromResource(R.xml.pref_weight);

        // Add 'developer' preferences, and a corresponding header.
        PreferenceCategory fakeHeader = new PreferenceCategory(this);
        fakeHeader.setTitle(R.string.pref_header_developer);
        getPreferenceScreen().addPreference(fakeHeader);
        addPreferencesFromResource(R.xml.pref_developer);

        // Bind the summaries of EditText/List/Dialog/Ringtone preferences to
        // their values. When their values change, their summaries are updated
        // to reflect the new value, per the Android Design guidelines.
        bindPreferenceSummaryToValue(findPreference(getResources().getString(R.string.pref_units_key)));
        bindPreferenceSummaryToValue(findPreference(getResources().getString(R.string.pref_plate_style_key)));
        bindPreferenceSummaryToValue(findPreference(getResources().getString(R.string.pref_bar_weight_key)));

        Preference populateSetsButton = (Preference)findPreference(getString(R.string.populate_sets_button));
        populateSetsButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                populateSets();
                return true;
            }
        });

        Preference populateAllButton = (Preference)findPreference(getString(R.string.populate_all_button));
        populateAllButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                truncateTables();
                populateSets();
                return true;
            }
        });

        Preference truncateAllButton = (Preference)findPreference(getString(R.string.truncate_all_button));
        truncateAllButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                truncateTables();
                return true;
            }
        });
    }

    public void truncateTables() {
        DBHandler dbHandler = DBHandler.getInstance(this);
        dbHandler.truncateAll();
    }

    public void populateSets(){
        new SetSeeder().seed(this);
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

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // SetSlot the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

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

