package com.team846.util.monads;

public class MutableValueMonad<T> extends ValueMonad<T> {
    T currentValue;

    public MutableValueMonad(T currentValue) {
        this.currentValue = currentValue;
    }

    public void set(T newValue) {
        currentValue = newValue;
    }

    @Override
    public T get() {
        return currentValue;
    }
}
