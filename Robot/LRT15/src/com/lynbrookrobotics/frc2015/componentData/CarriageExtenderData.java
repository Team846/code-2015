package com.lynbrookrobotics.frc2015.componentData;

public class CarriageExtenderData extends ComponentData 
{
	private ControlMode control;
	private Setpoint setpoint;
	
	private double maxSpeed;
	private double speed;
	
	private float desiredCarriagePosition;
	private float currentPosition;
	
	public enum ControlMode
	{
		AUTOMATED,
		MANUAL_VELOCITY,
		MANUAL_POSITION
	}
	
	public enum Setpoint
	{
		RETRACT, 
		EXTEND
	}
	
	public CarriageExtenderData() {
		super("CarriageExtenderData");
		maxSpeed = 1.0;
		ResetCommands();
	}
	
	public static CarriageExtenderData get()
	{
		return (CarriageExtenderData)ComponentData.GetComponentData("CarriageExtenderData");
	}
	
	public void setMaxSpeed(double speed)
	{
		maxSpeed = speed;
	}
	
	public double getMaxSpeed()
	{
		return maxSpeed;
	}
	
	public void setSpeed(double speed)
	{
		this.speed = speed * maxSpeed;
	}
	
	public double getSpeed()
	{
		return speed;
	}
	
	public ControlMode getControlMode()
	{
		return control;
	}
	
	public void setControlMode(ControlMode controlMode)
	{
		control = controlMode;
	}
	
	public void setAutomatedSetpoint(Setpoint set)
	{
		setpoint = set;
	}
	
	public Setpoint getAutomatedSetpoint()
	{
		return setpoint;
	}

	public void setDesiredPositionSetpoint(float carriagePosition)
	{
		this.desiredCarriagePosition = carriagePosition;	
	}
	
	public float getDesiredPositionSetpoint()
	{
		return desiredCarriagePosition;
	}
	
	public float getCurrentPosition()
	{
		return currentPosition;
	}
	
	public void setCurrentPosition(float pos)
	{
		currentPosition = pos;
	}
	
	@Override
	protected void ResetCommands() 
	{
		speed = 0.0;
		control = ControlMode.MANUAL_VELOCITY;
		setpoint = Setpoint.RETRACT;
	}

}
