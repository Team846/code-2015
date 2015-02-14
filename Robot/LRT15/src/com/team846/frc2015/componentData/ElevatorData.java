package com.team846.frc2015.componentData;

import edu.wpi.first.wpilibj.Ultrasonic;

public class ElevatorData extends ComponentData{
	
	private double speed;
	private double position;

	private ControlMode controlMode;

	private Setpoint setpoint;
	private Setpoint currentSetpoint;
	
	private double errorThreshold  = 0.1f;
	private double currentPosition = 0.0f;
	
	public enum ControlMode
	{
		POSITION,
		SPEED,
		SETPOINT
	}
	
	public enum Setpoint
	{
		GROUND,
		TOTE_1,
		TOTE_2,
		TOTE_3,
		TOTE_4,
		GRAB_TOTE,
		HOME
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
	
	public void setSpeed(double d)
	{
		speed = d;
	}

	public double getSpeed(){
		return speed;
	}

	public Setpoint getDesiredSetpoint(){
		return setpoint;
	}

	public void setSetpoint(Setpoint s)
	{
		setpoint = s;
	}
	
	public ControlMode getControlMode() {
		return controlMode;
	}

	public void setControlMode(ControlMode control) {
		this.controlMode = control;
	}
	
	public boolean isAtSetpoint(Setpoint s)
	{
		return currentSetpoint == s;
	}

	public void setCurrentPosition(Setpoint s)
	{
		currentSetpoint = s;
	}
	
	public boolean isAtPosition(double isAtPosition)
	{
		return Math.abs(isAtPosition - currentPosition) < errorThreshold;
	}
	
	public void setCurrentPosition(double position)
	{
		currentPosition = position;
	}
	
	public double getDesiredPosition() {
		return position;
	}

	public void setDesiredPosition(double d) {
		this.position = d;
	}

	@Override
	protected void ResetCommands() {
		controlMode = ControlMode.SPEED;
		speed = 0.0f;
		setpoint = Setpoint.GROUND;

	}

}
