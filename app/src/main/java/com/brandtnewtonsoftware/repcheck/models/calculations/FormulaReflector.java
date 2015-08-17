package com.brandtnewtonsoftware.repcheck.models.calculations;

import com.brandtnewtonsoftware.repcheck.models.calculations.formulas.OneRepMaxFormula;

/**
 * Created by Brandt on 8/16/2015.
 */
public class FormulaReflector {

    public static final String LOG_KEY = "FormulaReflector";

    public static OneRepMaxFormula reflectOneRepMaxFormula(String formulaName) throws Exception {
        OneRepMaxFormula formula;

        try {
            formula = (OneRepMaxFormula) Class.forName("com.brandtnewtonsoftware.repcheck.models.calculations.formulas." + formulaName).getConstructor().newInstance();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return formula;
    }
}
