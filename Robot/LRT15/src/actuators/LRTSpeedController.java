package actuators;

public interface LRTSpeedController
{
	enum NeutralMode
	{
		kNeutralMode_Jumper,
		kNeutralMode_Brake, 
		kNeutralMode_Coast
	}
	
	default double CurrentLimit(double dutyCycle, float speed, float forwardLimit, float reverseLimit)
	{
		if (speed < 0)
		{
			return -CurrentLimit(-dutyCycle, -speed, forwardLimit, reverseLimit);
		}
		// At this point speed >= 0
		if (dutyCycle > speed) // Current limit accelerating
		{
			dutyCycle = Math.min(dutyCycle, speed + forwardLimit);
		}
		else if (dutyCycle < 0) // Current limit reversing direction
		{
			double limitedDutyCycle = -reverseLimit / (1.0 + speed); // speed >= 0 so dutyCycle < -currentLimit
			dutyCycle = Math.max(dutyCycle, limitedDutyCycle); // Both are negative
		}
		return dutyCycle;
	}
	

}
