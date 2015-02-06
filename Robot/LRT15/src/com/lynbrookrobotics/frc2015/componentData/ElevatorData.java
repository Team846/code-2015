package com.lynbrookrobotics.frc2015.componentData;

import edu.wpi.first.wpilibj.Ultrasonic;

public class ElevatorData extends ComponentData{
	
	private float speed;
	private ControlMode controlMode;

	private Setpoint setpoint;
	
	public enum ControlMode
	{
		MANUAL,
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


	@Override
	protected void ResetCommands() {
		controlMode = ControlMode.MANUAL;
		speed = 0.0f;
		setpoint = Setpoint.GROUND;

	}

}
