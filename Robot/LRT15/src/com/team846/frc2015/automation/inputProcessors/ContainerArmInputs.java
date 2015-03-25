package com.team846.frc2015.automation.inputProcessors;

import com.team846.frc2015.automation.ControlResource;
import com.team846.frc2015.componentData.CollectorArmData;
import com.team846.frc2015.componentData.CollectorArmData.ArmPosition;
import com.team846.frc2015.componentData.ContainerArmData;
import com.team846.frc2015.config.DriverStationConfig;
import com.team846.frc2015.driverstation.LRTDriverStation;
import com.team846.frc2015.driverstation.LRTJoystick;

public class ContainerArmInputs extends InputProcessor {

	private final ContainerArmData armData;
	private final LRTJoystick driverStick;
	
	public ContainerArmInputs()
	{
		armData = ContainerArmData.get();
		driverStick = LRTDriverStation.Instance().GetDriverStick();
	}
	
	@Override
	public void Update() {
		armData.SetLeftDeployed(driverStick.IsButtonDown(DriverStationConfig.JoystickButtons.LEFT_CONTAINER_ARM));
		armData.SetRightDeployed(driverStick.IsButtonDown(DriverStationConfig.JoystickButtons.RIGHT_CONTAINER_ARM));
	}

}