package automation;

import componentData.CollectorRollersData;
import componentData.CollectorRollersData.Direction;
import sensors.SensorFactory;
import edu.wpi.first.wpilibj.DigitalInput;

public class Collect extends Automation
{
	int CHANGEME = 0;
	
	private DigitalInput proximitySensor;
	
	private CollectorRollersData collectorData = CollectorRollersData.get();
	
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
		collectorData.setRunning(true);
        collectorData.setDirection(Direction.FORWARD);
		collectorData.setSpeed(1.0);
		
		return !proximitySensor.get();
	}
}
