package sensors;

import config.Configurable;
import edu.wpi.first.wpilibj.AnalogInput;

public class ContinuousPotentiometer implements Configurable
{
	AnalogInput channelA;
	AnalogInput channelB;
	
	private float minVoltage;
	private float maxVoltage;
	private float middleRangeAngle;
	
	public ContinuousPotentiometer(int portA, int portB)
	{
		channelA = SensorFactory.GetAnalogInput(portA);
		channelB = SensorFactory.GetAnalogInput(portB);
	}
	
	
	public float GetAngle()
	{
		float range = maxVoltage - minVoltage; // Full voltage range
		double middleRange = middleRangeAngle / 360.0 * range; // Voltage range of discontinuity of both phases
		double voltage_a = channelA.getAverageVoltage();
		double voltage_b = channelB.getAverageVoltage();
		// Linearize both channels
		if (voltage_a > minVoltage + range / 2 - middleRange / 2 && voltage_a < maxVoltage - range / 2 + middleRange / 2) // Channel B is discontinuous
		{
			if (voltage_a < minVoltage + range / 2) // Channel B is at maximum
			{
				voltage_b = voltage_a + range / 2; // Add half phase to Channel A to get correct value for Channel B
			}
			else // Channel B is at minimum
			{
				voltage_b = voltage_a - range / 2; // Subtract half phase from Channel A to get correct value for Channel B
			}
		}
		else if (voltage_b > minVoltage + range / 2 - middleRange / 2 && voltage_b < maxVoltage - range / 2 + middleRange / 2) // Channel A is discontinuous
		{
			if (voltage_b < minVoltage + range / 2) // Channel A is at maximum
			{
				voltage_a = voltage_b + range / 2; // Add half phase to Channel B to get correct value for Channel A
			}
			else // Channel A is at minimum
			{
				voltage_a = voltage_b - range / 2; // Subtract half phase from Channel B to get correct value for Channel A
			}
		}
		// Return average of both channels
		if (voltage_b > voltage_a) // 0-180 degrees
			return (float) (((voltage_a + voltage_b - range / 2) / 2 - minVoltage) / range * 360);
		else // 180-360 degrees
			return (float) (((voltage_a + voltage_b + range / 2) / 2 - minVoltage) / range * 360);
	}
	
	public void Configure()
	{
		
	}

}
