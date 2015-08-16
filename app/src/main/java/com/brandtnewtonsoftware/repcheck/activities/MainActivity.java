package com.brandtnewtonsoftware.repcheck.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.brandtnewtonsoftware.repcheck.R;
import com.brandtnewtonsoftware.repcheck.activities.preferences.RepCheckPreferenceActivity;
import com.brandtnewtonsoftware.repcheck.database.schemas.SetSlotTable;
import com.brandtnewtonsoftware.repcheck.database.seeders.SetSeeder;
import com.brandtnewtonsoftware.repcheck.models.SetSlot;
import com.brandtnewtonsoftware.repcheck.util.AdMobHelper;
import com.brandtnewtonsoftware.repcheck.util.MessageDialog;
import com.brandtnewtonsoftware.repcheck.util.database.DBHandler;
import com.google.android.gms.ads.AdView;

import java.lang.ref.WeakReference;

public class MainActivity extends ActionBarActivity {

    MaxRepFragment maxRepFragment;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            toolbar.setElevation(getResources().getDimension(R.dimen.toolbar_elevation));
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        adView = AdMobHelper.CreateAdRequest(this);

        // Populate table if missing
        if (SetSlot.getSlotCount(this) != getResources().getInteger(R.integer.set_slot_count)) {
            DBHandler.truncateTable(this, new SetSlotTable());
            new SetSeeder().seed(this);
        }

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            maxRepFragment = (MaxRepFragment) getSupportFragmentManager().getFragment(savedInstanceState, "MaxRepFragment");
        } else {
            maxRepFragment = new MaxRepFragment();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content, maxRepFragment, "MaxRepFragment").commit();

        loadAppPreferences();
    }

    private void loadAppPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int openCount = sharedPreferences.getInt(getString(R.string.pref_app_start_counter), 0);
        editor.putInt(getString(R.string.pref_app_start_counter), openCount++);

        boolean showDisclaimer = sharedPreferences.getBoolean(getString(R.string.pref_show_disclaimer), true);

        if (showDisclaimer) {
            ConfirmDisclaimerDialog confirmDisclaimerDialog =
                    ConfirmDisclaimerDialog.newInstance(new DisclaimerResponse(this));
            confirmDisclaimerDialog.show(getFragmentManager(), "ConfirmDisclaimerDialog");
        } else {
            boolean showRateDialog = sharedPreferences.getBoolean(getString(R.string.pref_prompt_rate_dialog), true);

            if (showRateDialog && openCount >= getResources().getInteger(R.integer.prompt_rate_count)) {
                // TODO open dialog
            }
        }
    }

    public static class ConfirmDisclaimerDialog extends MessageDialog {

        public static ConfirmDisclaimerDialog newInstance(Handler updateHandler) {
            ConfirmDisclaimerDialog dialog = new ConfirmDisclaimerDialog();
            ConfirmDisclaimerDialog.responseHandler = updateHandler;

            return dialog;
        }
        @Override
        protected String getTitle() {
            return "Disclaimer";
        }
        @Override
        protected String getBody() {
            return "This app is for informational purposes only. " +
                    "Its recommendations are expressly not to be considered medical counsel. " +
                    "Always consult a doctor or health care professional before beginning any new training program. " +
                    "Always take proper safety precautions when exercising. \n" +
                    "The creators of this app make no warranties or guarantees of any kind with respect " +
                    "to this app and its contents.";
        }

        @Override
        protected String getButtonText() {
            return "GOT IT";
        }
    }

    public void unflagDisclaimer() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getString(R.string.pref_show_disclaimer), false);
        editor.apply();
    }

    static class DisclaimerResponse extends Handler {
        private final WeakReference<MainActivity> mService;

        DisclaimerResponse(MainActivity service) {
            mService = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg)
        {
            MainActivity service = mService.get();
            if (service != null && msg.what != 0) {
                service.unflagDisclaimer();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, RepCheckPreferenceActivity.class);
                startActivity(settingsIntent);
                break;
       }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "MaxRepFragment", maxRepFragment);
    }

    @Override
    protected void onPause() {
        adView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adView.pause();
    }
}