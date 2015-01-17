package actuators;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Timer;

public abstract class LRTSpeedController extends Actuator
{
	public enum NeutralMode
	{
		kNeutralMode_Jumper,
		kNeutralMode_Brake, 
		kNeutralMode_Coast
	}
	
	private CounterBase encoder = null;
	private double m_timeoutSeconds;
	private float m_threshold;
	private Timer m_timer;
	
	public LRTSpeedController(String name) {
		super(name);
		m_timeoutSeconds = 0;
	}
	
	public void Output()
	{
		if (encoder != null)
		{
			if (Math.abs(GetDutyCycle()) >= m_threshold && encoder.getStopped())
			{
				m_timer.start();
				if (m_timer.get() >= m_timeoutSeconds * 1000000)
					SafetyCallback();
			}
			else
			{
				m_timer.stop();
				m_timer.reset();
			}
		}
		Update();
	}
	
	public void RegisterSafety(CounterBase encoder, double timeoutSeconds, float threshold)
	{
		encoder = encoder;
		m_timeoutSeconds = timeoutSeconds;
		m_threshold = threshold;
	}
	
	public void SafetyCallback()
	{
		System.out.println("[ERROR] Safety failed in LRTSpeedController: " + GetName());
		SetDutyCycle(0.0);
	}
	
	public abstract void SetDutyCycle(double pwm);
	public abstract double GetDutyCycle();
	public abstract double GetHardwareValue();
	public abstract void Update();
	public abstract void ConfigNeutralMode(LRTSpeedController.NeutralMode mode);
	double CurrentLimit(double dutyCycle, float speed, float forwardLimit, float reverseLimit)
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
