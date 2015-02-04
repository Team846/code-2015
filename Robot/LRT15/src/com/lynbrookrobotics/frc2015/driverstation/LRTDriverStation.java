package com.lynbrookrobotics.frc2015.driverstation;

import com.lynbrookrobotics.frc2015.config.DriverStationConfig;

import team846.robot.RobotState;

public class LRTDriverStation {

	static LRTDriverStation instance = null;

	LRTJoystick driver_stick;
	LRTJoystick operator_stick;
	LRTJoystick driver_wheel;

	public static void Initialize()
	{
		if (instance == null)
			instance = new LRTDriverStation();
	}

	public static LRTDriverStation Instance()
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


	public static void Update()
	{
		if (RobotState.Instance().GameMode() != GameState.DISABLED)
		{
			Instance().driver_stick.Update();
			Instance().operator_stick.Update();
			Instance().driver_wheel.Update();
		}
	}

	public LRTJoystick GetOperatorStick()
	{
		return operator_stick;
	}

	public LRTJoystick GetDriverStick()
	{
		return driver_stick;
	}

	public LRTJoystick GetDriverWheel()
	{
		return driver_wheel;
	}

}
