package com.team846.frc2015.automation.inputProcessors;

import com.team846.frc2015.automation.ControlResource;
import com.team846.frc2015.componentData.CollectorRollersData;
import com.team846.frc2015.componentData.CollectorRollersData.Direction;
import com.team846.frc2015.config.DriverStationConfig;
import com.team846.frc2015.driverstation.LRTDriverStation;
import com.team846.frc2015.driverstation.LRTJoystick;

public class CollectorRollersInputs extends InputProcessor
{
	LRTJoystick operatorStick;
	LRTJoystick driverStick;

	CollectorRollersData collectorRollers;
	
	public CollectorRollersInputs()
	{
		operatorStick = LRTDriverStation.Instance().GetOperatorStick();
		driverStick = LRTDriverStation.Instance().GetDriverStick();
		collectorRollers = CollectorRollersData.get();
		RegisterResource(ControlResource.COLLECTOR_ROLLERS);
	}

	@Override
	public void Update() {
		
		if(driverStick.IsButtonDown(DriverStationConfig.JoystickButtons.REVERSE_ROLLERS))
		{
			collectorRollers.setRunning(true);
			collectorRollers.setDirection(Direction.REVERSE);
			collectorRollers.setSpeed(1.0);
		}
		else if(driverStick.IsButtonDown(DriverStationConfig.JoystickButtons.SPIN_ROLLERS))
		{
			collectorRollers.setRunning(true);
			collectorRollers.setDirection(Direction.INTAKE);
			collectorRollers.setSpeed(1.0);
		}
		else if(driverStick.IsButtonDown(DriverStationConfig.JoystickButtons.SWEEP))
		{
			collectorRollers.setRunning(true);
			collectorRollers.setDirection(Direction.SWEEP_LEFT);
			collectorRollers.setSpeed(1.0);
		}
		
			
		
	}

}
