package com.example.brandt.repcheck.activities.saveslots;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

/**
 * Created by brandt on 7/25/15.
 */
public class LoadSetDialog extends SetsListDialog {

    private static Handler updateHandler;

    public static LoadSetDialog newInstance(Handler updateHandler) {
        LoadSetDialog fragment = new LoadSetDialog();
        LoadSetDialog.updateHandler = updateHandler;
        return fragment;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Message msg = new Message();
        msg.arg1 = rowItems.get(i).getId();
        updateHandler.sendMessage(msg);
        dismiss();
    }

    @Override
    public String getTitle() {
        return "Load Set";
    }
}
