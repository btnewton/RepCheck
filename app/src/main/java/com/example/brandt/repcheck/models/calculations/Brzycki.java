package com.example.brandt.repcheck.models.calculations;

/**
 * Created by Brandt on 7/23/2015.
 */
public class Brzycki {

    int reps;
    double weight;
    double max;

    public Brzycki(int reps, double weight) {
        this.weight = weight;
        this.reps = reps;
        calculateMax();
    }

    private void calculateMax() {
        max = weight * 36 / (37 - reps);
    }

    public double getWeightForReps(int reps) {
        if (reps == 1) {
            return max;
        }

        if (reps < 0) {
            throw new UnsupportedOperationException("Invalid rep count");
        }

        return max * (37 - reps) / 36;
    }
}
