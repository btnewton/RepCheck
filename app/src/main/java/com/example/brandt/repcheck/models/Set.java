package com.example.brandt.repcheck.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.brandt.repcheck.database.schemas.HistoryTable;
import com.example.brandt.repcheck.util.database.DBHandler;
import com.example.brandt.repcheck.util.database.DataObject;

import java.util.Date;


/**
 * Created by Brandt on 7/24/2015.
 */
public class Set extends DataObject {

    private int reps;
    private double weight;
    private boolean pinned;
    private Date lastUsed;

    public Set(int reps, double weight, boolean pinned, Date lastUsed) {
        super(HistoryTable.class, true);
        this.reps = reps;
        this.weight = weight;
        this.pinned = pinned;
        this.lastUsed = lastUsed;
    }

    private Set(int id, int reps, double weight, boolean pinned, String lastUsed) {
        this(reps, weight, pinned, DBHandler.convertStringTime(lastUsed));
        this.id = id;
        setIsNewRecord(false);
    }

    public static Set find(Context context, int id) {
        Cursor cursor = DBHandler.getReadable(context).query(HistoryTable.TABLE_NAME,
                new String[] {
                        HistoryTable.ID,
                        HistoryTable.REPS,
                        HistoryTable.WEIGHT,
                        HistoryTable.PINNED,
                        HistoryTable.LAST_USED,
                },
                HistoryTable.ID + "=?",
                new String[] {Integer.toString(id)},
                null, null, null, null);

        if (cursor.moveToFirst()) {

            return new Set(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getDouble(2),
                    cursor.getInt(3) == 1,
                    cursor.getString(4)
            );
        } else {
            return null;
        }
    }

    public int getReps() {
        return reps;
    }

    public double getWeight() {
        return weight;
    }

    public Date getLastUsed() {
        return lastUsed;
    }

    @Override
    protected ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(HistoryTable.ID, id);
        contentValues.put(HistoryTable.REPS, reps);
        contentValues.put(HistoryTable.WEIGHT, weight);
        contentValues.put(HistoryTable.PINNED, pinned? 1 : 0);
        contentValues.put(HistoryTable.LAST_USED, DBHandler.dateToString(lastUsed));
        return contentValues;
    }
}
