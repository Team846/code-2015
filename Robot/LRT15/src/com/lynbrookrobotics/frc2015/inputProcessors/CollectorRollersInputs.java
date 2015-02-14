package com.lynbrookrobotics.frc2015.inputProcessors;

import com.lynbrookrobotics.frc2015.automation.ControlResource;
import com.lynbrookrobotics.frc2015.componentData.CollectorRollersData;
import com.lynbrookrobotics.frc2015.componentData.CollectorRollersData.Direction;
import com.lynbrookrobotics.frc2015.config.DriverStationConfig;
import com.lynbrookrobotics.frc2015.driverstation.LRTDriverStation;
import com.lynbrookrobotics.frc2015.driverstation.LRTJoystick;

public class CollectorRollersInputs extends InputProcessor
{
	LRTJoystick operatorStick;
	CollectorRollersData collectorRollers;
	
	public CollectorRollersInputs()
	{
		operatorStick = LRTDriverStation.Instance().GetOperatorStick();
		collectorRollers = CollectorRollersData.get();
		RegisterResource(ControlResource.COLLECTOR_ROLLERS);
	}

	@Override
	public void Update() {
		
		if(operatorStick.IsButtonDown(DriverStationConfig.JoystickButtons.REVERSE_ROLLERS))
		{
			collectorRollers.setRunning(true);
			collectorRollers.setDirection(Direction.REVERSE);
			collectorRollers.setSpeed(1.0);
		}
			
		
	}

}
