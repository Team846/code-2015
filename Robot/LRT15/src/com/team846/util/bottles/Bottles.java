package com.team846.util.bottles;

import java.util.HashMap;
import java.util.Map;

public class Bottles {
    private static Map<Class<?>, Object> bottles = new HashMap<>();

    public static <C> C getBottle(Class<C> bottleClass) {
        if (bottles.containsKey(bottleClass)) {
            return (C) bottles.get(bottleClass);
        } else {
            try {
                putBottle(bottleClass.newInstance(), bottleClass);
                return getBottle(bottleClass);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static <C> C bottle() {
        return getBottle(new BottleTypeGrabber<C>(){}.getBottleClass());
    }

    public static <C> void putBottle(C bottle, Class<C> bottleClass) {
        bottles.put(bottleClass, bottle);
    }

    public static <C> void putBottle(C bottle) {
        putBottle(bottle, new BottleTypeGrabber<C>(){}.getBottleClass());
    }
}
