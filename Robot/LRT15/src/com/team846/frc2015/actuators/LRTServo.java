package com.team846.frc2015.actuators;

import com.team846.frc2015.logging.AsyncLogger;

import edu.wpi.first.wpilibj.Servo;

public class LRTServo extends Actuator
{
	public enum ControlMode
	{
		kValue, kMicroseconds, kAngle
	}
	
	private Servo servo;
	private ControlMode controlMode;
    private double value;
    private boolean enabled;
    private double previous_value;
    
	private static final int MIN_VAL = 727;
	private static final int MAX_VAL = 2252;

	public LRTServo(String name)
	{
		super(name);
	}
	
	public LRTServo(int channel, String name)
	{
		super(name);
		servo = new Servo(channel);
		controlMode = ControlMode.kValue;
		value = 0.0;
		enabled = false;
		previous_value = 999.0;
			
	    AsyncLogger.info("Created LRTServo " + name + " on channel " + channel);
	}


	public void output()
	{
		if (enabled)
		{
			switch(controlMode)
			{
			case kValue:
		        servo.set(value);
				break;
			case kMicroseconds:
		    	float val = (float)(value - MIN_VAL) / (MAX_VAL - MIN_VAL);
		        servo.set(val);
				break;
			case kAngle:
		        servo.setAngle(value);
				break;
			}
		}
		else
	       ;// servo.SetOffline(); TODO:Find Safety
	}

	public void SetEnabled(boolean enabled)
	{
	    this.enabled = enabled;
	}
	
	boolean IsEnabled()
	{
	    return enabled;
	}
	
	public void Set(float value)
	{
		controlMode = ControlMode.kValue;
		this.value = value;
	}
	
	public void SetMicroseconds(int ms) 
	{
		controlMode = ControlMode.kMicroseconds;
		value = Math.max(MIN_VAL, Math.min(MAX_VAL,ms));
	}
	
	public void SetAngle(float angle)
	{
		controlMode = ControlMode.kAngle;
		value = angle;
	}
	
	public void SetControlMode(ControlMode mode)
	{
		controlMode = mode;
	}
	
	public double Get()
	{
		return value;
	}
	
	public double GetHardwareValue()
	{
		return servo.get();
	}
	
	public ControlMode GetControlMode()
	{
		return controlMode;
	}
}
