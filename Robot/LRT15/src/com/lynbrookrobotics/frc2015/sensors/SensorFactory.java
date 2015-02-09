package com.lynbrookrobotics.frc2015.sensors;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.lynbrookrobotics.frc2015.log.AsyncPrinter;
import com.lynbrookrobotics.frc2015.utils.Pair;

import edu.wpi.first.wpilibj.*;

public class SensorFactory {
	static Map<Integer, AnalogInput> analog = new HashMap<Integer, AnalogInput>();
	static Map<Integer, DigitalInput> digital = new HashMap<Integer, DigitalInput>();
	static Map<Integer, Counter> counters = new HashMap<>();
	static Map<Pair<Integer, Integer>, LRTEncoder> encoders = new HashMap<Pair<Integer, Integer>, LRTEncoder>();
	
	static SensorFactory instance = null;
	
	static public void Initialize()
	{
		if(instance == null)
			instance = new SensorFactory();
	}
	
	static public SensorFactory Instance()
	{
		if(instance == null)
			instance = new SensorFactory();
		
		return instance;
	}
	
	static public AnalogInput GetAnalogInput(int port)
	{
		if(!analog.containsKey(port))
		{
			AsyncPrinter.warn("No analog port at port " + port + ", creating new port");
			analog.put(port, new AnalogInput(port));
		}
		
		return analog.get(port);
	}
	
	static public DigitalInput GetDigitalInput(int port)
	{
		if (!digital.containsKey(port))
		{
			AsyncPrinter.warn("No gear tooth at port " + port + ", creating new port");
			digital.put(port, new DigitalInput(port));
		}
		return digital.get(port);
	}

	static public Counter GetCounter(int port)
	{
		if (!counters.containsKey(port))
		{
			AsyncPrinter.warn("No counter at port " + port + ", creating new port");
			counters.put(port, new Counter(port));
		}
		return counters.get(port);
	} 

	static public LRTEncoder GetLRTEncoder(String name, int portA, int portB)
	{
		Pair<Integer, Integer> mapEntry = new Pair<Integer, Integer>(portA, portB);
		if (!encoders.containsKey(mapEntry))
		{
			AsyncPrinter.warn("No encoder at ports " + portA +" " + portB + ", creating new port");
			encoders.put( mapEntry, new LRTEncoder( portA, portB));
		}
		return encoders.get(mapEntry);
	}

	static public GearTooth GetGearTooth(int port)
	{
		if (!counters.containsKey(port))
		{
			AsyncPrinter.warn("No gear tooth at port " + port + ", creating new port");
			counters.put(port, new GearTooth(port));
		}
		return (GearTooth)counters.get(port);
	}


}
