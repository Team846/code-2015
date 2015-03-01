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
		SPEED,
		SETPOINT
	}
	
	public enum ElevatorSetpoint
	{
		GROUND,
		TOTE_1,
		TOTE_2,
		TOTE_3,
		TOTE_4,
		GRAB_TOTE,
		HOME,
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
		controlMode = ElevatorControlMode.SPEED;
		speed = 0.0f;
		setpoint = ElevatorSetpoint.GROUND;

	}

}
