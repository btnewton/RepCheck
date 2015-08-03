package com.example.brandt.repcheck.database.schemas;

import com.example.brandt.repcheck.util.database.Schema;

/**
 * Created by Brandt on 8/2/2015.
 */
public class FormulaConfigurationTable extends Schema {

    public static final String TABLE_NAME = "FormulaConfiguration";
    public static final String ID = "id";
    public static final String REPS = "reps";
    public static final String FORMULA = "formula";
    public static final String IS_USED = "isUsed";

    public static final String[] columns = new String[]{
            FormulaConfigurationTable.ID,
            FormulaConfigurationTable.REPS,
            FormulaConfigurationTable.FORMULA,
            FormulaConfigurationTable.IS_USED,
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
                + REPS          + " INTEGER NOT NULL UNIQUE, "
                + FORMULA     + " INTEGER NOT NULL, "
                + IS_USED + " BOOLEAN DEFAULT FALSE "
                + ")";
    }

    @Override
    public String[] getColumns() {
        return columns;
    }
}
