package com.lynbrookrobotics.frc2015.automation;

import com.lynbrookrobotics.frc2015.componentData.CollectorArmData;
import com.lynbrookrobotics.frc2015.componentData.CollectorArmData.Position;
import com.lynbrookrobotics.frc2015.componentData.CollectorRollersData;
import com.lynbrookrobotics.frc2015.componentData.CollectorRollersData.Direction;
import com.lynbrookrobotics.frc2015.config.ConfigPortMappings;
import com.lynbrookrobotics.frc2015.sensors.SensorFactory;

import edu.wpi.first.wpilibj.DigitalInput;

public class Collect extends Automation
{
	
	private DigitalInput proximitySensor;
	
	private CollectorRollersData rollersData = CollectorRollersData.get();
	private CollectorArmData armData = CollectorArmData.get();
	
	public Collect()
	{
		super("Collect", false, false, true);
		
		proximitySensor = SensorFactory.GetDigitalInput(
				ConfigPortMappings.Instance().Get("Digital/COLLECTOR_PROX"));
	}

	@Override
	public void AllocateResources()
	{
		AllocateResource(ControlResource.COLLECTOR_ARMS);
		AllocateResource(ControlResource.COLLECTOR_ROLLERS);
	}

	@Override
	protected boolean Start()
	{
		return true;
	}

	@Override
	protected boolean Abort()
	{
		rollersData.setRunning(false);
		return true;
	}

	@Override
	protected boolean Run()
	{
		armData.setDesiredCollectorState(Position.EXTEND);
		rollersData.setRunning(true);
        rollersData.setDirection(Direction.FORWARD);
		rollersData.setSpeed(1.0);
		
		if(proximitySensor.get())
		{
			rollersData.setRunning(false);
			return true;
		}
			
		return false;
	}
}
