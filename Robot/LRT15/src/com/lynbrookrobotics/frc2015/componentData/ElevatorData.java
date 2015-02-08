package com.lynbrookrobotics.frc2015.componentData;

import edu.wpi.first.wpilibj.Ultrasonic;

public class ElevatorData extends ComponentData{
	
	private float speed;
	private float position;

	private ControlMode controlMode;

	private Setpoint setpoint;
	private Setpoint currentSetpoint;
	
	public enum ControlMode
	{
		MANUAL_POSITION,
		MANUAL_VELOCITY,
		SETPOINT
	}
	
	public enum Setpoint
	{
		GROUND,
		TOTE_1,
		TOTE_2,
		TOTE_3,
		TOTE_4
	}

	public ElevatorData() {
		super("ElevatorData");
		ResetCommands();
	}
	
	public static ElevatorData get()
	{
		return (ElevatorData)ComponentData.GetComponentData("ElevatorData");
	}
	
	public void setSpeed(float desiredSpeed)
	{
		speed = desiredSpeed;
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
	
	public float getPosition() {
		return position;
	}

	public void setPosition(float position) {
		this.position = position;
	}

	@Override
	protected void ResetCommands() {
		controlMode = ControlMode.MANUAL_POSITION;
		speed = 0.0f;
		setpoint = Setpoint.GROUND;

	}

}
