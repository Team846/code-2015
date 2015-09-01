package com.team846.util.monads;

public class MutableCachedMonadSource<T> extends CachedMonad<T> {
    public void set(T value) {
        update(value);
    }
}
