package com.lynbrookrobotics.frc2015.sensors;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
			analog.put(port, new AnalogInput(port));
		
		return analog.get(port);
	}
	
	static public DigitalInput GetDigitalInput(int port)
	{
		if (!digital.containsKey(port))
		{
			digital.put(port, new DigitalInput(port));
		}
		return digital.get(port);
	}

	static public Counter GetCounter(int port)
	{
		if (!counters.containsKey(port))
		{
			counters.put(port, new Counter(port));
		}
		return counters.get(port);
	} 

	static public LRTEncoder GetLRTEncoder(String name, int portA, int portB)
	{
		Pair<Integer, Integer> mapEntry = new Pair<Integer, Integer>(portA, portB);
		if (!encoders.containsKey(mapEntry))
		{
			encoders.put( mapEntry, new LRTEncoder( portA, portB));
		}
		return encoders.get(mapEntry);
	}

	static public GearTooth GetGearTooth(int port)
	{
		if (!counters.containsKey(port))
		{
			counters.put(port, new GearTooth(port));
		}
		return (GearTooth)counters.get(port);
	}

//	void Send()
//	{
//		for (map<int, AnalogInput*>::iterator it = m_analog.begin(); it != m_analog.end(); it++)
//		{
//			SendToNetwork(it->second->GetAverageValue(), "Analog" + lexical_cast(it->first), "SensorData");
//			
//			INT16 id = 30000 + it->first;
//			
//			Dashboard2::SetOrAddTelemetryData("Analog" + lexical_cast(it->first), id, DashboardTelemetryType::INT32, it->second->GetAverageValue());
//		}
//		for (map<int, DigitalInput*>::iterator it = m_digital.begin(); it != m_digital.end(); it++)
//		{
//			SendToNetwork(it->second->Get(), "Digital" + lexical_cast(it->first), "SensorData");
//			
//			INT16 id = 31000 + it->first;
//			
//			Dashboard2::SetOrAddTelemetryData("Digital" + lexical_cast(it->first), id, DashboardTelemetryType::UINT32, it->second->Get());
//		}
//		for (map<int, Counter*>::iterator it = m_counters.begin(); it != m_counters.end(); it++)
//		{
//			SendToNetwork(it->second->GetPeriod(), "Counter" + lexical_cast(it->first), "SensorData");
//			
//			INT16 id = 32000 + it->first;
//			
//			Dashboard2::SetOrAddTelemetryData("Counter" + lexical_cast(it->first), id, DashboardTelemetryType::FLOAT, (float)it->second->GetPeriod());
//		}
//		for (map<pair<int, int>, LRTEncoder*>::iterator it = m_encoders.begin(); it != m_encoders.end(); it++)
//		{
//			SendToNetwork(it->second->GetRate(), "EncoderRate" + lexical_cast(it->first.first) + "," + lexical_cast(it->first.second), "SensorData");
//			SendToNetwork(it->second->Get(), "EncoderDistance" + lexical_cast(it->first.first) + "," + lexical_cast(it->first.second), "SensorData");
//		
//			{
//				INT16 rateId = 28000 + it->first.first;
//				
//				Dashboard2::SetOrAddTelemetryData("EncoderRate" + lexical_cast(it->first.first) + "," + lexical_cast(it->first.second), rateId, DashboardTelemetryType::FLOAT, (float)it->second->GetPeriod());
//			}
//			
//			{
//				INT16 distanceId = 29000 + it->first.first;
//				
//				Dashboard2::SetOrAddTelemetryData("EncoderDistance" + lexical_cast(it->first.first) + "," + lexical_cast(it->first.second), distanceId, DashboardTelemetryType::INT32, it->second->Get());
//			}
//		}

}
