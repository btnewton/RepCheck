package com.brandtnewtonsoftware.rep_check.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import android.widget.RelativeLayout;

import com.brandtnewtonsoftware.rep_check.R;
import com.brandtnewtonsoftware.rep_check.activities.preferences.RepCheckPreferenceActivity;
import com.brandtnewtonsoftware.rep_check.activities.tutorial.BarLoad;
import com.brandtnewtonsoftware.rep_check.activities.tutorial.BasicUsage;
import com.brandtnewtonsoftware.rep_check.activities.tutorial.ChangingWeight;
import com.brandtnewtonsoftware.rep_check.activities.tutorial.Conclusion;
import com.brandtnewtonsoftware.rep_check.activities.tutorial.Introduction;
import com.brandtnewtonsoftware.rep_check.activities.tutorial.LoadingSets;
import com.brandtnewtonsoftware.rep_check.activities.tutorial.SavingSets;
import com.brandtnewtonsoftware.rep_check.database.schemas.SetSlotTable;
import com.brandtnewtonsoftware.rep_check.database.seeders.SetSeeder;
import com.brandtnewtonsoftware.rep_check.models.SetSlot;
import com.brandtnewtonsoftware.rep_check.util.AdMobHelper;
import com.brandtnewtonsoftware.rep_check.util.ConfirmDialog;
import com.brandtnewtonsoftware.rep_check.util.database.DBHandler;
import com.brandtnewtonsoftware.rep_check.util.tutorial.TutorialBuilder;
import com.brandtnewtonsoftware.rep_check.util.tutorial.TutorialEventListener;
import com.brandtnewtonsoftware.rep_check.util.tutorial.topic.TutorialTopic;
import com.google.android.gms.ads.AdView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TutorialEventListener {
    public final static String LOG_KEY = "MainActivity";
    MaxRepFragment maxRepFragment;
    TutorialBuilder tutorial;
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

    //region Tutorial Listeners
    @Override
    public void onTutorialStart() {
        adView.pause();
    }

    @Override
    public void onTutorialComplete() {
        adView.resume();
    }

    @Override
    public void onTutorialQuit(int topic) {
        adView.resume();
    }

    @Override
    public void onTopicChanged(int topic) {

    }
    //endregion

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

        MenuItem settingsIcon = menu.findItem(R.id.action_settings);
        Drawable newIcon = settingsIcon.getIcon();
        newIcon.mutate().setColorFilter(getResources().getColor(R.color.accent_500), PorterDuff.Mode.SRC_IN);
        settingsIcon.setIcon(newIcon);
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
        if (tutorial != null)
            tutorial.dispose();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adView.resume();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        if (sharedPreferences.getBoolean(getString(R.string.pref_prompt_tutorial_flag), true)) {
        // TODO return shared pref boolean
        if (true) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(getString(R.string.pref_prompt_tutorial_flag), false);
            editor.apply();

            List<TutorialTopic> topics = new ArrayList<>();
            topics.add(new Introduction());
            topics.add(new BasicUsage());
            topics.add(new ChangingWeight());
            topics.add(new BarLoad());
            topics.add(new SavingSets());
            topics.add(new LoadingSets());
            topics.add(new Conclusion());

            tutorial = new TutorialBuilder().setTopics(topics).start(this, (RelativeLayout) findViewById(R.id.container));
            tutorial.addListener(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adView.pause();
    }
}