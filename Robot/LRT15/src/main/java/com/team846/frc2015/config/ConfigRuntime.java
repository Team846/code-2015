package com.team846.frc2015.config;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;

import com.team846.frc2015.utils.AsyncPrinter;

public class ConfigRuntime
{
	private static ConfigRuntime instance = null;
	private static final ArrayList<Configurable> configurables = new ArrayList<Configurable>();
	
	private final static String CONFIG_FILE_PATH = RobotConfig.CONFIG_FILE_PATH;
	private final static char COMMENT_DELIMITERS = '#'; 
	
	private final HierarchicalINIConfiguration config;
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
	
	void Load()
	{
		LoadConfig(CONFIG_FILE_PATH);
		AsyncPrinter.info("ConfigRuntime: Done loading " + CONFIG_FILE_PATH);
		//Save();
		ConfigureAll();
	}
	
	public void Save()
	{
		SaveConfig(CONFIG_FILE_PATH);
	}
	
	public int Get(String section, String key, int defaultValue)
	{
		if (config.containsKey(section + '.' + key))
		{
			return  (config.getInt(section + '.' + key));
		}
		else
		{
			AsyncPrinter.warn("Cannnot find " + key  + " in " + section+ ", setting default value in file");
			Set(section, key, defaultValue);
			return defaultValue;
		}
		
	}
	
	public double Get(String section, String key, double defaultValue)
	{
		if (config.containsKey(section + '.' + key))
		{
			return config.getDouble(section + '.' + key);
		}
		else
		{
			AsyncPrinter.warn("Cannnot find " + key  + " in " + section+ ", setting default value in file");
			Set(section, key, defaultValue);
			return defaultValue;
		}
		
	}
	
	<T extends Number> void Set(String section, String key, T value)
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
			AsyncPrinter.info("Change detected, reloading config");
			config.reload();
			ConfigureAll();
			//Load();
			lastReadTimestamp = currentFileTimestamp;
		}
		
	}
	
	void LoadConfig(String path)
	{
		File configFileHandle  = new File(path);
		if(!configFileHandle.exists())
		{
			AsyncPrinter.error("Invalid Path for Config File at " + path);
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
			config.save(new File(RobotConfig.CONFIG_FILE_PATH));
		} catch (ConfigurationException e) {
			
			e.printStackTrace();
		}	
		AsyncPrinter.info("Done saving " + path);
	}

}
