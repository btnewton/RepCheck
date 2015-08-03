package com.example.brandt.repcheck.models.calculations;

import com.example.brandt.repcheck.models.SetSlot;
import com.example.brandt.repcheck.models.Unit;
import com.example.brandt.repcheck.models.calculations.formulas.BrzyckiFormula;
import com.example.brandt.repcheck.models.calculations.formulas.OneRepMaxFormula;
import com.example.brandt.repcheck.util.adapters.StandardRowItem;

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
    private StandardRowItem[] weightHolders;
    private OneRepMaxFormula oneRepMaxFormula;

    public FormulaWrapper(SetSlot setSlot, int setRange) {
        this.reps = setSlot.getReps();
        this.weight = setSlot.getWeight();
        unit = Unit.ImperialUnit();
        weightHolders = new StandardRowItem[setRange];
        oneRepMaxFormula = new BrzyckiFormula();
    }

    public void setFormula(OneRepMaxFormula oneRepMaxFormula) {
        this.oneRepMaxFormula = oneRepMaxFormula;
    }

    public void update(SetSlot setSlot) {
        if (reps != setSlot.getReps() || weight != setSlot.getWeight()) {
            reps = setSlot.getReps();
            weight = setSlot.getWeight();
            calculateSets();
        }
    }

    public void setWeight(double weight) {
        if (this.weight != weight) {
            this.weight = weight;
            calculateSets();
        }
    }

    public void setReps(int reps) {
        if (this.reps != reps) {
            this.reps = reps;
            calculateSets();
        }
    }

    public void calculateSets() {
        NumberFormat formatter = getFormatter();

        oneRepMaxFormula.update(reps, weight);

        for (int i = 0; i < weightHolders.length; i++) {
            int reps = i + 1;
            double weight = oneRepMaxFormula.getWeightWeightForReps(reps);
            weightHolders[i] = new StandardRowItem(0, Integer.toString(reps), formatter.format(weight) + " " + unit.getUnit() + ((weight != 1)? "s" : ""));
        }

        setChanged();
        notifyObservers();
    }

    private NumberFormat getFormatter() {
        return new DecimalFormat("#0.00");
    }

    public StandardRowItem[] getSets() {
        return weightHolders;
    }

    public double getWeight() {
        return weight;
    }

    public int getReps() {
        return reps;
    }


    public void setUnit(Unit unit) {
        this.unit = unit;
        calculateSets();
    }
}
