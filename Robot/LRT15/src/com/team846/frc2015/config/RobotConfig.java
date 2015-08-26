package com.team846.frc2015.config;

public class RobotConfig 
{
	public static final int LOOP_RATE = 50; //hertz
	public static final int LOOP_PERIOD = (int)((1.0/LOOP_RATE) * 1000); //ms
	public static final double ROBOT_WIDTH = 26.574; //in
	
	public static final String CONFIG_FILE_PATH = "/home/lvuser/configuration/config/LRT15.txt";
	public static final String AUTO_FOLDER_PATH = "/home/lvuser/configuration/auto/";
	public static final String PORT_MAPPINGS_FILE_PATH = "/home/lvuser/configuration/config/ConfigPortMappings.txt";
}
