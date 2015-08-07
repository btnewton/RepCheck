package com.example.brandt.repcheck.models;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Brandt on 8/7/2015.
 */
public class WeightFormatter {

    private NumberFormat formatter;

    public WeightFormatter(boolean shouldRound) {
        if (shouldRound) {
            formatter = new DecimalFormat("#0");
        } else {
            formatter = new DecimalFormat("#0.0");
        }
    }

    public String format(double number) {
        return formatter.format(number);
    }
}
