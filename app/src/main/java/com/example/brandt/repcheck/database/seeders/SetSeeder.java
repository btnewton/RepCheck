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

        SetSlot set1 = new SetSlot(5, 135);
        set1.setName("Slot 1");
        set1.saveChanges(context);

        SetSlot set2 = new SetSlot(5, 135);
        set2.setName("Slot 2");
        set2.saveChanges(context);

        SetSlot set3 = new SetSlot(5, 135);
        set3.setName("Slot 3");
        set3.saveChanges(context);

        SetSlot set4 = new SetSlot(5, 135);
        set4.setName("Slot 4");
        set4.saveChanges(context);

        SetSlot set5 = new SetSlot(5, 135);
        set5.setName("Slot 5");
        set5.saveChanges(context);

        SetSlot set6 = new SetSlot(5, 135);
        set6.setName("Slot 6");
        set6.saveChanges(context);
    }
}
