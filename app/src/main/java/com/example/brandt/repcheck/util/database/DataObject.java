package com.example.brandt.repcheck.util.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.brandt.repcheck.util.database.QueryParams.QueryParams;

import java.lang.reflect.Array;

/**
 * Created by Brandt on 7/24/2015.
 */
public abstract class DataObject {
    protected int id;
    private boolean isNewRecord;
    private Schema table;
    private static int lastInsertID;
    private String primaryKey;

    protected <T extends Schema> DataObject(Class<T> tableType, boolean isNewRecord) {
        if (table == null) {
            try {
                table = tableType.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        primaryKey = "id";
        this.isNewRecord = isNewRecord;

        resetSnapshot();
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    protected void setIsNewRecord(boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    public boolean saveChanges(Context context) {
        if (isNewRecord) {
            ContentValues contentValues = getContentValues();
            if (contentValues.containsKey(primaryKey)) {
                contentValues.remove(primaryKey);
            }
            lastInsertID = (int) (long) DBHandler.getWritable(context).
                    insert(getTableName(), null, contentValues);
            return lastInsertID != -1;
        } else {
            String whereClause = primaryKey + "=?";
            String[] whereArgs = new String[]{ Integer.toString(id) };

            return DBHandler.getWritable(context).
                    update(getTableName(), getContentValues(), whereClause, whereArgs) > 0;
        }
    }

    public int getCount(Context context) {
        Cursor cursor = DBHandler.getReadable(context).query(getTableName(),
                getColumns(),
                null, null, null, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public <T extends DataObject> T find(Context context, int id, T returnType) {
        Cursor cursor = DBHandler.getReadable(context).query(getTableName(),
                getColumns(),
                primaryKey + "=?",
                new String[] {Integer.toString(id)},
                null, null, null, null);

        if (cursor.moveToFirst()) {
            T object = (T) bindCursor(cursor);
            cursor.close();
            return object;
        } else {
            cursor.close();
            return null;
        }
    }

    public static int getLastInsertID() {
        return lastInsertID;
    }

    protected abstract ContentValues getContentValues();

    public boolean delete(Context context) {
        String whereClause = primaryKey + "=?";
        String[] whereArgs = { Integer.toString(id) };
        if (DBHandler.getWritable(context)
                .delete(getTableName(), whereClause, whereArgs) > 0) {
            isNewRecord = true;
            id = -1;
            return true;
        } else {
            return false;
        }
    }

    protected abstract DataObject bindCursor(Cursor cursor);

    public <T extends DataObject> T[] select(Context context, QueryParams queryParams, Class<T> c) {
        Cursor cursor = DBHandler.getReadable(context).query(table.getTableName(),
                table.getColumns(),
                queryParams.selection, queryParams.selectionArgs, queryParams.groupBy, queryParams.having, queryParams.orderBy, queryParams.limit);

        if (cursor.moveToFirst()) {
            final T[] objects = (T[]) Array.newInstance(c, cursor.getCount());

            do {
                objects[cursor.getPosition()] = (T) bindCursor(cursor);
            } while (cursor.moveToNext());

            cursor.close();
            return objects;
        } else {
            cursor.close();
            return null;
        }
    }

    public abstract boolean hasChanged();

    public abstract void resetSnapshot();

    public abstract void rollbackChanges(Context context);

    public void truncateTable(Context context) {
        table.truncate(context);
    }

    protected String[] getColumns() {
        return table.getColumns();
    }

    protected String getTableName() {
        return table.getTableName();
    }
}
