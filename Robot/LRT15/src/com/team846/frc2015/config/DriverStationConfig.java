package com.team846.frc2015.config;

/*!
 * Constants used for Joystick Mappings and Driverstation virtual values
 */
public class DriverStationConfig {
	
	public class JoystickConfig
	{
		//Joystick Axis
		public static final int NUM_JOYSTICK_BUTTONS = 12;// Max supported by the driver station
		public static final int NUM_JOYSTICK_AXES = 3; //Doesnt recognize slider
		public static final int NUM_WHEEL_BUTTONS = 11;
		public static final int NUM_WHEEL_AXES = 3;
		
		//Joystick DriverStation Ports
		public static final int DRIVER_STICK_PORT = 0;
		public static final int OPERATOR_STICK_PORT = 1;
		public static final int DRIVER_WHEEL_PORT = 2;
	}

	public class JoystickButtons
	{
		// Driver Wheel
		public static final int FIELD_CENTRIC = 4;
				
		// Driver Joystick
		public static final int COLLECT_TOTE = 0;
		public static final int COLLECT_UPRIGHT_CONTAINER = 1;
		
		
		// Operator Joystick
		public static final int EXTEND_CARRIAGE = 0;
		
		public static final int LOAD_TOTE = 1;
		public static final int LOAD_UPRIGHT_CONTAINER = 2;
		public static final int LOAD_CONTAINER = 3;
		
		public static final int ELEVATE_ONE = 1;
		public static final int ELEVATE_TWO = 1;
		public static final int ELEVATE_THREE = 1;
		public static final int ELEVATE_FOUR = 1;

		public static final int DEPLOY_STACK = 7;
		
		
		
		//Operator overrides
		public static final int FRONT_HOOKS_OVERRIDE = 12;
		public static final int BACK_HOOKS_OVERRIDE = 11;
		
		public static final int ELEVATOR_OVERRIDE = 10;
		public static final int REVERSE_ROLLERS = 11;
		
		
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
		public static final int ELEVATOR = 4;
		public static final int CARRIAGE_HOOKS = 5;
		public static final int CARRIAGE_EXTENDER = 6;
		public static final int NETWORK = 7;
		public static final int COMPRESSOR = 8;
	}

	public class AnalogIns
	{
		public static final int AUTONOMOUS_DELAY = 1;
		public static final int AUTONOMOUS_SELECT = 2;
	}
}
