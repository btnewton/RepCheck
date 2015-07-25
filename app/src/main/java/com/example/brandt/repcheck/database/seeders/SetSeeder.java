package com.example.brandt.repcheck.database.seeders;

import android.content.Context;

import com.example.brandt.repcheck.models.SetSlot;
import com.example.brandt.repcheck.util.database.Seeder;

import java.util.Date;

/**
 * Created by brandt on 7/25/15.
 */
public class SetSeeder extends Seeder {

    @Override
    public void seed(Context context) {
        int now = new Date().getSeconds();

        SetSlot set1 = new SetSlot(5, 225, new Date(now));
        set1.setName("Bench press");
        set1.saveChanges(context);

        // 2 hours ago
        SetSlot set2 = new SetSlot(5, 55, new Date(now - 60 * 60 * 2));
        set2.setName("Curl");
        set2.saveChanges(context);

        SetSlot set3 = new SetSlot(3, 315, new Date(2015, 3, 3));
        set3.setName("Deadlift");
        set3.saveChanges(context);
    }
}
