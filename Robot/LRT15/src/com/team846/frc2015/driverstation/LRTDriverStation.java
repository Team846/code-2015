package com.team846.frc2015.driverstation;

import com.team846.frc2015.config.DriverStationConfig;
import com.team846.robot.RobotState;

public class LRTDriverStation {

	private static LRTDriverStation instance = null;

	private final LRTJoystick driver_stick;
	private final LRTJoystick operator_stick;
	private final LRTJoystick driver_wheel;

	public static void initialize()
	{
		if (instance == null)
			instance = new LRTDriverStation();
	}

	public static LRTDriverStation instance()
	{
		if (instance == null)
			instance = new LRTDriverStation();
		return instance;
	}

	public static void Finalize()
	{
		instance = null;
	}

	private LRTDriverStation()
	{
		driver_stick = new LRTJoystick(
				DriverStationConfig.JoystickConfig.DRIVER_STICK_PORT,
				DriverStationConfig.JoystickConfig.NUM_JOYSTICK_BUTTONS,
				DriverStationConfig.JoystickConfig.NUM_JOYSTICK_AXES);
		operator_stick = new LRTJoystick(
				DriverStationConfig.JoystickConfig.OPERATOR_STICK_PORT,
				DriverStationConfig.JoystickConfig.NUM_JOYSTICK_BUTTONS,
				DriverStationConfig.JoystickConfig.NUM_JOYSTICK_AXES);
		driver_wheel = new LRTJoystick(
				DriverStationConfig.JoystickConfig.DRIVER_WHEEL_PORT,
				DriverStationConfig.JoystickConfig.NUM_WHEEL_BUTTONS,
				DriverStationConfig.JoystickConfig.NUM_WHEEL_AXES);
	}


	public static void update()
	{
		if (RobotState.Instance().GameMode() != GameState.DISABLED)
		{
			instance().driver_stick.update();
			instance().operator_stick.update();
			instance().driver_wheel.update();
		}
	}

	public LRTJoystick getOperatorStick()
	{
		return operator_stick;
	}

	public LRTJoystick getDriverStick()
	{
		return driver_stick;
	}
	

	public LRTJoystick getDriverWheel()
	{
		return driver_wheel;
	}

}
