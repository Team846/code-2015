package com.team846.util.monads;

import java.util.function.Function;

public abstract class Monad<T> {
    public abstract T get();

    public abstract <R> Monad<R> map(Function<T, R> transformer);

    public abstract <R> Monad<R> flatMap(Function<T, Monad<R>> transformer);
}
