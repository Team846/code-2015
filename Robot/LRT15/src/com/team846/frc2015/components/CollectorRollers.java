package com.team846.frc2015.components;


import com.team846.frc2015.componentData.CollectorRollersData;
import com.team846.frc2015.componentData.CollectorRollersData.*;
import com.team846.frc2015.oldconfig.ConfigPortMappings;

import edu.wpi.first.wpilibj.CANTalon;

public class CollectorRollers extends Component
{
	private final CollectorRollersData rollersData;
	
	private final CANTalon leftMotor;
	private final CANTalon rightMotor;

	public CollectorRollers()
	{
		rollersData = CollectorRollersData.get(); 
		
		leftMotor = new CANTalon(
				ConfigPortMappings.Instance().get("CAN/COLLECTOR_LEFT"));
		rightMotor = new CANTalon(
				ConfigPortMappings.Instance().get("CAN/COLLECTOR_RIGHT"));
		
	}

	@Override
	protected void updateEnabled()
	{
		double left_speed = 0.0;
		double right_speed = 0.0;
		
		if (rollersData.isRunning())
		{
			if (rollersData.getDirection() == Direction.INTAKE)
			{
				left_speed = rollersData.getSpeed();
				right_speed = rollersData.getSpeed();
			}
			else if (rollersData.getDirection() == Direction.REVERSE)
			{
				left_speed = -rollersData.getSpeed();
				right_speed = -rollersData.getSpeed();
			}
			else if (rollersData.getDirection() == Direction.SWEEP_LEFT)
			{
				left_speed = -rollersData.getSpeed();
				right_speed = rollersData.getSpeed();
			}
			else if (rollersData.getDirection() == Direction.SWEEP_RIGHT)
			{
				left_speed = rollersData.getSpeed();
				right_speed = -rollersData.getSpeed();
			}
			else if (rollersData.getDirection() == Direction.LEFT_REVERSE)
			{
				left_speed = -rollersData.getSpeed();
				right_speed = 0;
			}
			else if (rollersData.getDirection() == Direction.RIGHT_REVERSE)
			{
				left_speed = 0;
				right_speed = -rollersData.getSpeed();
			}
		}
		else
		{
			left_speed = 0.0;
			right_speed = 0.0;
		}
		leftMotor.set(left_speed);
		rightMotor.set(right_speed);

	}

	@Override
	protected void updateDisabled()
	{
		leftMotor.set(0.0);
		rightMotor.set(0.0);
	}

	@Override
	protected void onEnabled()
	{
	}

	@Override
	protected void onDisabled()
	{
	}

}
