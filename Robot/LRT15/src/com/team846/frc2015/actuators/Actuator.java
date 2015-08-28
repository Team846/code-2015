package com.team846.frc2015.actuators;

import java.util.ArrayList;

public abstract class Actuator {

	private final String name;
	private static final ArrayList<Actuator> ACTUATOR_LIST = new ArrayList<Actuator>();

	Actuator(String name) {
		this.name = name;
		ACTUATOR_LIST.add(this);
	}
	
	String getName() {
		return name;
	}
	
	protected abstract void output();
	
	public static void outputAll() {
		for ( Actuator a : Actuator.ACTUATOR_LIST){
			a.output();
		}
	}
}
