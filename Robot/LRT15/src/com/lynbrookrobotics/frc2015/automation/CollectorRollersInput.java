package com.lynbrookrobotics.frc2015.automation;

import com.lynbrookrobotics.frc2015.componentData.CollectorRollersData;
import com.lynbrookrobotics.frc2015.componentData.CollectorRollersData.Direction;
import com.lynbrookrobotics.frc2015.config.DriverStationConfig;
import com.lynbrookrobotics.frc2015.driverstation.LRTDriverStation;
import com.lynbrookrobotics.frc2015.driverstation.LRTJoystick;

public class CollectorRollersInput extends InputProcessor
{
	LRTJoystick operatorStick;
	CollectorRollersData collectorRollers;
	
	public CollectorRollersInput()
	{
		operatorStick = LRTDriverStation.Instance().GetOperatorStick();
		collectorRollers = CollectorRollersData.get();
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
