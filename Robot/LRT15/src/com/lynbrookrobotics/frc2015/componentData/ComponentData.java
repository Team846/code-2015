package com.lynbrookrobotics.frc2015.componentData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.lynbrookrobotics.frc2015.log.AsyncPrinter;


public abstract class ComponentData 
{
	private static Map<String, ComponentData> componentData_map = new HashMap<String, ComponentData>();
	private static ArrayList<ComponentData> data = new ArrayList<ComponentData>();
	
	public ComponentData(String name)
	{
		componentData_map.put(name, this);
		AsyncPrinter.println("Created componentData: " + name);
	}

	public static void createComponentDatas()
	{
		data.add(new DrivetrainData());
		data.add(new ElevatorData());
		data.add(new CarriageExtenderData());
		data.add(new CarriageHooksData());
		data.add(new CollectorRollersData());
		data.add(new CollectorArmData());

	}

	public static ComponentData GetComponentData(String name)
	{
		if (componentData_map.containsKey(name))
			return componentData_map.get(name);
		return null;
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
