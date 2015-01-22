package config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigPortMappings 
{
	LinkedList<String> fileData;
	HashMap<String, HashMap<String, Integer>> portData;
	
	static private ConfigPortMappings instance = null;

	final static private String CONFIG_FILE_PATH = RobotConfig.PORT_MAPPINGS_FILE_PATH;
	final static private String COMMENT_DELIMITERS = "#;";
	
	public ConfigPortMappings Instance()
	{
		if (instance == null)
			instance = new ConfigPortMappings();
		return instance;
	}
	
	private ConfigPortMappings()
	{
		fileData = new LinkedList<String>();
		portData = new HashMap<String, HashMap<String, Integer> >();
	}
	
	public void Load()
	{
		LoadConfig(CONFIG_FILE_PATH);
		System.out.println("ConfigPortMappings: Done loading " +  CONFIG_FILE_PATH);
	}
	
	public int Get(String name)
	{
		String section, key;
		section = name.substring(0, name.indexOf('/')).toLowerCase();
		key = name.substring(name.indexOf('/')+1).toUpperCase();

		if (Instance().KeyExists(section, key))
		{
			return Instance().portData.get(section).get(key);
		}
		System.out.println("[ERROR] Port mapping not found for " + name);
		return 0;
	}
	
	private void LoadConfig(String path)
	{

		Scanner fin = null;
		try {
			fin = new Scanner(new File(path));
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
				
		// Read lines into list
		while (fin.hasNext())
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
				currentSection = line.substring(1, line.lastIndexOf("]") - 1); // Set current section name
				currentSection = currentSection.toLowerCase();//transform(currentSection.begin(), currentSection.end(), currentSection.begin(), ::tolower);
				continue;
			}

			//stringstream sstream(line);
			String key;
			int value;
			//getline(sstream, key, '='); // Get key up to =
			key = line.substring(0, line.indexOf('='));
			value = Integer.parseInt((line.substring(line.indexOf('=')+1)));

			//sstream >> value; // Get assigned value after =
			key = key.toUpperCase();
			portData.get(currentSection).put(key, value); // Set value for key of current section
		}
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
			return portData.containsKey(section) && portData.get(section).containsKey(key);
		}

}
