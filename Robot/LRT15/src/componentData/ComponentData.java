package componentData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public abstract class ComponentData 
{
	private static Map<String, ComponentData> componentData_map = new HashMap<String, ComponentData>();
	private static ArrayList<ComponentData> data = new ArrayList<ComponentData>();
	
	public ComponentData(String name)
	{
		componentData_map.put(name, this);
	}

	public static void Initialize()
	{
		data.add(new DrivetrainData());
		data.add(new ElevatorData());
		//data.add(new CarriageData());
		data.add(new CollectorRollersData());
		data.add(new CollectorArmData());
		data.add(new RakeArmData());
		//data.add(new RakeExtenderData());

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
