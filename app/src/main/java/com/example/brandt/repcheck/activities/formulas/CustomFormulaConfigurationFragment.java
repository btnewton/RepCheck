package com.example.brandt.repcheck.activities.formulas;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

import com.example.brandt.repcheck.R;
import com.example.brandt.repcheck.models.RepFormulaConfigurationHolder;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Brandt on 8/2/2015.
 */
public class CustomFormulaConfigurationFragment extends Fragment implements Observer {

    private RepFormulaConfigurationHolder[] formulaHolders;
    private Handler asyncHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // TODO load current config
    }


    public void refreshData() {
        AsyncSelect asyncSelect = new AsyncSelect();
        asyncSelect.addObserver(this);
        new Thread(asyncSelect).start();
    }

    @Override
    public void update(Observable observable, Object data) {

    }

    private class AsyncSelect extends Observable implements Runnable {
        @Override
        public void run() {

            formulaHolders = new RepFormulaConfigurationHolder[getResources().getInteger(R.integer.max_reps)];

            for (int i = 0; i < formulaHolders.length; i++) {
                formulaHolders[i] = new RepFormulaConfigurationHolder(i + 1);
                formulaHolders[i].populate(getActivity());
            }

            asyncHandler.post(new Runnable() {
                @Override
                public void run() {
                    setChanged();
                    notifyObservers();
                }
            });
        }
    }
}
