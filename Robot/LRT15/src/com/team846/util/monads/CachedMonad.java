package com.team846.util.monads;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CachedMonad<T> extends Monad<T> {
    class TransformerAndCached<R> {
        private Function<T, R> transformer;
        private CachedMonad<R> cached;

        public void update(T newSource) {
            cached.update(transformer.apply(newSource));
        }

        public TransformerAndCached(Function<T, R> transformer, CachedMonad<R> cached) {
            this.transformer = transformer;
            this.cached = cached;
        }
    }

    private T value;
    private LinkedList<TransformerAndCached<?>> dependents = new LinkedList<>();
    private LinkedList<Runnable> callbacks = new LinkedList<>();

    protected void update(T newValue) {
        if (value == null || !value.equals(newValue)) {
            value = newValue;
            dependents.forEach((dependent) -> dependent.update(get()));
            callbacks.forEach(Runnable::run);
        }
    }

    protected void onUpdate(Runnable callback) {
        callbacks.add(callback);
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public <R> CachedMonad<R> map(Function<T, R> transformer) {
        CachedMonad<R> dependent = new CachedMonad<>();
        dependent.update(transformer.apply(get()));
        dependents.add(new TransformerAndCached<>(transformer, dependent));

        return dependent;
    }

    @Override
    public <R> CachedMonad<R> flatMap(Function<T, Monad<R>> transformer) {
        CachedMonad<R> dependent = new CachedMonad<>();
        dependent.update(transformer.apply(get()).get());
        dependents.add(new TransformerAndCached<>(
                (elem) -> transformer.apply(elem).get(),
                dependent
        ));

        return dependent;
    }

    public Monad<T> asSeed() {
        return new ValueMonad<T>() {
            @Override
            public T get() {
                return CachedMonad.this.get();
            }
        };
    }

    public static <O> CachedMonad<List<O>> sequence(List<CachedMonad<O>> monads) {
        CachedMonad<List<O>> ret = new CachedMonad<>();
        Runnable onChildrenChange = () -> {
            List<O> newValue = monads.stream().map(CachedMonad::get).collect(Collectors.toList());
            ret.update(newValue);
        };

        monads.forEach(monad -> monad.onUpdate(onChildrenChange));
        onChildrenChange.run();

        return ret;
    }

    public static <T> CachedMonad<List<T>> sequence(CachedMonad<T>... monads) {
        return sequence(Arrays.asList(monads));
    }
}
