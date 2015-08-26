package com.team846.frc2015.componentData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.team846.frc2015.logging.AsyncLogger;


public abstract class ComponentData 
{
	private static final Map<String, ComponentData> componentData_map = new HashMap<String, ComponentData>();
	private static final ArrayList<ComponentData> data = new ArrayList<ComponentData>();
	
	ComponentData(String name)
	{
		componentData_map.put(name, this);
		AsyncLogger.info("Created componentData: " + name);
	}

	public static void createComponentDatas()
	{
		data.add(new DrivetrainData());
		data.add(new ElevatorData());
		data.add(new CarriageExtenderData());
		data.add(new CarriageHooksData());
		data.add(new CollectorRollersData());
		data.add(new CollectorArmData());
		data.add(new ContainerArmData());
	}

	static ComponentData GetComponentData(String name)
	{
		if (componentData_map.containsKey(name))
			return componentData_map.get(name);
		else
		{
			AsyncLogger.warn(name + " not found in map! Returning null");
			return null;
		}
	}

	public static void ResetAllCommands()
	{
		for ( ComponentData c : data)
		{
			c.ResetCommands();
		}
	}
	
	protected abstract void ResetCommands();
}
