package com.team846.frc2015.actuators;

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
	private double timeoutSeconds;
	private float threshold;
	private final Timer timer = new Timer();
	
	LRTSpeedController(String name) {
		super(name);
		timeoutSeconds = 0;
	}
	
	public void output()
	{
		if (encoder != null)
		{
			if (Math.abs(GetDutyCycle()) >= threshold && encoder.getStopped())
			{
				timer.start();
				if (timer.get() >= timeoutSeconds * 1000000)
					SafetyCallback();
			}
			else
			{
				timer.stop();
				timer.reset();
			}
		}
		Update();
	}
	
	public void RegisterSafety(CounterBase encoder, double timeoutSeconds, float threshold)
	{
		this.encoder = encoder;
		this.timeoutSeconds = timeoutSeconds;
		this.threshold = threshold;
	}
	
	void SafetyCallback()
	{
		System.out.println("[ERROR] Safety failed in LRTSpeedController: " + getName());
		SetDutyCycle(0.0);
	}
	
	protected abstract void SetDutyCycle(double pwm);
	protected abstract double GetDutyCycle();
	public abstract double GetHardwareValue();
	protected abstract void Update();
	public abstract void ConfigNeutralMode(LRTSpeedController.NeutralMode mode);
	double CurrentLimit(double dutyCycle, double speed, double forwardLimit, double reverseLimit)
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
