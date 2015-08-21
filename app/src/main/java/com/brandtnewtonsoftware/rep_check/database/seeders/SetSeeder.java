package com.brandtnewtonsoftware.rep_check.database.seeders;

import android.content.Context;

import com.brandtnewtonsoftware.rep_check.R;
import com.brandtnewtonsoftware.rep_check.models.SetSlot;
import com.brandtnewtonsoftware.rep_check.util.database.Seeder;

import java.util.Random;

/**
 * Created by brandt on 7/25/15.
 */
public class SetSeeder extends Seeder {

    @Override
    public void seed(Context context) {
        Random random = new Random();

        int now = (int) (System.currentTimeMillis() * 1000);
        int setCount = context.getResources().getInteger(R.integer.set_slot_count);
        int maxReps = context.getResources().getInteger(R.integer.max_reps);


        for (int i = 0; i < setCount; i++) {
            SetSlot set = new SetSlot(random.nextInt(maxReps - 1) + 1, random.nextInt(150) + 50);
            set.setName("Set " + (i + 1));
            set.saveChanges(context);
        }
    }
}
