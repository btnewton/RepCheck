package com.example.brandt.repcheck.models.calculations;

import com.example.brandt.repcheck.models.SetSlot;
import com.example.brandt.repcheck.models.Unit;
import com.example.brandt.repcheck.util.adapters.WeightHolder;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Observable;

/**
 * Created by Brandt on 7/23/2015.
 */
public class FormulaWrapper extends Observable {

    private int reps;
    private double weight;
    private Unit unit;
    private WeightHolder[] weightHolders;
    private int setRange;

    public FormulaWrapper(SetSlot setSlot, int setRange) {
        this.reps = setSlot.getReps();
        this.weight = setSlot.getWeight();
        unit = Unit.ImperialUnit();
        this.setRange = setRange;
        weightHolders = new WeightHolder[setRange];
    }

    public void update(SetSlot setSlot) {
        reps = setSlot.getReps();
        weight = setSlot.getWeight();
        calculateSets();
    }

    public void incrementWeight(double incrementValue) {
        weight += incrementValue;
        calculateSets();
    }

    public void setWeight(double weight) {
        this.weight = weight;
        calculateSets();
    }

    public void calculateSets() {
        NumberFormat formatter = getFormatter();
        Brzycki brzycki = new Brzycki(reps, weight);

        for (int i = 0; i < weightHolders.length; i++) {
            int reps = i + 1;
            double weight = brzycki.getWeightForReps(reps);
            weightHolders[i] = new WeightHolder(reps, formatter.format(weight) + " " + unit.getUnit() + ((weight != 1)? "s" : ""));
        }

        notifyObservers();
    }

    private NumberFormat getFormatter() {
        return new DecimalFormat("#0.00");
    }

    public WeightHolder[] getSets() {
        return weightHolders;
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
        calculateSets();
    }
}
