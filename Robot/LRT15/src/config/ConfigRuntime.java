package config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigRuntime
{
	
	private class Config
	{
		public Config()
		{
			value = "";
			position = null;
		}
		public String value;
		public ListIterator<String> position;
	}
	private static ConfigRuntime instance = null;
	private static ArrayList<Configurable> configurables = new ArrayList<Configurable>();
	
	private final static String CONFIG_FILE_PATH = RobotConfig.CONFIG_FILE_PATH;;
	private final static String COMMENT_DELIMITERS = "#;";;
	
	HashMap<String, ListIterator<String>> sectionMap;
	//std::time_t lastReadTime;
	
	List<String> fileData;
	HashMap<String, HashMap<String, Config>> configData;
	
	private ConfigRuntime()
	{
		fileData = new LinkedList<String>();
		configData = new HashMap<String, HashMap<String, Config>>();
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
	public int Get(String section, String key, int defaultValue)
	{
		if (KeyExists(section, key))
		{
			 return Integer.parseInt(configData.get(section).get(key).value);
//			stringstream sstream((*configData)[section][key].value);
//			T value;
//			sstream >> value;
//			return value;
		}
		else
		{
			//Set(section, key, defaultValue);
			return defaultValue;
		}
		
	}
	
	public void Set(String section, String key, int value)
	{
		String newValue = String.valueOf(value);
		// Edit file data to save to file when Save() is called
		if (KeyExists(section, key))
		{
			Config nameValuePair = configData.get(section).get(key);
			ListIterator valueLocation = new ListI
			list<string>::iterator valueLocation = nameValuePair.position;
			string oldValue = nameValuePair.value;
			
			string newString = key + "=" + newValue;
			*valueLocation = newString;
		}
		else
		{
			if (configData.containsKey(section)) // If section does not exist
			{
				fileData.add("[" + section + "]");
				list<string>::iterator sectionLocation = fileData.listIterator().->end();
				sectionLocation--;
				(*sectionMap)[section] = sectionLocation;
			}
			list<string>::iterator sectionLocation = (*sectionMap)[section];
			sectionLocation++;
			string str = key + "=" + newValue;
			fileData->insert(sectionLocation, str);
		}
		(*configData)[section][key].value = newValue; // Update current config data
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
		
	}
	
	public void LoadConfig(String path)
	{
		fileData = new LinkedList<String>();
		
		configData = new HashMap<String, HashMap<String, Config>>();
		
		sectionMap = new HashMap<String, ListIterator<String>>();
		
		Scanner fin = null;
		try {
			fin = new Scanner(new File(path));
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			System.out.println("ConfigManager: Could not open" + path);
			return;
		}
		
		// Read lines into list
		while (!fin.hasNextLine())
		{
			fileData.add(fin.nextLine());
		}
		fin.close();

		String currentSection = "";
		for (int i = 0; i < fileData.size(); i++)
		{
			int length = matchDelimiters(fileData.get(i)); // String length up to first comment
			if (length == -1) // If no comments on this line
				length = fileData.get(i).length();

			String line = (fileData.get(i).substring(0, length)).trim(); // Trim whitespace from non-comment part of this line
			if (line.length() == 0) // If this line contains no data
				continue;

			if (line.charAt(0) == '[') // If new section
			{
				currentSection = line.substring(1, line.lastIndexOf("]")); // Set current section name
				(*sectionMap)[currentSection] = it; // Set position of current section
				continue;
			}

			stringstream sstream(line);
			string key, value;
			getline(sstream, key, '='); // Get key up to =
			getline(sstream, value); // Get assigned value after =
			Config currentConfig;
			currentConfig.value = value;
			currentConfig.position = it;
			(*configData)[currentSection][key] = currentConfig; // Set value for key of current section
		}
	}
	
	private void SaveConfig(String path)
	{
		ofstream fout(path.c_str());
		if (!fout.is_open())
		{
			BufferedConsole::Printf("ConfigManager: could not open %s for writing\n",
					path.c_str());
		}

		for (list<string>::iterator it = fileData->begin(); it != fileData->end(); it++)
		{
			fout << *it << '\n';
		}
		
		fout.close();
		
		System.out.println("Done saving " + path);
	}
	
	private int matchDelimiters(String line)
	{
	     String letters = COMMENT_DELIMITERS;
	     Pattern pattern = Pattern.compile("[" + letters + "]");
	     Matcher matcher = pattern.matcher(line);
	     int position = -1;
	     if (matcher.find()) {
	         position = matcher.start();
	     }
	     return position;
	}
	
	private boolean KeyExists(String section, String key)
	{
		return configData.containsKey(section)&& configData.get(section).containsKey(key);
	}

	
}
