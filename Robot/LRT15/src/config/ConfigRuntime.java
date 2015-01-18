package config;

import java.util.*;
import java.io.*;
public class ConfigRuntime {
	public ConfigRuntime()
	{
		fileData = null;
		configData = null;
		sectionMap = null;
		Load();
	}
	
	final static String CONFIG_FILE_PATH = RobotConfig.CONFIG_FILE_PATH;
	final static String COMMENT_DELIMITERS = "#;";
	
	class Config
	{
		String value;
		ListIterator<String> position;
	}

		public static ConfigRuntime Instance()
		{
			if(m_instance == null)
				m_instance = new ConfigRuntime();
			return m_instance;
		}
						
		public void Load()
		{
			LoadConfig(CONFIG_FILE_PATH);
			System.out.println("ConfigRuntime: done loading " + CONFIG_FILE_PATH);
			ConfigureAll();
		}
		public void Save()
		{
			SaveConfig(CONFIG_FILE_PATH);
		}
				 
		public <T> void Set(String section, String key, T value)
		{

		}
		/*
		public <T> T Get(String section, String key, T defaultValue)
		{
			if (KeyExists(section, key))
			{
				//c++ code starts here
				stringstream sstream(configData.get(section).get(key).value);
				T value;
				sstream >> value;
				return value;
			}
			else
			{
				Set<T>(section, key, defaultValue);
				return defaultValue;
			}
		}
		*/
		//public<T> T Set(String section, String key, T value){}
		public static void Register(Configurable configurable)
		{
			configurables.add(configurable);
		}
		public static void ConfigureAll()
		{
				
		}

		public void CheckForFileUpdates()
		{
			
		}
		
		private static ConfigRuntime m_instance;
		private static ArrayList<Configurable> configurables;
		
		private void LoadConfig(String path)
		{
			//Map<String , List<String>>
			//Stream<String> lines = Files.lines(path);
		}

		void SaveConfig(String path)
		{
			
		}
		List<String> fileData;
		Map<String, Map <String , Map<String, Map<String , Config>>> > configData;
		Map<String, ListIterator<String>> sectionMap;
		//(time in java keyword) lastReadTime;
		
		boolean KeyExists(String section, String key)
		{
			return configData.get(section) != null && (configData.get(section)).get(key) != null;
		}


}
