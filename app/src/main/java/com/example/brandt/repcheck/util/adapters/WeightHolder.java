package com.example.brandt.repcheck.util.adapters;

/**
 * Created by Brandt on 7/23/2015.
 */
public class WeightHolder {

    private String reps;
    private String weight;

    public WeightHolder(String reps, String weight) {
        this.reps = reps;
        this.weight = weight;
    }

    public WeightHolder(int reps, double weight) {
        this.reps = Integer.toString(reps);
        this.weight = Double.toString(weight);
    }

    public WeightHolder(int reps, String weight) {
        this.reps = Integer.toString(reps);
        this.weight = weight;
    }

    public String getWeight() {
        return weight;
    }

    public String getReps() {
        return reps;
    }
}
