package com.example.brandt.repcheck.models.calculations;

import com.example.brandt.repcheck.models.WeightFormatter;
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

    private List<IDetailRow> weightHolders;
    private OneRepMaxFormula oneRepMaxFormula;
    private final int MAX_REPS;
    private WeightFormatter formatter;

    public FormulaWrapper(OneRepMaxFormula oneRepMaxFormula, WeightFormatter formatter, int setRange) {
        MAX_REPS = setRange;
        this.oneRepMaxFormula = oneRepMaxFormula;
        this.formatter = formatter;
    }

    public void calculateSets(int reps, double weight) {
        oneRepMaxFormula.update(reps, weight);

        weightHolders = new ArrayList<>(MAX_REPS);

        for (int i = 0; i < MAX_REPS; i++) {
            int currentReps = i + 1;
            double currentWeight = oneRepMaxFormula.getWeightWeightForReps(currentReps);
            weightHolders.add(new DetailRow(currentReps, Integer.toString(currentReps),
                            formatter.format(currentWeight) + " " + formatter.getUnit(currentWeight),
                    Integer.toString((int)oneRepMaxFormula.getPercentOfMax(currentWeight)) + "%"));
        }
        setChanged();
        notifyObservers();
    }

    public List<IDetailRow> getSets() {
        return weightHolders;
    }
}
