package com.example.brandt.repcheck.util.database;

import android.content.ContentValues;
import android.content.Context;

import com.example.brandt.repcheck.database.schemas.HistoryTable;

/**
 * Created by Brandt on 7/24/2015.
 */
public abstract class DataObject {
    protected int id;
    private boolean isNewRecord;
    private Class<Schema> table;
    private String tableName;
    private static int lastInesertID;

    protected DataObject(Class<?> table, boolean isNewRecord) {
        if (table.isAssignableFrom(Schema.class))
            this.table = (Class<Schema>) table;

        this.isNewRecord = isNewRecord;
    }

    public void setId(int id) {
        this.id = id;
    }

    protected void setIsNewRecord(boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }


    public boolean saveChanges(Context context) {
        if (isNewRecord) {
            lastInesertID = (int) (long) DBHandler.getWritable(context).
                    insert(getTableName(), null, getContentValues());
            return lastInesertID != -1;
        } else {
            String whereClause = "id=?";
            String[] whereArgs = new String[]{ Integer.toString(id) };

            return DBHandler.getWritable(context).
                    update(getTableName(), getContentValues(), whereClause, whereArgs) > 0;
        }
    }

    public static int getLastInesertID() {
        return lastInesertID;
    }

    protected abstract ContentValues getContentValues();

    public boolean delete(Context context) {
        String whereClause = HistoryTable.ID + "=?";
        String[] whereArgs = { Integer.toString(id) };
        return DBHandler.getWritable(context)
                .delete(getTableName(), whereClause, whereArgs) > 0;
    }

    private String getTableName() {

        if (tableName != null) {
            return tableName;
        }

        try {
            tableName = table.newInstance().getTableName();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return tableName;
    }
}
