package com.lynbrookrobotics.frc2015.components;


import com.lynbrookrobotics.frc2015.actuators.LRTSpeedController;
import com.lynbrookrobotics.frc2015.actuators.LRTTalon;
import com.lynbrookrobotics.frc2015.componentData.CollectorRollersData;
import com.lynbrookrobotics.frc2015.componentData.CollectorRollersData.*;

public class CollectorRollers extends Component
{
	private CollectorRollersData collectorData;
	
	private LRTSpeedController leftMotor;
	private LRTSpeedController rightMotor;
	
	private final static int CHANGEME = 99;

	public CollectorRollers()
	{
		super("Collector", CHANGEME);
		collectorData = CollectorRollersData.get();
		
		leftMotor = new LRTTalon(CHANGEME, "collectorMotor_left", CHANGEME);
		rightMotor = new LRTTalon(CHANGEME, "collectorMotor_right", CHANGEME);
		
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
		
		leftMotor.SetDutyCycle(speed);
		rightMotor.SetDutyCycle(speed);

	}

	@Override
	protected void UpdateDisabled()
	{
		leftMotor.SetDutyCycle(0.0);
		rightMotor.SetDutyCycle(0.0);
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
