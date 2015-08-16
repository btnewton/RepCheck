package com.brandtnewtonsoftware.repcheck.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.brandtnewtonsoftware.repcheck.R;
import com.brandtnewtonsoftware.repcheck.activities.preferences.RepCheckPreferenceActivity;
import com.brandtnewtonsoftware.repcheck.activities.saveslots.LoadSetDialog;
import com.brandtnewtonsoftware.repcheck.database.schemas.SetSlotTable;
import com.brandtnewtonsoftware.repcheck.database.seeders.SetSeeder;
import com.brandtnewtonsoftware.repcheck.models.SetSlot;
import com.brandtnewtonsoftware.repcheck.util.AdMobHelper;
import com.brandtnewtonsoftware.repcheck.util.database.DBHandler;
import com.google.android.gms.ads.AdView;

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
        boolean showDisclaimer = sharedPreferences.getBoolean(getString(R.string.pref_show_disclaimer), false);

        if (showDisclaimer) {
            LoadSetDialog loadSetDialog =
                    LoadSetDialog.newInstance(new Handler());
            loadSetDialog.show(getSupportFragmentManager(), "Disclaimer");
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