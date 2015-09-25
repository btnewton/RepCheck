package com.brandtnewtonsoftware.rep_check.util.adapters.detail;

/**
 * Created by Brandt on 8/4/2015.
 */
public final class SetRowItem implements ISetRowItem {
    private final String reps;
    private final String helperText;
    private final String weight;
    private final String percentMax;

    public SetRowItem(int reps, String weight, String percentMax) {
        this.reps = Integer.toString(reps);
        helperText = reps > 1? "reps at" : "rep at";
        this.weight = weight;
        this.percentMax = percentMax;
    }

    @Override
    public String getHelperText() {
        return helperText;
    }

    @Override
    public String getReps() {
        return reps;
    }

    @Override
    public String getWeight() {
        return weight;
    }

    @Override
    public String getPercentMax() {
        return percentMax;
    }
}
