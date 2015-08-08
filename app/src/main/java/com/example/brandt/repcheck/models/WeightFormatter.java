package com.example.brandt.repcheck.models;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Brandt on 8/7/2015.
 */
public class WeightFormatter {

    private NumberFormat formatter;
    private Unit unit;

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

    public String getUnit() { return unit.displayUnit(); }
    public String getUnit(double number) { return unit.displayUnit(number); }
}
