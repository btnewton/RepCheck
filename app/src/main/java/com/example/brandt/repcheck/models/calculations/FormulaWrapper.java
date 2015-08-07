package com.example.brandt.repcheck.models.calculations;

import com.example.brandt.repcheck.models.SetSlot;
import com.example.brandt.repcheck.models.Unit;
import com.example.brandt.repcheck.models.WeightFormatter;
import com.example.brandt.repcheck.models.calculations.formulas.BrzyckiFormula;
import com.example.brandt.repcheck.models.calculations.formulas.OneRepMaxFormula;
import com.example.brandt.repcheck.util.adapters.detail.DetailRow;
import com.example.brandt.repcheck.util.adapters.detail.IDetailRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by Brandt on 7/23/2015.
 */
public class FormulaWrapper extends Observable {

    private int reps;
    private double weight;
    private Unit unit;
    private List<IDetailRow> weightHolders;
    private OneRepMaxFormula oneRepMaxFormula;
    private final int MAX_REPS;
    private WeightFormatter weightFormatter;

    public FormulaWrapper(SetSlot setSlot, int setRange) {
        this.reps = setSlot.getReps();
        this.weight = setSlot.getWeight();
        unit = Unit.ImperialUnit();
        weightHolders = new ArrayList<>(setRange);
        MAX_REPS = setRange;
        oneRepMaxFormula = new BrzyckiFormula();
        weightFormatter = new WeightFormatter(true);
    }

    public void setFormula(OneRepMaxFormula oneRepMaxFormula) {
        if (this.oneRepMaxFormula != oneRepMaxFormula) {
            this.oneRepMaxFormula = oneRepMaxFormula;
        }
    }

    public void update(SetSlot setSlot) {
        if (reps != setSlot.getReps() || weight != setSlot.getWeight()) {
            reps = setSlot.getReps();
            weight = setSlot.getWeight();
        }
    }

    public void setWeight(double weight) {
        if (this.weight != weight) {
            this.weight = weight;
        }
    }

    public void setReps(int reps) {
        if (this.reps != reps) {
            this.reps = reps;
        }
    }

    public void calculateSets() {
        oneRepMaxFormula.update(reps, weight);

        weightHolders = new ArrayList<>(MAX_REPS);

        for (int i = 0; i < MAX_REPS; i++) {
            int reps = i + 1;
            double weight = oneRepMaxFormula.getWeightWeightForReps(reps);
            weightHolders.add(new DetailRow(0, Integer.toString(reps),
                    weightFormatter.format(weight) + " " + unit.displayUnit(weight),
                    Integer.toString((int)oneRepMaxFormula.getPercentOfMax(weight)) + "%"));
        }
        setChanged();
        notifyObservers();
    }

    public void setRoundCalculations(WeightFormatter weightFormatter) {
        this.weightFormatter = weightFormatter;
    }

    public List<IDetailRow> getSets() {
        return weightHolders;
    }

    public double getWeight() {
        return weight;
    }

    public int getReps() {
        return reps;
    }

    public void setUnit(Unit unit) {
        if ( ! this.unit.equals(unit)) {
            this.unit = unit;
        }
    }
}
