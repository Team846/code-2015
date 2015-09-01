package com.team846.frc2015.config;

import com.team846.util.monads.CachedMonad;
import com.team846.util.monads.MutableCachedMonadSource;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;

public class RootConfig {
    private MutableCachedMonadSource<Config> mutableRoot = new MutableCachedMonadSource<>();

    public RootConfig(File configFile) {
        mutableRoot.set(ConfigFactory.parseFile(configFile));
    }

    public CachedMonad<Config> get() {
        return mutableRoot;
    }
}
