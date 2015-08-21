package com.brandtnewtonsoftware.rep_check.models;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Brandt on 8/7/2015.
 */
public final class WeightFormatter {

    private final NumberFormat formatter;
    private final Unit unit;

    public WeightFormatter(boolean shouldRound, Unit unit) {
        if (shouldRound) {
            formatter = new DecimalFormat("#0");
        } else {
            formatter = new DecimalFormat("#0.0");
        }

        this.unit = unit;
    }

    public String format(double number) {
        return formatter.format(number);
    }

    public Unit getUnit() { return unit; }
    public String displayUnit(double number) { return unit.displayUnit(number); }
}
