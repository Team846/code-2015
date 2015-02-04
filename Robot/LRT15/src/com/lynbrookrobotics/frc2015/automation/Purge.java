package com.lynbrookrobotics.frc2015.automation;

import com.lynbrookrobotics.frc2015.componentData.CollectorRollersData;
import com.lynbrookrobotics.frc2015.sensors.SensorFactory;

import edu.wpi.first.wpilibj.DigitalInput;

public class Purge extends Automation
{
	static final int COUNTDOWN_LENGTH = 15;

	int CHANGEME = 0;

	private DigitalInput proximitySensor;

	private CollectorRollersData collectorRollersData = CollectorRollersData.get();

	int countdown = -1;

	public Purge()
	{
		super("Purge", false, false, true);
		proximitySensor = SensorFactory.GetDigitalInput(CHANGEME);
	}

	@Override
	public void AllocateResources()
	{
		AllocateResource(ControlResource.COLLECTOR_ROLLERS);
	}

	@Override
	protected boolean Start()
	{
		collectorRollersData.setSpeed(-1.0);
		collectorRollersData.setRunning(true);
		return true;
	}

	@Override
	protected boolean Abort()
	{
		countdown = -1;
		collectorRollersData.setRunning(false);
		return true;
	}

	@Override
	protected boolean Run()
	{
		if (countdown < 0 && proximitySensor.get())
		{
			countdown = COUNTDOWN_LENGTH;
		} else if (countdown == 0)
		{
			return true;
		} else
		{
			countdown--;
		}

		return false;
	}

}
