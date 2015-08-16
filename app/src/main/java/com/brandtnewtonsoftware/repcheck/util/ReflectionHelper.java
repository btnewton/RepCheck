package com.brandtnewtonsoftware.repcheck.util;

/**
 * Created by Brandt on 8/2/2015.
 */
public class ReflectionHelper<T> {

    public T reflectClass(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<T> myClass = (Class<T>) Class.forName(className);
        return myClass.newInstance();
    }
}
