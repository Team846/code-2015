package com.team846.util.monads;

public class MutableCachedMonadSource<T> extends CachedMonad<T> {
    public MutableCachedMonadSource() {
    }

    public MutableCachedMonadSource(T value) {
        set(value);
    }

    public void set(T value) {
        update(value);
    }
}
