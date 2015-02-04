package com.lynbrookrobotics.frc2015.actuators;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.SafePWM;

public abstract class Actuator 
{
	private String name;
	public static ArrayList<Actuator> actuator_list = new ArrayList<Actuator>();

	public Actuator(String name)
	{
		this.name = name;
		actuator_list.add(this);
	}
	
	public String GetName()
	{
		return name;
	}
	
	public abstract void Output();
	
	public static void OutputAll()
	{
		for ( Actuator a : Actuator.actuator_list)
		{ 
			a.Output();
		}
	}
}
