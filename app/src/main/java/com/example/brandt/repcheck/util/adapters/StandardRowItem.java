package com.example.brandt.repcheck.util.adapters;

/**
 * Created by Brandt on 7/25/2015.
 */
public class StandardRowItem {

    private int id;
    private String title;
    private String text;

    public StandardRowItem(int id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
