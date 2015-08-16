package com.brandtnewtonsoftware.repcheck.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brandt on 8/2/2015.
 */
public class ReflectionHelper<T> {

    private List<T> alreadyReflected;

    public ReflectionHelper() {
        alreadyReflected = new ArrayList<>();
    }

    public T reflectClass(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<T> myClass = (Class<T>) Class.forName(className);
        return myClass.newInstance();
    }
}
