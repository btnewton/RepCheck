package com.brandtnewtonsoftware.repcheck.database.schemas;

import com.brandtnewtonsoftware.repcheck.util.database.Schema;

/**
* Created by brandt on 7/22/15.
*/
public class SetSlotTable extends Schema {

    public static final String TABLE_NAME = "SetSlots";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String REPS = "reps";
    public static final String WEIGHT = "weight";
    public static final String LAST_USED = "last_used";

    public static final String[] columns = new String[]{
            SetSlotTable.ID,
            SetSlotTable.NAME,
            SetSlotTable.REPS,
            SetSlotTable.WEIGHT,
            SetSlotTable.LAST_USED,
    };

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
                        + NAME          + " TEXT NOT NULL UNIQUE, "
                        + REPS     + " INTEGER NOT NULL, "
                        + WEIGHT + " DOUBLE NOT NULL, "
                        + LAST_USED + " TEXT NOT NULL "             // SQLite Timestamp
                        + ")";
    }

    @Override
    public String[] getColumns() {
        return columns;
    }
}
