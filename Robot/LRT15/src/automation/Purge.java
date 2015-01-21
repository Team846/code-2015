package automation;

import sensors.SensorFactory;
import componentData.CollectorData;
import edu.wpi.first.wpilibj.DigitalInput;

public class Purge extends Automation
{
	static final int COUNTDOWN_LENGTH = 15;

	int CHANGEME = 0;

	private DigitalInput proximitySensor;

	private CollectorData collectorData = CollectorData.get();

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
		collectorData.setSpeed(-1.0);
		collectorData.setRunning(true);
		return true;
	}

	@Override
	protected boolean Abort()
	{
		countdown = -1;
		collectorData.setRunning(false);
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
