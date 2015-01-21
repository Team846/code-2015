package automation;

import componentData.CollectorData;

import actuators.LRTSpeedController;
import actuators.LRTTalon;
import sensors.SensorFactory;
import edu.wpi.first.wpilibj.DigitalInput;

public class Collect extends Automation
{
	int CHANGEME = 0;
	
	private DigitalInput proximitySensor;
	
	private CollectorData collectorData = CollectorData.get();
	
	public Collect()
	{
		super("Collect", false, false, true);
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
		collectorData.setSpeed(1.0);
		collectorData.setRunning(true);
		return true;
	}

	@Override
	protected boolean Abort()
	{
		collectorData.setRunning(false);
		return true;
	}

	@Override
	protected boolean Run()
	{
		return !proximitySensor.get();
	}
}
