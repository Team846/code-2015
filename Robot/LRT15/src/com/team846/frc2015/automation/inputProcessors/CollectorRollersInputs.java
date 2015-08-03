package com.team846.frc2015.automation.inputProcessors;

import com.team846.frc2015.automation.ControlResource;
import com.team846.frc2015.componentData.CollectorRollersData;
import com.team846.frc2015.componentData.CollectorRollersData.Direction;
import com.team846.frc2015.config.DriverStationConfig;
import com.team846.frc2015.driverstation.LRTDriverStation;
import com.team846.frc2015.driverstation.LRTJoystick;

public class CollectorRollersInputs extends InputProcessor
{
	private final LRTJoystick operatorStick;
	private final LRTJoystick driverStick;

	private final CollectorRollersData collectorRollers;
	
	public CollectorRollersInputs()
	{
		operatorStick = LRTDriverStation.instance().getOperatorStick();
		driverStick = LRTDriverStation.instance().getDriverStick();
		collectorRollers = CollectorRollersData.get();
		RegisterResource(ControlResource.COLLECTOR_ROLLERS);
	}

	@Override
	public void Update() {
		
		if(driverStick.isButtonDown(DriverStationConfig.JoystickButtons.REVERSE_ROLLERS))
		{
			collectorRollers.setRunning(true);
			collectorRollers.setDirection(Direction.REVERSE);
			collectorRollers.setSpeed(1.0);
		}
		else if(driverStick.isButtonDown(DriverStationConfig.JoystickButtons.SPIN_ROLLERS))
		{
			collectorRollers.setRunning(true);
			collectorRollers.setDirection(Direction.INTAKE);
			collectorRollers.setSpeed(1.0);
		}
		else if(driverStick.isButtonDown(DriverStationConfig.JoystickButtons.DRIVER_SWEEP_LEFT))
		{
			collectorRollers.setRunning(true);
			collectorRollers.setDirection(Direction.LEFT_REVERSE);
			collectorRollers.setSpeed(1.0);
		}
		else if(driverStick.isButtonDown(DriverStationConfig.JoystickButtons.DRIVER_SWEEP_RIGHT))
		{
			collectorRollers.setRunning(true);
			collectorRollers.setDirection(Direction.RIGHT_REVERSE);
			collectorRollers.setSpeed(1.0);
		}
			
		
	}

}
