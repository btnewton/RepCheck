package com.brandtnewtonsoftware.repcheck.util.database;

/**
 * Created by Brandt on 7/24/2015.
 */
public class TableNotFoundException extends Exception {

    public TableNotFoundException(){
    }

    public TableNotFoundException(String message) {
        super(message);
    }

    public TableNotFoundException(Throwable cause) {
        super(cause);
    }
}