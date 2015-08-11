package com.example.brandt.repcheck.activities.saveslots;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;

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
    public String getTitle() {
        return "Load Set";
    }

    @Override
    public void onItemClickAction(AdapterView<?> parent, View view, int position, long id) {
        Message msg = new Message();
        msg.arg1 = rowItems.get(position).getId();
        updateHandler.sendMessage(msg);
        dismiss();
    }
}
