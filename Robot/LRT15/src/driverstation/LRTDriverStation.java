package driverstation;

import team846.robot.RobotState;
import config.DriverStationConfig;

public class LRTDriverStation {

	static LRTDriverStation m_instance = null;

	LRTJoystick m_driver_stick;
	LRTJoystick m_operator_stick;
	LRTJoystick m_driver_wheel;

	public void Initialize()
	{
		if (m_instance == null)
			m_instance = new LRTDriverStation();
	}

	LRTDriverStation Instance()
	{
		if (m_instance == null)
			m_instance = new LRTDriverStation();
		return m_instance;
	}

	void Finalize()
	{
		m_instance = null;
	}

	LRTDriverStation()
	{
		m_driver_stick = new LRTJoystick(
				DriverStationConfig.JoystickConfig.DRIVER_STICK_PORT,
				DriverStationConfig.JoystickConfig.NUM_JOYSTICK_BUTTONS,
				DriverStationConfig.JoystickConfig.NUM_JOYSTICK_AXES);
		m_operator_stick = new LRTJoystick(
				DriverStationConfig.JoystickConfig.OPERATOR_STICK_PORT,
				DriverStationConfig.JoystickConfig.NUM_JOYSTICK_BUTTONS,
				DriverStationConfig.JoystickConfig.NUM_JOYSTICK_AXES);
		m_driver_wheel = new LRTJoystick(
				DriverStationConfig.JoystickConfig.DRIVER_WHEEL_PORT,
				DriverStationConfig.JoystickConfig.NUM_WHEEL_BUTTONS,
				DriverStationConfig.JoystickConfig.NUM_WHEEL_AXES);
	}


	void Update()
	{
		if (RobotState.Instance().GameMode() != GameState.DISABLED)
		{
			Instance().m_driver_stick.Update();
			Instance().m_operator_stick.Update();
			Instance().m_driver_wheel.Update();
		}
	}

	LRTJoystick GetOperatorStick()
	{
		return m_operator_stick;

	}

	LRTJoystick GetDriverStick()
	{
		return m_driver_stick;

	}

	LRTJoystick GetDriverWheel()
	{
		return m_driver_wheel;
	}

}
