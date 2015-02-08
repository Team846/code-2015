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
	private CollectorRollersData collectorData;
	
	private CANTalon leftMotor;
	private CANTalon rightMotor;

	public CollectorRollers()
	{
		super("Collector", DriverStationConfig.DigitalIns.NO_DS_DI);
		collectorData = CollectorRollersData.get();
		
		leftMotor = new CANTalon(
				ConfigPortMappings.Instance().Get("CAN/COLLECTOR_LEFT"));
		rightMotor = new CANTalon(
				ConfigPortMappings.Instance().Get("CAN/COLLECTOR_RIGHT"));
		
	}

	@Override
	protected void UpdateEnabled()
	{
		double speed;
		
		if(collectorData.isRunning())
		{
			if(collectorData.getDirection() == Direction.FORWARD)
				speed = collectorData.getSpeed();
			else
				speed = -collectorData.getSpeed();
		}
		else
		{
			speed = 0.0;
		}
		
		leftMotor.set(speed);
		rightMotor.set(speed);

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
