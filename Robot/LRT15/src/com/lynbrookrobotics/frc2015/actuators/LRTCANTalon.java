package com.lynbrookrobotics.frc2015.actuators;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Talon;


public class LRTCANTalon extends Actuator
{
	private double speed;
	
	public static final int CHANGEME = 42;
	
	private CANTalon talon = new CANTalon(CHANGEME);
	
	public static ArrayList<LRTCANTalon> talon_list = new ArrayList<LRTCANTalon>();

	public LRTCANTalon(String name, int inputChannel)
	{
		super("CANTalon" + name);
		// TODO Auto-generated constructor stub
		talon_list.add(this);
	}

	@Override
	public void Output() 
	{
		// TODO Auto-generated method stub
		
	}
	
	public void SetDutyCycle(double speed)
	{
		this.speed = speed;
	}
	
	public double GetDutyCycle()
	{
		return speed;
	}
	
	public double GetHardwareValue()
	{
		return talon.get();
	}
	
	public void Disable()
	{
		speed = Talon.kPwmDisabled;
	}
	
	public void Update()
	{
		talon.set(speed);
		
//		if (brake_jumper != null)
//		{
//			if(neutral == LRTSpeedController.NeutralMode.kNeutralMode_Coast)
//				brake_jumper.set(true);
//			if(neutral == LRTSpeedController.NeutralMode.kNeutralMode_Brake)
//				brake_jumper.set(false);
//		}
	}
}
