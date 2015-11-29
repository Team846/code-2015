
package com.team846.frc2015.oldconfig;

public interface Configurable {
    public void configure();

    public default int GetConfig(String key, int defaultValue) {
        String simpleClassName = this.getClass().getName();
        simpleClassName = simpleClassName.substring(simpleClassName.lastIndexOf('.') + 1);
        return ConfigRuntime.Instance().Get(simpleClassName, key, defaultValue);
    }

    public default double GetConfig(String key, double defaultValue) {
        String simpleClassName = this.getClass().getName();
        simpleClassName = simpleClassName.substring(simpleClassName.lastIndexOf('.') + 1);
        return ConfigRuntime.Instance().Get(simpleClassName, key, defaultValue);
    }
}

