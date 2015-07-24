package com.example.brandt.repcheck.models;

/**
 * Created by Brandt on 7/23/2015.
 */
public class Unit {

    private String unit;

    private Unit(String unit) {
        this.unit = unit;
    }

    public static Unit ImperialUnit() {
        return new Unit("lb");
    }

    public static Unit MetricUnit() {
        return new Unit("kg");
    }

    public String getUnit() {
        return unit;
    }
}