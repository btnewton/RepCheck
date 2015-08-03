package com.example.brandt.repcheck.database.seeders;

import android.content.Context;

import com.example.brandt.repcheck.R;
import com.example.brandt.repcheck.models.FormulaConfiguration;
import com.example.brandt.repcheck.models.calculations.formulas.FormulaType;
import com.example.brandt.repcheck.util.database.Seeder;

/**
 * Created by Brandt on 8/2/2015.
 */
public class FormulaConfigurationSeeder extends Seeder {

    @Override
    public void seed(Context context) {

    }

    /**
     * Searches database for missing configurations and adds default where none are found.
     * @param context
     */
    public void repair(Context context) {
        final int maxReps = context.getResources().getInteger(R.integer.max_reps);
        for (int i = 0; i < maxReps; i++) {
            int reps = i + 1;
            FormulaConfiguration[] configurations = FormulaConfiguration.getAllForReps(context, reps);
            if (configurations == null || ! validConfiguration(configurations)) {
                FormulaConfiguration defaultFormula = findDefaultFormula(configurations, reps);
                defaultFormula.setIsUsed(true);
                defaultFormula.saveChanges(context);
            }
        }
    }

    private FormulaConfiguration findDefaultFormula(FormulaConfiguration[] configurations, int reps) {
        if (configurations != null) {
            for (FormulaConfiguration config : configurations) {
                if (config.getFormula() == FormulaType.Brzycki) {
                    return config;
                }
            }
        }
        return new FormulaConfiguration(reps, FormulaType.Brzycki, true);
    }

    private boolean validConfiguration(FormulaConfiguration[] configurations) {
        for (FormulaConfiguration config : configurations) {
            if (config.isUsed()) {
                return true;
            }
        }
        return false;
    }
}
