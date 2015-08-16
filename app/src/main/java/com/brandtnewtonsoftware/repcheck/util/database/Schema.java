package com.brandtnewtonsoftware.repcheck.util.database;

import android.content.Context;
import android.util.Log;

/**
 * Created by brandt on 7/22/15.
 */
public abstract class Schema {

    public void truncate(Context context) {
        DBHandler.getWritable(context).execSQL("DELETE FROM " + getTableName());

        Log.w("Schema", getTableName() + " has been truncated!");
    }

    public void drop(Context context) {
        DBHandler.getWritable(context).execSQL("DROP TABLE IF EXISTS " + getTableName());

        Log.w("Schema", getTableName() + " has been dropped!");
    }

    public abstract String getTableName();

    public abstract String tableSQL();

    public abstract String[] getColumns();
}
