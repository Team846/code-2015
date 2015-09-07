package com.team846.frc2015;

import com.team846.util.monads.*;
import org.junit.Test;

import java.util.List;

public class MonadsTest {
    @Test
    public void sequenceWithValueMonads() {
        MutableValueMonad<Integer> monad1 = new MutableValueMonad<>(0);

        MutableValueMonad<Integer> monad2 = new MutableValueMonad<>(1);

        Monad<List<Integer>> sequenced = ValueMonad.sequence(monad1, monad2);

        assert sequenced.get().get(0) == 0 && sequenced.get().get(1) == 1;

        monad1.set(2);
        assert sequenced.get().get(0) == 2 && sequenced.get().get(1) == 1;

        monad2.set(3);
        assert sequenced.get().get(0) == 2 && sequenced.get().get(1) == 3;
    }

    @Test
    public void sequenceWithCachedMonads() {
        MutableCachedMonadSource<Integer> monad1 = new MutableCachedMonadSource<>(0);

        MutableCachedMonadSource<Integer> monad2 = new MutableCachedMonadSource<>(1);

        Monad<List<Integer>> sequenced = CachedMonad.sequence(monad1, monad2);

        assert sequenced.get().get(0) == 0 && sequenced.get().get(1) == 1;

        monad1.set(2);
        assert sequenced.get().get(0) == 2 && sequenced.get().get(1) == 1;

        monad2.set(3);
        assert sequenced.get().get(0) == 2 && sequenced.get().get(1) == 3;
    }
}
