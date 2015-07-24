package com.example.brandt.repcheck.models.calculations;

import com.example.brandt.repcheck.models.Unit;
import com.example.brandt.repcheck.util.adapters.WeightHolder;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Brandt on 7/23/2015.
 */
public class FormulaWrapper {

    private int reps;
    private double weight;
    private boolean isHalfWeight;
    private double baseWeight;
    private boolean shouldFormat;
    private Unit unit;

    public FormulaWrapper(int reps, double weight) {
        this.reps = reps;
        this.weight = weight;
        isHalfWeight = false;
        baseWeight = 0;
        shouldFormat = false;
        unit = Unit.ImperialUnit();
    }

    public void setIsHalfWeight(boolean isHalfWeight) {
        this.isHalfWeight = isHalfWeight;
    }

    public void setBaseWeight(double baseWeight) {
        this.baseWeight = baseWeight;
    }

    public void setShouldFormat(boolean shouldFormat) {
        this.shouldFormat = shouldFormat;
    }

    public void incrementWeight(double incrementValue) {
        weight += incrementValue;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getTotalWeight() {
        double totalWeight = weight;

        if (isHalfWeight)
            totalWeight *= 2;

        totalWeight += baseWeight;

        return totalWeight;
    }

    public double getWeightForReps(int index) {
        Brzycki brzycki = new Brzycki(reps, getTotalWeight());
        return brzycki.getWeightForReps(index);
    }

    public double[] getWeightForRepRange(int size) {
        Brzycki brzycki = new Brzycki(reps, getTotalWeight());
        double[] weights = new double[size];

        for (int i = 0; i < size; i++) {
            int reps = i + 1;
            weights[i] = brzycki.getWeightForReps(reps);
        }

        return weights;
    }

    public WeightHolder[] getWeightForRepRangeAsWeightHolderArray(int size) {

        NumberFormat formatter = getFormatter();
        Brzycki brzycki = new Brzycki(reps, getTotalWeight());

        WeightHolder[] weightHolders = new WeightHolder[size];

        for (int i = 0; i < size; i++) {
            int reps = i + 1;
            double weight = brzycki.getWeightForReps(reps);
            weightHolders[i] = new WeightHolder(reps, formatter.format(weight) + " " + unit.getUnit() + ((weight != 1)? "s" : ""));
        }

        return weightHolders;
    }

    private NumberFormat getFormatter() {
        NumberFormat formatter;

        if (shouldFormat) {
            formatter = new DecimalFormat("#0");
        } else {
            formatter = new DecimalFormat("#0.00");
        }

        return formatter;
    }

    public String getWeightAsString(int index) {
        return getFormatter().format(getWeightForReps(index));
    }

    public double getWeight() {
        return weight;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}
