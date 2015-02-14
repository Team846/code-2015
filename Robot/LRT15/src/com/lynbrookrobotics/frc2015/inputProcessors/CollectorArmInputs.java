package com.lynbrookrobotics.frc2015.inputProcessors;

import com.lynbrookrobotics.frc2015.automation.ControlResource;
import com.lynbrookrobotics.frc2015.componentData.CollectorArmData;
import com.lynbrookrobotics.frc2015.componentData.CollectorArmData.Position;
import com.lynbrookrobotics.frc2015.config.DriverStationConfig;
import com.lynbrookrobotics.frc2015.driverstation.LRTDriverStation;
import com.lynbrookrobotics.frc2015.driverstation.LRTJoystick;

public class CollectorArmInputs extends InputProcessor {

	private CollectorArmData armData;
	private LRTJoystick driverStick;
	public CollectorArmInputs()
	{
		RegisterResource(ControlResource.COLLECTOR_ARMS);
		armData = CollectorArmData.get();
		driverStick = LRTDriverStation.Instance().GetDriverStick();
	}
	
	@Override
	public void Update() {
		if(driverStick.IsButtonDown(DriverStationConfig.JoystickButtons.COLLECT_TOTE))
			armData.setDesiredPosition(Position.EXTEND);

	}

}
