package com.brandtnewtonsoftware.rep_check.activities;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.brandtnewtonsoftware.rep_check.R;

public class TutorialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add(Menu.NONE, R.id.action_close, 10, R.string.close);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        Drawable loadIcon = ContextCompat.getDrawable(this, R.drawable.ic_close);
        loadIcon.mutate().setColorFilter(getResources().getColor(R.color.accent_500), PorterDuff.Mode.SRC_IN);
        item.setIcon(loadIcon);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_close) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
