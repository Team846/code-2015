package com.team846.frc2015.config;

import static com.team846.frc2015.logging.Logger.*;

import com.team846.util.monads.CachedMonad;
import com.team846.util.monads.MutableCachedMonadSource;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;

public class RootConfig {
    private MutableCachedMonadSource<Config> mutableRoot = new MutableCachedMonadSource<>();

    public RootConfig(File configFile) {
        mutableRoot.set(ConfigFactory.parseFile(configFile));

        info("[RootConfig] Successfully parsed configuration file");

        try {
            WatchKey key = configFile.toPath().register(FileSystems.getDefault().newWatchService(), StandardWatchEventKinds.ENTRY_MODIFY);
            Thread watcher = new Thread() {
                public void run() {
                    key.pollEvents().stream().filter(event -> event.kind() != StandardWatchEventKinds.OVERFLOW).forEach(event ->
                                    mutableRoot.set(ConfigFactory.parseFile(configFile))
                    );
                }
            };

            watcher.start();

            info("[RootConfig] Configuration file watcher successfully started");
        } catch (IOException e) {
            error("[RootConfig] Unable to register watcher on configuration file");
        }
    }

    public CachedMonad<Config> get() {
        return mutableRoot;
    }
}
