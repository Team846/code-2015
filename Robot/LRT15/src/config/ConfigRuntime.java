package config;

import java.util.ArrayList;

public class ConfigRuntime
{
	private static ConfigRuntime instance = null;
	private static ArrayList<Configurable> configurables = new ArrayList<Configurable>();

	public static ConfigRuntime Instance()
	{
		if(instance  == null)
			instance = new ConfigRuntime();
		return instance;
	}
	
	public static void ConfigureAll()
	{
		for(Configurable c: configurables)
		{
			c.Configure();
		}
	}
	
	public void CheckForFileUpdates()
	{
		
	}

}
