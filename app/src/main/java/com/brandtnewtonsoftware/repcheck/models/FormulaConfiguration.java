package com.brandtnewtonsoftware.repcheck.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.brandtnewtonsoftware.repcheck.database.schemas.FormulaConfigurationTable;
import com.brandtnewtonsoftware.repcheck.models.calculations.formulas.FormulaType;
import com.brandtnewtonsoftware.repcheck.util.database.DataObject;
import com.brandtnewtonsoftware.repcheck.util.database.QueryParams.QueryParams;

/**
 * Created by Brandt on 8/2/2015.
 */
public class FormulaConfiguration extends DataObject {

    private int reps;
    private FormulaType formula;
    private boolean isUsed;

    private FormulaConfiguration() {
        super(FormulaConfigurationTable.class, true);
    }

    public FormulaConfiguration(int reps, FormulaType formula, boolean isUsed) {
        this();
        setReps(reps);
        this.formula = formula;
        this.isUsed = isUsed;
    }

    private FormulaConfiguration(int id, int reps, FormulaType formula, boolean isUsed) {
        this(reps, formula, isUsed);
        setIsNewRecord(false);
        this.id = id;
    }

    public static FormulaConfiguration[] getAllForReps(Context context, int reps) {
        QueryParams queryParams = new QueryParams();
        queryParams.selection = FormulaConfigurationTable.REPS + "=?";
        queryParams.selectionArgs = new String[] {Integer.toString(reps)};
        FormulaConfiguration formula = new FormulaConfiguration();
        return formula.select(context, queryParams, formula.getClass());
    }

    @Override
    protected ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FormulaConfigurationTable.ID, id);
        contentValues.put(FormulaConfigurationTable.REPS, reps);
        contentValues.put(FormulaConfigurationTable.FORMULA, formula.toString());
        contentValues.put(FormulaConfigurationTable.IS_USED, isUsed? 1 : 0);
        return contentValues;
    }

    @Override
    protected DataObject bindCursor(Cursor cursor) {
        return new FormulaConfiguration(
            cursor.getInt(0),
            cursor.getInt(1),
            FormulaType.valueOf(cursor.getString(2)),
            cursor.getInt(3) == 1
        );
    }

    @Override
    public boolean hasChanged() {
        return false;
    }

    @Override
    public void resetSnapshot() {

    }

    @Override
    public void rollbackChanges(Context context) {

    }

    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public FormulaType getFormula() {
        return formula;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setReps(int reps) {
        if (reps <= 0) {
            return;
        }
        this.reps = reps;
    }
}
