package com.team846.frc2015.oldconfig;


import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;

import com.team846.frc2015.logging.AsyncLogger;

public class ConfigPortMappings {
    static private ConfigPortMappings instance = null;

    final static private String CONFIG_FILE_PATH = RobotConfig.PORT_MAPPINGS_FILE_PATH;
    final static private char COMMENT_DELIMITER = '#';

    private final HierarchicalINIConfiguration config;

    public static ConfigPortMappings Instance() {
        if (instance == null)
            instance = new ConfigPortMappings();
        return instance;
    }

    private ConfigPortMappings() {
        config = new HierarchicalINIConfiguration();
    }

    public void Load() {
        LoadConfig(CONFIG_FILE_PATH);
        AsyncLogger.info("ConfigPortMappings: Done loading " + CONFIG_FILE_PATH);
    }

    public int get(String name) {
        String key;
        key = name.replace('/', '.');
        if (config.getInt(key, -1) == -1) {
            AsyncLogger.error("Port mapping not found for " + name);
            return -1;
        }

        return config.getInt(key);

    }

    private void LoadConfig(String path) {
        File configFile = new File(path);
        if (!configFile.exists()) {
            AsyncLogger.error("Could not find config file at " + path);
            return;
        }

        try {
            config.load(configFile);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

}
