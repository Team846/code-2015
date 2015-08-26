package com.team846.frc2015.config;

/*!
 * Constants used for Joystick Mappings and Driverstation virtual values
 */
public class DriverStationConfig
{
	
	public class JoystickConfig
	{
		//Joystick Axis
		public static final int NUM_JOYSTICK_BUTTONS = 16;// Max supported by the driver station
		public static final int NUM_JOYSTICK_AXES = 3;
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
		public static final int COLLECT = 1;
		public static final int ADVANCE_STATE =2;
		
		// Operator Joystick
		public static final int EXTEND_CARRIAGE = 1;
		public static final int DEPLOY_STACK = 2;
		
		public static final int LOAD_TOTE = 15;
		public static final int LOAD_SIDEWAYS_CONTAINER = 4;
		public static final int LOAD_UPRIGHT_CONTAINER = 8;
		public static final int LOAD_STACK = 11;
		
		public static final int ELEVATE_ONE = 5;
		public static final int ELEVATE_TWO = 6;
		public static final int ELEVATE_THREE = 7;
		public static final int ELEVATE_FOUR = 10;
		public static final int ELEVATE_STEP = 12;
		
		public static final int HUMAN_LOAD_START = 3;
		public static final int HUMAN_LOAD_FINISH = 16;
		
		// Overrides
		public static final int HOOKS_OVERRIDE = 13;
		public static final int LOAD_ADDITIONAL = 9;
		public static final int ELEVATOR_OVERRIDE = 14;
		public static final int CARRIAGE_OVERRIDE = 11;

		public static final int SPIN_ROLLERS = 15;
		public static final int REVERSE_ROLLERS = 16;
		
		public static final int LEFT_CONTAINER_ARM = 12;
		public static final int RIGHT_CONTAINER_ARM = 11;

		
		// Dashboard Mode
		public static final int DASHBOARD_ENABLE1 = 9;
		public static final int DASHBOARD_ENABLE2 = 10;

		public static final int PIVOT = 5;

		public static final int DRIVER_SWEEP_LEFT = 3;
		public static final int DRIVER_SWEEP_RIGHT = 4;
	}

	private class AnalogIns
	{
		public static final int AUTONOMOUS_DELAY = 1;
		public static final int AUTONOMOUS_SELECT = 2;
	}
}
