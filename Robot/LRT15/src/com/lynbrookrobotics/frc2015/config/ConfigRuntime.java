package com.lynbrookrobotics.frc2015.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;

import com.lynbrookrobotics.frc2015.log.AsyncPrinter;



public class ConfigRuntime
{
	private static ConfigRuntime instance = null;
	private static ArrayList<Configurable> configurables = new ArrayList<Configurable>();
	
	private final static String CONFIG_FILE_PATH = RobotConfig.CONFIG_FILE_PATH;;
	private final static char COMMENT_DELIMITERS = '#'; 
	
	private HierarchicalINIConfiguration config;
	private long lastReadTimestamp;

	private ConfigRuntime()
	{
		lastReadTimestamp = 0;
		config = new HierarchicalINIConfiguration();
		Load();
	}

	public static ConfigRuntime Instance()
	{
		if(instance  == null)
			instance = new ConfigRuntime();
		return instance;
	}
	
	public static void Initialize()
	{
		if(instance  == null)
			instance = new ConfigRuntime();
	}
	
	public void Load()
	{
		LoadConfig(CONFIG_FILE_PATH);
		System.out.println("ConfigRuntime: Done loading " + CONFIG_FILE_PATH);
		ConfigureAll();
	}
	
	public void Save()
	{
		SaveConfig(CONFIG_FILE_PATH);
	}
	
	//TODO: add different methods for values bc java generics sux 
	public <T extends Number> T Get(String section, String key, T defaultValue)
	{
		if (config.containsKey(section + '.' + key))
		{
			return (T) config.getProperty(section + '.' + key);
		}
		else
		{
			Set(section, key, defaultValue);
			return defaultValue;
		}
		
	}
	
	public <T extends Number> void Set(String section, String key, T value)
	{
	
		String combinedKey = section + '.' + key;
		// Edit file data to save to file when Save() is called
		if (config.containsKey(combinedKey))
			config.clearProperty(combinedKey);
		config.setProperty(combinedKey, value);
	}
	

	public static void Register(Configurable configurable)
	{
		configurables.add(configurable);
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
		long currentFileTimestamp = config.getFile().lastModified();
		if(lastReadTimestamp != currentFileTimestamp)
		{
			Load();
			lastReadTimestamp = currentFileTimestamp;
		}
		
	}
	
	public void LoadConfig(String path)
	{
		File configFileHandle  = new File(path);
		if(!configFileHandle.exists())
		{
			System.out.println("[ERROR] Invalid Path for Config File!");
			return;
		}
		try 
		{
			config.load(configFileHandle);
		}
		catch (ConfigurationException e)
		{
			e.printStackTrace();
		}
	}
	
	private void SaveConfig(String path)
	{
		try {
			config.save();
		} catch (ConfigurationException e) {
			
			e.printStackTrace();
		}	
		System.out.println("Done saving " + path);
	}

}