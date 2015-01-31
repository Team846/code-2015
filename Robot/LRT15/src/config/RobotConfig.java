package config;

public class RobotConfig {
	public static final int LOOP_RATE = 50; //hertz
	public static final int LOOP_PERIOD = (int)((1/LOOP_RATE) * 100);
	public static final double ROBOT_WIDTH = 26.574;
	public static final String CONFIG_FILE_PATH = "/LRT14.conf";
	public static final String PORT_MAPPINGS_FILE_PATH = "/PortMappings.conf";
	public static final String ROUTINE_FILE_PATH = "/Autonomous.routine";
	public static final String LOG_FILE_PATH = "/log.bin";
	public static final String PRINT_FILE_PATH = "/stdout.out";
	public static final int MAX_GAME_PIECES = 4; 
	public static final double MAX_VOLTAGE = 13;

	public class Drive
	{
		public static final int THROTTLE_COEFFICIENT = 1;
		public static final int THROTTLE_EXPONENT = 1;
		public static final int BLEND_COEFFICIENT = 1;
		public static final int BLEND_EXPONENT = 1;
		public static final double DEADBAND = 0.03;
	}

}
