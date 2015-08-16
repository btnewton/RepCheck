package com.brandtnewtonsoftware.repcheck.util.adapters.standard;

/**
 * Created by Brandt on 8/2/2015.
 */
public class StandardRowItem implements IStandardRowItem {

    private int id;
    private String title;
    private String text;

    public StandardRowItem(int id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getText() {
        return text;
    }
}
