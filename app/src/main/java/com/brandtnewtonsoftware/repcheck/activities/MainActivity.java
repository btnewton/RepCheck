package com.brandtnewtonsoftware.repcheck.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.brandtnewtonsoftware.repcheck.R;
import com.brandtnewtonsoftware.repcheck.activities.preferences.RepCheckPreferenceActivity;
import com.brandtnewtonsoftware.repcheck.database.schemas.SetSlotTable;
import com.brandtnewtonsoftware.repcheck.database.seeders.SetSeeder;
import com.brandtnewtonsoftware.repcheck.models.SetSlot;
import com.brandtnewtonsoftware.repcheck.util.AdMobHelper;
import com.brandtnewtonsoftware.repcheck.util.ConfirmDialog;
import com.brandtnewtonsoftware.repcheck.util.database.DBHandler;
import com.google.android.gms.ads.AdView;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    public final static String LOG_KEY = "MainActivity";
    MaxRepFragment maxRepFragment;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.frame_main);

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
            maxRepFragment = (MaxRepFragment) getSupportFragmentManager().getFragment(savedInstanceState, MaxRepFragment.LOG_KEY);
        } else {
            maxRepFragment = new MaxRepFragment();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content, maxRepFragment, MaxRepFragment.LOG_KEY).commit();

        loadAppPreferences();
    }

    private void loadAppPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int openCount = sharedPreferences.getInt(getString(R.string.pref_app_start_counter), 0);
        openCount++;
        editor.putInt(getString(R.string.pref_app_start_counter), openCount);
        editor.apply();

        Log.i(LOG_KEY, "Start count: " + openCount);

        boolean showRateDialog = sharedPreferences.getBoolean(getString(R.string.pref_prompt_rate_dialog), true);

        if (showRateDialog && openCount >= getResources().getInteger(R.integer.prompt_rate_count)) {
            RateThisAppDialog dialog = RateThisAppDialog.newInstance(new RateThisAppHandler(this));
            dialog.show(getFragmentManager(), RateThisAppDialog.LOG_KEY);
        }
    }

    public static class RateThisAppDialog extends ConfirmDialog {

        public static final String LOG_KEY = "RateThisAppDialog";

        public static RateThisAppDialog newInstance(Handler updateHandler) {
            RateThisAppDialog dialog = new RateThisAppDialog();
            RateThisAppDialog.responseHandler = updateHandler;
            return dialog;
        }

        @Override
        protected String getTitle() {
            return "Rate Rep Check?";
        }

        @Override
        protected String getBody() {
            return "Thanks for using Rep Check! Please consider rating the app to show your support or suggest improvements.";
        }

        @Override
        protected String getNegative() {
            return "No Thanks";
        }

        @Override
        protected String getPositive() {
            return "Rate";
        }
    }

    public void launchPlayStore() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        } finally {
            turnOffShowRateDialog();
        }
    }

    public void turnOffShowRateDialog() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getString(R.string.pref_prompt_rate_dialog), false);
        editor.apply();
    }

    static class RateThisAppHandler extends Handler {
        private final WeakReference<MainActivity> mService;

        RateThisAppHandler(MainActivity service) {
            mService = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg)
        {
            MainActivity service = mService.get();

            if (service != null) {
                if (msg.what != 0) {
                    service.launchPlayStore();
                } else {
                    service.turnOffShowRateDialog();
                }
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
        getSupportFragmentManager().putFragment(outState, MaxRepFragment.LOG_KEY, maxRepFragment);
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