package com.team846.frc2015.componentData;

import edu.wpi.first.wpilibj.Ultrasonic;

public class ElevatorData extends ComponentData{
	
	private double speed;
	private int position;

	private ElevatorControlMode controlMode;

	private ElevatorSetpoint setpoint;
	private ElevatorSetpoint currentSetpoint;
	
	private double errorThreshold  = 10;
	private int currentPosition = 0;
	
	public enum ElevatorControlMode
	{
		POSITION,
		VELOCITY,
		SETPOINT
	}
	
	public enum ElevatorSetpoint
	{
		// First object sequence is collect, grab, home
		// Additional object sequence is COLLECT_ADDITIONAL, GRAB_TOTE, HOME_TOTE
		GROUND,
		COLLECT_TOTE,
		COLLECT_UPRIGHT_CONTAINER,
		COLLECT_SIDEWAYS_CONTAINER,
		GRAB_TOTE,
		GRAB_SIDEWAYS_CONTAINER,
		HOME_TOTE,
		HOME_UPRIGHT_CONTAINER,
		HOME_SIDEWAYS_CONTAINER,
		COLLECT_ADDITIONAL,
		TOTE_1,
		TOTE_2,
		TOTE_3,
		TOTE_4,
		STEP,
		NONE
	}

	public ElevatorData() {
		super("ElevatorData");
		position = 0;
		ResetCommands();
	}
	
	public static ElevatorData get()
	{
		return (ElevatorData)ComponentData.GetComponentData("ElevatorData");
	}
	
	public void setDesiredSpeed(double d)
	{
		speed = d;
	}

	public double getSpeed(){
		return speed;
	}

	public ElevatorSetpoint getDesiredSetpoint(){
		return setpoint;
	}

	public void setSetpoint(ElevatorSetpoint s)
	{
		setpoint = s;
	}
	
	public ElevatorControlMode getControlMode() {
		return controlMode;
	}

	public void setControlMode(ElevatorControlMode control) {
		this.controlMode = control;
	}
	
	public boolean isAtSetpoint(ElevatorSetpoint s)
	{
		return currentSetpoint == s;
	}

	public void setCurrentPosition(ElevatorSetpoint s)
	{
		currentSetpoint = s;
	}
	
	public boolean isAtPosition(double isAtPosition)
	{
		return Math.abs(isAtPosition - currentPosition) < errorThreshold;
	}
	
	public void setCurrentPosition(int position)
	{
		currentPosition = position;
	}
	
	public int getCurrentPosition()
	{
		return currentPosition;
	}
	
	public int getDesiredPosition() {
		return position;
	}

	public void setDesiredPosition(int d) {
		this.position = d;
	}

	@Override
	protected void ResetCommands() {
		controlMode = ElevatorControlMode.VELOCITY;
		speed = 0.0f;
		setpoint = ElevatorSetpoint.HOME_TOTE;

	}

}
