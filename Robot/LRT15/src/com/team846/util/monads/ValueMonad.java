package com.team846.util.monads;

import com.team846.frc2015.utils.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public static <O> ValueMonad<List<O>> sequence(List<Monad<O>> monads) {
        return new ValueMonad<List<O>>() {
            @Override
            public List<O> get() {
                return monads.stream().map(Monad::get).collect(Collectors.toList());
            }
        };
    }

    public static <T> ValueMonad<List<T>> sequence(Monad<T>... monads) {
        return sequence(Arrays.asList(monads));
    }
}
