package com.brandtnewtonsoftware.repcheck.util.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.brandtnewtonsoftware.repcheck.database.schemas.FormulaConfigurationTable;
import com.brandtnewtonsoftware.repcheck.database.schemas.SetSlotTable;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Created by brandt on 7/22/15.
 */
public class DBHandler extends SQLiteOpenHelper {

    // Increase to make app call onUpgrade
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "repcheck.db";

    // Singleton DBHandler instance
    private static DBHandler mInstance = null;
    private static DBHandler mTestInstance = null;

    private Schema[] tables = new Schema[] {
            new SetSlotTable(),
            new FormulaConfigurationTable(),
            // ADD NEW TABLES HERE!
    };

    // Private constructor to prevent direct instantiation.
    // Instantiate with getInstance() instead.
    private DBHandler(Context context, String databaseName) {
        super(context, databaseName, null, DATABASE_VERSION);
    }

    public static synchronized DBHandler getInstance(Context context) {
        if (mInstance == null) {
            // Use application context to prevent leak in activity's context
            mInstance = new DBHandler(context.getApplicationContext(), DATABASE_NAME);
        }

        return mInstance;
    }

    public static SQLiteDatabase getWritable(Context context) {
        return DBHandler.getInstance(context).getWritableDatabase();
    }

    public static SQLiteDatabase getReadable(Context context) {
        return DBHandler.getInstance(context).getReadableDatabase();
    }

    public static synchronized DBHandler getTestInstance(Context context, String DatabaseName) {
        if (mTestInstance == null) {
            // Use application context to prevent leak in activity's context
            mTestInstance = new DBHandler(context.getApplicationContext(), DatabaseName);
        }

        return mTestInstance;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        // Enable foreign key constraints
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (Schema table : tables) {
            db.execSQL( table.tableSQL() );
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {

        for (Schema table : tables) {
            db.execSQL("DROP TABLE IF EXISTS " + table.getTableName());
        }

        onCreate(db);

        Log.i("DBHandler", "Database updated!");
    }

    /**
     * Empties all data from all tables.
     *
     * DO NOT INCLUDE IN PRODUCTION VERSION!
     */
    public void truncateAll() {
        SQLiteDatabase db = this.getWritableDatabase();

        for (Schema table : tables) {
            db.execSQL("DELETE FROM " + table.getTableName());
        }

        Log.w("DB Handler", "Tables truncated!");
    }

    public static void truncateTable(Context context, Schema schema) {
        SQLiteDatabase db = DBHandler.getInstance(context).getWritableDatabase();
        db.execSQL("DELETE FROM " + schema.getTableName());
    }

    public static Date convertStringTime(String time) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-d HH:mm:ss");
        try {
            return format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getRoundedTimeFromMillis(int timeMillis) {

        final int YEAR = 31536000;
        final int MONTH = 2592000;
        final int WEEK = 604800;
        final int DAY = 86400;
        final int HOUR = 3600;
        final int MINUTE = 60;

        timeMillis = timeMillis / 1000;

        if (timeMillis < MINUTE) {
            return "just now";
        } else if (timeMillis < HOUR) {
            int minutes = (int) timeMillis / MINUTE;
            return minutes + " minute" + ((minutes == 1) ? "" : "s") + " ago";
        } else if (timeMillis < DAY) {
            int hours = (int) timeMillis / HOUR;
            return hours + " hour" + ((hours == 1) ? "" : "s") + " ago";
        } else if (timeMillis < WEEK) {
            int days = (int) timeMillis / DAY;
            return days + " day" + ((days == 1) ? "" : "s") + " ago";
        } else if (timeMillis < MONTH) {
            int weeks = (int) timeMillis / WEEK;
            return weeks + " week" + ((weeks == 1) ? "" : "s") + " ago";
        } else if (timeMillis < YEAR) {
            int months = (int) timeMillis / MONTH;
            return months + " month" + ((months == 1) ? "" : "s") + " ago";
        } else {
            int years = (int) timeMillis / YEAR;
            return years + " year" + ((years == 1) ? "" : "s") + " ago";
        }
    }

    public static String getTimestamp(int millisOffset) {
        return (new Timestamp(new Date().getTime() - millisOffset)).toString();
    }

    public static String getTimestamp() {
        return getTimestamp(0);
    }

    public static String formatDateTime(Context context, String timeToFormat) {

        String finalDateTime = "";

        SimpleDateFormat iso8601Format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.US);

        Date date = null;
        if (timeToFormat != null) {
            try {
                date = iso8601Format.parse(timeToFormat);
            } catch (ParseException e) {
                date = null;
            }

            if (date != null) {
                long when = date.getTime();
                int flags = 0;
                flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
                flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
                flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
                flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;

                finalDateTime = android.text.format.DateUtils.formatDateTime(context,
                        when + TimeZone.getDefault().getOffset(when), flags);
            }
        }
        return finalDateTime;
    }

    public static String dateToString(Date date) {
        String finalDateTime = "";

        SimpleDateFormat iso8601Format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.US);

        return iso8601Format.format(date);
    }
}
