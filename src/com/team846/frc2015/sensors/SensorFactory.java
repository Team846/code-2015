package com.team846.frc2015.sensors;

import java.util.HashMap;
import java.util.Map;

import com.team846.frc2015.logging.AsyncLogger;
import com.team846.frc2015.utils.Pair;

import edu.wpi.first.wpilibj.*;

public class SensorFactory {
    private static final Map<Integer, AnalogInput> analog = new HashMap<Integer, AnalogInput>();
    private static final Map<Integer, DigitalInput> digital = new HashMap<Integer, DigitalInput>();
    private static final Map<Integer, Counter> counters = new HashMap<>();
    private static final Map<Pair<Integer, Integer>, LRTEncoder> encoders = new HashMap<Pair<Integer, Integer>, LRTEncoder>();

    private static SensorFactory instance = null;

    static public void initialize() {
        if (instance == null)
            instance = new SensorFactory();
    }

    static public SensorFactory instance() {
        if (instance == null)
            instance = new SensorFactory();

        return instance;
    }

    static public AnalogInput getAnalogInput(int port) {
        if (!analog.containsKey(port)) {
            AsyncLogger.warn("[SENSORFACTORY] No analog port at port " + port + ", creating new port");
            analog.put(port, new AnalogInput(port));
        }

        return analog.get(port);
    }

    static public DigitalInput getDigitalInput(int port) {
        if (!digital.containsKey(port)) {
            AsyncLogger.warn("[SENSORFACTORY] No gear tooth at port " + port + ", creating new port");
            digital.put(port, new DigitalInput(port));
        }
        return digital.get(port);
    }

    static public Counter getCounter(int port) {
        if (!counters.containsKey(port)) {
            AsyncLogger.warn("[SENSORFACTORY] No counter at port " + port + ", creating new port");
            counters.put(port, new Counter(port));
        }
        return counters.get(port);
    }

    static public LRTEncoder getLRTEncoder(String name, int portA, int portB) {
        Pair<Integer, Integer> mapEntry = new Pair<Integer, Integer>(portA, portB);
        if (!encoders.containsKey(mapEntry)) {
            AsyncLogger.warn("[SENSORFACTORY] No encoder at ports " + portA + " " + portB + ", creating new port");
            encoders.put(mapEntry, new LRTEncoder(portA, portB));
        }
        return encoders.get(mapEntry);
    }

    static public GearTooth getGearTooth(int port) {
        if (!counters.containsKey(port)) {
            AsyncLogger.warn("[SENSORFACTORY] No gear tooth at port " + port + ", creating new port");
            counters.put(port, new GearTooth(port));
        }
        return (GearTooth) counters.get(port);
    }


}
