package com.team846.frc2015.actuators;

import java.util.ArrayList;

public abstract class Actuator 
{
	private final String name;
	private static final ArrayList<Actuator> actuator_list = new ArrayList<Actuator>();

	Actuator(String name)
	{
		this.name = name;
		actuator_list.add(this);
	}
	
	String getName()
	{
		return name;
	}
	
	protected abstract void output();
	
	public static void outputAll()
	{
		for ( Actuator a : Actuator.actuator_list)
		{ 
			a.output();
		}
	}
}
