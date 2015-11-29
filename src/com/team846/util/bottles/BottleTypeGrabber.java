package com.team846.util.bottles;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BottleTypeGrabber<T> {
    public Class<T> getBottleClass() {
        Type superClass = getClass().getGenericSuperclass();
        Type t = ((ParameterizedType) superClass).getActualTypeArguments()[0];

        try {
            return (Class<T>) Class.forName(t.getTypeName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Something in the BottleTypeGrabber logic messed up: " + e);
        }
    }
}