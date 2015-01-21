package components;

import componentData.CollectorData;
import actuators.LRTSpeedController;
import actuators.LRTTalon;

public class Collector extends Component
{
	private CollectorData collectorData;
	private LRTSpeedController motor;
	private LRTSpeedController motor1;
	private final int CHANGEME = 99;

	public Collector(int driverStationDigitalIn)
	{
		super("Collector", driverStationDigitalIn);
		collectorData = CollectorData.get();
		motor = new LRTTalon(CHANGEME, "collectorMotor", CHANGEME);
		// CHANGEME
		motor1 = motor;
	}

	@Override
	protected void UpdateEnabled()
	{
		double speed = 0.0;
		if (collectorData.isRunning())
		{

//			if (collectorData.getIO())
//			{
//
//				speed = collectorData.getSpeed();
//			}
//
//			else if (!collectorData.getIO())
//			{
//				speed = -collectorData.getSpeed();
//			} else
//			{
//				speed = 0;
//			}
		}
		// assuming motor 1 and motor are mounted in such a way that the same
		// dutyCycle will make input not work
		motor.SetDutyCycle(speed);
		motor1.SetDutyCycle(-speed);

	}

	@Override
	protected void UpdateDisabled()
	{
		motor.SetDutyCycle(0);
		motor1.SetDutyCycle(0);
	}

	@Override
	protected void OnEnabled()
	{

	}

	@Override
	protected void OnDisabled()
	{
		// TODO Auto-generated method stub

	}

}
