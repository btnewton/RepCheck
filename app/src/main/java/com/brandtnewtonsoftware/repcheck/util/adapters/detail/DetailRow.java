package com.brandtnewtonsoftware.repcheck.util.adapters.detail;

/**
 * Created by Brandt on 8/4/2015.
 */
public final class DetailRow implements IDetailRow {
    private final int id;
    private final String title;
    private final String text;
    private final String detail;

    public DetailRow(int id, String title, String text, String detail) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.detail = detail;
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

    @Override
    public String getDetail() {
        return detail;
    }
}
