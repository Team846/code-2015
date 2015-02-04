package com.lynbrookrobotics.frc2015.config;

public class DriverStationConfig {
	
	public class JoystickConfig
	{
		public static final int NUM_JOYSTICK_BUTTONS = 12;// Max supported by the driver station
		public static final int NUM_JOYSTICK_AXES = 6;
		public static final int NUM_WHEEL_BUTTONS = 11;
		public static final int NUM_WHEEL_AXES = 3;
		
		public static final int DRIVER_STICK_PORT = 1;
		public static final int OPERATOR_STICK_PORT = 2;
		public static final int DRIVER_WHEEL_PORT = 3;
	}
	
	public class JoystickButtons
	{
		// Driver Joystick
		public static final int COLLECT = 0;
		public static final int TURN_90 = 1;
		public static final int TURN_180 = 2;
		// Operator Joystick
		public static final int PASS = 1;
	
		public static final int REVERSE_ROLLERS = 11;
		public static final int UNLOAD_LAUNCHER = 12;
		
		// Driver Wheel
		public static final int QUICK_TURN = 5;
		public static final int REVERSE_DRIVE = 6;
		public static final int POSITION_HOLD = 7;
		
		//Dashboard Mode
		public static final int DASHBOARD_ENABLE1 = 6;
		public static final int DASHBOARD_ENABLE2 = 7;
	}
	
	public class DigitalIns
	{
		public static final int NO_DS_DI = -1;
		public static final int DRIVETRAIN = 1;
		public static final int COLLECTOR_ROLLERS = 2;
		public static final int COLLECTOR_ARM = 3;
		public static final int LAUNCHER_LOADER = 4;
		public static final int LAUNCHER_ANGLE = 5;
		public static final int BALL_HOLDER = 6;
		public static final int NETWORK = 7;
		public static final int COMPRESSOR = 8;
	}

	public class AnalogIns
	{
		public static final int AUTONOMOUS_DELAY = 1;
		public static final int AUTONOMOUS_SELECT = 2;
	}
	
}
