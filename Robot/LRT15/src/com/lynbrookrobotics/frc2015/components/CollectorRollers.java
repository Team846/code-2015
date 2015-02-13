package com.lynbrookrobotics.frc2015.components;


import com.lynbrookrobotics.frc2015.actuators.LRTSpeedController;
import com.lynbrookrobotics.frc2015.actuators.LRTTalon;
import com.lynbrookrobotics.frc2015.componentData.CollectorRollersData;
import com.lynbrookrobotics.frc2015.componentData.CollectorRollersData.*;
import com.lynbrookrobotics.frc2015.config.ConfigPortMappings;
import com.lynbrookrobotics.frc2015.config.DriverStationConfig;

import edu.wpi.first.wpilibj.CANTalon;

public class CollectorRollers extends Component
{
	private CollectorRollersData rollersData;
	
	private CANTalon leftMotor;
	private CANTalon rightMotor;

	public CollectorRollers()
	{
		super("Collector", DriverStationConfig.DigitalIns.NO_DS_DI);
		rollersData = CollectorRollersData.get();
		
		leftMotor = new CANTalon(
				ConfigPortMappings.Instance().get("CAN/COLLECTOR_LEFT"));
		rightMotor = new CANTalon(
				ConfigPortMappings.Instance().get("CAN/COLLECTOR_RIGHT"));
		
	}

	@Override
	protected void UpdateEnabled()
	{
		double speed;
		
		if(rollersData.isRunning())
		{
			if(rollersData.getDirection() == Direction.FORWARD)
				speed = rollersData.getSpeed();
			else
				speed = -rollersData.getSpeed();
		}
		else
		{
			speed = 0.0;
		}
		
		leftMotor.set(speed);
		rightMotor.set(-speed);

	}

	@Override
	protected void UpdateDisabled()
	{
		leftMotor.set(0.0);
		rightMotor.set(0.0);
	}

	@Override
	protected void OnEnabled()
	{
	}

	@Override
	protected void OnDisabled()
	{
	}

}
