package com.example.brandt.repcheck.util.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.brandt.repcheck.util.database.QueryParams.QueryParams;

/**
 * Created by Brandt on 7/24/2015.
 */
public abstract class DataObject {
    protected int id;
    private boolean isNewRecord;
    private static Schema table;
    private static int lastInesertID;

    protected DataObject(Class<?> tableType, boolean isNewRecord) {
        if (table == null) {
            try {
                table = (Schema) tableType.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

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

    public Object find(Context context, int id) {
        Cursor cursor = DBHandler.getReadable(context).query(getTableName(),
                getColumns(),
                "id=?",
                new String[] {Integer.toString(id)},
                null, null, null, null);

        if (cursor.moveToFirst()) {
            Object object = bindCursor(cursor);
            cursor.close();
            return object;
        } else {
            cursor.close();
            return null;
        }
    }

    public static int getLastInesertID() {
        return lastInesertID;
    }

    protected abstract ContentValues getContentValues();

    public boolean delete(Context context) {
        String whereClause = "id=?";
        String[] whereArgs = { Integer.toString(id) };
        return DBHandler.getWritable(context)
                .delete(getTableName(), whereClause, whereArgs) > 0;
    }

    protected abstract Object bindCursor(Cursor cursor);

    public Object[] selectAll(Context context) {
        return selectAll(context, new QueryParams());
    }

    public Object[] selectAll(Context context, QueryParams queryParams) {
        Cursor cursor = DBHandler.getReadable(context).query(table.getTableName(),
                table.getColumns(),
                null, null, queryParams.groupBy, null, queryParams.orderBy, queryParams.limit);

        if (cursor.moveToFirst()) {
            Object[] objects = new Object[cursor.getCount()];

            do {
                objects[cursor.getPosition()] = bindCursor(cursor);
            } while (cursor.moveToNext());

            return objects;
        } else {
            cursor.close();
            return null;
        }
    }

    protected String[] getColumns() {
        return table.getColumns();
    }

    protected String getTableName() {
        return table.getTableName();
    }
}
