package com.brandtnewtonsoftware.repcheck.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.brandtnewtonsoftware.repcheck.database.schemas.SetSlotTable;
import com.brandtnewtonsoftware.repcheck.util.database.DBHandler;
import com.brandtnewtonsoftware.repcheck.util.database.DataObject;
import com.brandtnewtonsoftware.repcheck.util.database.QueryParams.QueryParams;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * Created by Brandt on 7/24/2015.
 */
public class SetSlot extends DataObject {

    private String name;
    private int reps;
    private double weight;
    private Date lastUsed;

    private int originalReps;
    private double originalWeight;

    private SetSlot() {
        super(SetSlotTable.class, true);

        if (lastUsed == null)
            lastUsed = new Date();
    }

    public SetSlot(int reps, double weight) {
        this();
        setReps(reps);
        setWeight(weight);
        resetSnapshot();
    }

    public SetSlot(int reps, double weight, Date lastUsed) {
        this(reps, weight);
        this.lastUsed = lastUsed;
    }

    private SetSlot(int id, int reps, double weight, String lastUsed) {
        this(reps, weight, DBHandler.convertStringTime(lastUsed));
        this.id = id;
        setNotNewRecord();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static List<SetSlot> selectAllByDate(Context context) {
        QueryParams queryParams = new QueryParams();
        queryParams.orderBy = SetSlotTable.LAST_USED + " DESC";
        SetSlot setSlot = new SetSlot();
        SetSlot[] results = setSlot.select(context, queryParams, setSlot.getClass());
        if (results != null) {
            return Arrays.asList(results);
        } else {
            return null;
        }
    }

    public static SetSlot findByName(Context context, String name) {
        QueryParams queryParams = new QueryParams();
        queryParams.selection = SetSlotTable.NAME + " LIKE ?";
        queryParams.selectionArgs = new String[] {name};
        SetSlot setSlot = new SetSlot();
        return setSlot.select(context, queryParams, setSlot.getClass())[0];
    }

    public boolean nameUnique(Context context) {
        QueryParams queryParams = new QueryParams();
        queryParams.selection = SetSlotTable.NAME + " LIKE ? AND " + SetSlotTable.ID + " != ?";
        queryParams.selectionArgs = new String[] {name, String.valueOf(id)};
        SetSlot setSlot = new SetSlot();
        return setSlot.select(context, queryParams, setSlot.getClass()) == null;
    }

    public static SetSlot first(Context context) {
        QueryParams queryParams = new QueryParams();
        queryParams.orderBy = SetSlotTable.LAST_USED + " DESC";
        queryParams.limit = "1";
        SetSlot setSlot = new SetSlot();
        SetSlot[] objects = setSlot.select(context, queryParams, setSlot.getClass());
        if (objects != null) {
            return objects[0];
        } else {
            return null;
        }
    }

    public static int getSlotCount(Context context) {
        return new SetSlot().getCount(context);
    }

    public static SetSlot findById(Context context, int id) {
        SetSlot setSlot = new SetSlot();
        return setSlot.find(context, id, setSlot);
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        if (reps <= 0) {
            reps = 1;
        }
        this.reps = reps;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        if (weight <= 0) {
            weight = 0;
        }
        this.weight = weight;
    }

    public void reloadName(Context context) {
        name = new SetSlot().find(context, id, this).getName();
    }

    public Date getLastUsed() {
        return lastUsed;
    }

    @Override
    protected ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SetSlotTable.ID, id);
        contentValues.put(SetSlotTable.NAME, name);
        contentValues.put(SetSlotTable.REPS, reps);
        contentValues.put(SetSlotTable.WEIGHT, weight);
        contentValues.put(SetSlotTable.LAST_USED, DBHandler.dateToString(lastUsed));
        return contentValues;
    }

    @Override
    protected DataObject bindCursor(Cursor cursor) {
        SetSlot set = new SetSlot(
                cursor.getInt(0),
                cursor.getInt(2),
                cursor.getDouble(3),
                cursor.getString(4)
        );
        set.setName(cursor.getString(1));
        return set;
    }

    @Override
    public boolean hasChanged() {
        return weight != originalWeight || reps != originalReps;
    }

    public void rollbackChanges(Context context) {
        setWeight(originalWeight);
        setReps(originalReps);
        saveChanges(context);
    }

    public void resetSnapshot() {
        originalReps = reps;
        originalWeight = weight;
    }
}
