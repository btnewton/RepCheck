package com.example.brandt.repcheck.database.schemas;

import com.example.brandt.repcheck.util.database.Schema;

/**
* Created by brandt on 7/22/15.
*/
public class HistoryTable extends Schema {

    public static final String TABLE_NAME = "history";
    public static final String ID = "id";
    public static final String REPS = "reps";
    public static final String WEIGHT = "weight";
    public static final String PINNED = "weight";
    public static final String TIME_CREATED = "time_consumed";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String tableSQL() {
        return
                "CREATE TABLE "
                        + TABLE_NAME + "("
                        + ID          + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + REPS     + " INTEGER NOT NULL, "
                        + WEIGHT + " DOUBLE NOT NULL, "
                        + PINNED        + " BOOLEAN DEFAULT 0, "
                        + TIME_CREATED  + " TEXT NOT NULL, "             // SQLite Timestamp
                        + ")";
    }
}
