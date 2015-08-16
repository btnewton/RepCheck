package com.brandtnewtonsoftware.repcheck.database.seeders;

import android.content.Context;

import com.brandtnewtonsoftware.repcheck.models.SetSlot;
import com.brandtnewtonsoftware.repcheck.util.database.Seeder;

import java.util.Date;

/**
 * Created by brandt on 7/25/15.
 */
public class SetSeeder extends Seeder {

    @Override
    public void seed(Context context) {
        int now = new Date().getSeconds();

        SetSlot set1 = new SetSlot(5, 135);
        set1.setName("Bench press");
        set1.saveChanges(context);

        SetSlot set2 = new SetSlot(8, 135);
        set2.setName("Dead Lift");
        set2.saveChanges(context);

        SetSlot set3 = new SetSlot(10, 135);
        set3.setName("Squat");
        set3.saveChanges(context);

        SetSlot set4 = new SetSlot(10, 95);
        set4.setName("Military Press");
        set4.saveChanges(context);

        SetSlot set5 = new SetSlot(10, 65);
        set5.setName("Barbell Curl");
        set5.saveChanges(context);

        SetSlot set6 = new SetSlot(5, 225);
        set6.setName("Leg Press");
        set6.saveChanges(context);
    }
}
