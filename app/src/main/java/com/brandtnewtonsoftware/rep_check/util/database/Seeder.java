package com.brandtnewtonsoftware.rep_check.util.database;

import android.content.Context;

import java.util.Observable;

/**
 * Created by brandt on 7/22/15.
 */
public abstract class Seeder extends Observable {

    public void asyncSeed(Context context) {
        new Thread(new SeedTask(context)).start();
    }

    public abstract void seed(Context context);

    private class SeedTask implements Runnable {

        Context context;

        public SeedTask(Context context) {this.context = context; }

        @Override
        public void run() {
            try {
                seed(context);
            } catch (Exception e) {
                e.printStackTrace();
            }
            setChanged();
            notifyObservers();
        }
    }
}
