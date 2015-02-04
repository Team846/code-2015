package com.lynbrookrobotics.frc2015.componentData;

import edu.wpi.first.wpilibj.Ultrasonic;

public class ElevatorData extends ComponentData{

	public int maxSPEED = 1;

	public double speed = 0.0;

	public static final int STAY = 1;
	public static final int UP = 2;
	public static final int DOWN= 3;
	private int motion = 1;
	public static double distance;
	public int numberOfTotes;

	public static final int GROUND = 0;
	public final int TOTE1 = 12;
	public final int TOTE2 = 24;
	public final int TOTE3 = 36;
	
	
	private int state = 1;
	public static int desiredState;

	public Ultrasonic ultrasonic;

	public ElevatorData() {
		super("ElevatorData");
	}
	
	public int getCurrentMotion(){
		return motion; 
	}

	public int setDesiredMotion(int i){
		if(i == STAY || i == UP || i == DOWN){
			motion = i;
		}
		return motion; 
	}

	public int getMaxSpeed(){
		return maxSPEED;
	}

	public int setMaxSpeed(int desiredSpeed){
		maxSPEED = desiredSpeed;
		return maxSPEED;
	}

	public double getSpeed(){
		return speed;
	}

	public double setSpeed(double desiredSpeed){
		speed = desiredSpeed;
		return speed;
	}

	public int getDesiredState(){
		return desiredState;
	}

	public int setDesiredState(int i){
		if(i == GROUND || i == TOTE1 || i == TOTE2 || i == TOTE3){
			state = i;
		}
		return desiredState;
	}


	@Override
	protected void ResetCommands() {

	}

}
