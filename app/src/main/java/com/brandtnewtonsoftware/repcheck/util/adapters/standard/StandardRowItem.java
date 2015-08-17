package com.brandtnewtonsoftware.repcheck.util.adapters.standard;

/**
 * Created by Brandt on 8/2/2015.
 */
public final class StandardRowItem implements IStandardRowItem {

    private final int id;
    private final String title;
    private final String text;

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
