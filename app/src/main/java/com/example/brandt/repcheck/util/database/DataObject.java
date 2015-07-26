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
    private static Schema table;
    private static int lastInsertID;

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

        this.isNewRecord = isNewRecord;
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
            if (contentValues.containsKey("id")) {
                contentValues.remove("id");
            }
            lastInsertID = (int) (long) DBHandler.getWritable(context).
                    insert(getTableName(), null, contentValues);
            return lastInsertID != -1;
        } else {
            String whereClause = "id=?";
            String[] whereArgs = new String[]{ Integer.toString(id) };

            return DBHandler.getWritable(context).
                    update(getTableName(), getContentValues(), whereClause, whereArgs) > 0;
        }
    }

    public <T extends DataObject> T find(Context context, int id, T returnType) {
        Cursor cursor = DBHandler.getReadable(context).query(getTableName(),
                getColumns(),
                "id=?",
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
        String whereClause = "id=?";
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

    public Object[] selectAll(Context context) {
        return selectAll(context, new QueryParams(), this.getClass());
    }

    public <T extends DataObject> T[] selectAll(Context context, QueryParams queryParams, Class<T> c) {
        Cursor cursor = DBHandler.getReadable(context).query(table.getTableName(),
                table.getColumns(),
                null, null, queryParams.groupBy, null, queryParams.orderBy, queryParams.limit);

        if (cursor.moveToFirst()) {
            final T[] objects = (T[]) Array.newInstance(c, cursor.getCount());

            do {
                objects[cursor.getPosition()] = (T) bindCursor(cursor);
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
