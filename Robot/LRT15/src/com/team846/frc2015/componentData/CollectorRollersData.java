package com.team846.frc2015.componentData;

import com.team846.frc2015.componentData.ComponentData;

public class CollectorRollersData extends ComponentData
{
	private double speed;
	private boolean running;
	
	private Direction direction;
	
	public enum Direction
	{
		INTAKE,
		REVERSE,
		SWEEP_LEFT,
		SWEEP_RIGHT
	}
	
	public static CollectorRollersData get()
	{
		return (CollectorRollersData) ComponentData.GetComponentData("CollectorRollersData");
	}

	public CollectorRollersData()
	{
		super("CollectorRollersData");
		ResetCommands();
	}

	public double getSpeed()
	{
		return speed;
	}

	public void setSpeed(double desiredSpeed)
	{
		speed = desiredSpeed;
	}

	public boolean isRunning()
	{
		return running;
	}

	public void setRunning(boolean desiredRunningState)
	{
		running = desiredRunningState;
	}
	
	public void setDirection(Direction d)
	{
		direction = d;
	}
	
	public Direction getDirection()
	{
		return direction;
	}
	
	@Override
	protected void ResetCommands()
	{
		direction = Direction.INTAKE;
		running = false;
		speed = 0.0;

	}
}
