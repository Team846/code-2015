package com.team846.frc2015.actuators;

import java.util.ArrayList;

import com.team846.frc2015.config.ConfigRuntime;
import com.team846.frc2015.config.Configurable;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Talon;

public class LRTTalon extends LRTSpeedController implements Configurable {
	
	private static final ArrayList<LRTTalon> talon_list = new ArrayList<LRTTalon>();
	
	private double pwm;
	private final DigitalOutput brake_jumper;
	private LRTSpeedController.NeutralMode neutral;
	
	private double forwardLimit;
	private double reverseLimit;

	private double speedThreshold;
	
	private final Talon talon;

	public LRTTalon(int channel, String name, int jumperChannel)
		//LRTSpeedController("LRTTalon" + name),
		//brake_jumper(jumperChannel != 0 ? new DigitalOutput(jumperChannel) : NULL)
	{
		super("LRTTalon"+name);
		talon = new Talon(channel);
		brake_jumper = (jumperChannel != 0 ? new DigitalOutput(jumperChannel) : null);
		pwm = 0.0;
		neutral = LRTSpeedController.NeutralMode.kNeutralMode_Coast;
		talon_list.add(this);
		
		System.out.println("Constructed LRTTalon" + name+" on channel " + channel);
		
		ConfigRuntime.Register(this);
	}
	
	@Override
	public void Configure() {
		forwardLimit = GetConfig("forwardLimit", 0.3);
		reverseLimit = GetConfig("reverseLimit", -0.3);

		speedThreshold = GetConfig("reverseLimit", 0.05);
	}

	public void SetDutyCycle(double newDutyCycle)
	{
		double currentSpeed = talon.getSpeed();

		if (-speedThreshold < currentSpeed && speedThreshold > currentSpeed) {
			// don't do current limiting if Talon reports speed within threshold
			pwm = newDutyCycle;
		} else {
			pwm = CurrentLimit(newDutyCycle, currentSpeed, forwardLimit, reverseLimit);
		}
	}

	public double GetDutyCycle()
	{
		return pwm;
	}

	public double GetHardwareValue()
	{
		return talon.get();
	}

	public void Set( float speed )
	{
		System.out.println("[WARNING] Calling Set() in LRTTalon: "+ getName() +" use setDutyCycle() instead");
		SetDutyCycle(speed);
	}

	public double Get()
	{
		return pwm;
	}

	public void Disable()
	{
		pwm = Talon.kPwmDisabled;
	}

	public void PIDWrite( float output) 
	{
		SetDutyCycle(output);
	}

	public void ConfigNeutralMode(LRTSpeedController.NeutralMode mode)
	{
		neutral = mode;
	}

	LRTSpeedController.NeutralMode GetNeutralMode()
	{
		return neutral;
	}

	public void Update()
	{
		talon.set(pwm);
		if (brake_jumper != null)
		{
			if(neutral == LRTSpeedController.NeutralMode.kNeutralMode_Coast)
				brake_jumper.set(true);
			if(neutral == LRTSpeedController.NeutralMode.kNeutralMode_Brake)
				brake_jumper.set(false);
		}
	}

}
