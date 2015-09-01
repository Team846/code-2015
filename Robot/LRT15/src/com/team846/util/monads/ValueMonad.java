package com.team846.util.monads;

import java.util.function.Function;

public abstract class ValueMonad<T> extends Monad<T> {
    @Override
    public <R> Monad<R> map(Function<T, R> transformer) {
        return new ValueMonad<R>() {
            @Override
            public R get() {
                return transformer.apply(ValueMonad.this.get());
            }
        };
    }

    @Override
    public <R> Monad<R> flatMap(Function<T, Monad<R>> transformer) {
        return new ValueMonad<R>() {
            @Override
            public R get() {
                return transformer.apply(ValueMonad.this.get()).get();
            }
        };
    }
}
