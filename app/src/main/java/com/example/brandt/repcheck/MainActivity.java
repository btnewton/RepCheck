package com.example.brandt.repcheck;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    int PROFILE = R.mipmap.boilerman2;

    ListView navList;
    DrawerLayout Drawer;
    NavListAdapter navListAdapter;
    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.abc_btn_check_material);
        toolbar.setNavigationContentDescription("Navigate up");
        toolbar.setLogo(R.drawable.ic_launcher);

        ImageButton settings = (ImageButton) findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(v.getContext(), SettingsActivity.class);
                startActivity(settingsIntent);
            }
        });

        CircularImageView profile = (CircularImageView) findViewById(R.id.profile_picture);
        profile.setImageResource(PROFILE);

        navList = (ListView) findViewById(R.id.nav_options);
        navList.setOnItemClickListener(this);

        NavRowItem[] navRowItems = new NavRowItem[]{
                new NavRowItem(R.drawable.ic_assessment, "Log"),
                new NavRowItem(R.drawable.ic_cart, "Food Manager"),
                new NavRowItem(R.drawable.ic_compass, "Diet Manager"),
        };

        navListAdapter = new NavListAdapter(this, getLayoutInflater(), navRowItems);
        navList.setAdapter(navListAdapter);

        Drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer, toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        Drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content, new LogFragment()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = preferences.getString("name", "");
        String subtitle = preferences.getString("subtitle", "");

        TextView nameView = (TextView) findViewById(R.id.name);
        nameView.setText(name);

        TextView subtitleView = (TextView) findViewById(R.id.subtitle);
        subtitleView.setText(subtitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Fragment fragment;

        switch (position) {
            case (0):
                fragment = new LogFragment();
                break;
            case (1):
                fragment = new FoodManagerFragment();
                break;
            case (2):
            default:
                fragment = new DietManagerFragment();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();
        navList.setItemChecked(position, true);
        setTitle(navListAdapter.getItem(position).getText());

        Drawer.closeDrawers();
    }
}