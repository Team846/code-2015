package com.lynbrookrobotics.frc2015.componentData;

import com.lynbrookrobotics.frc2015.componentData.ComponentData;

public class CollectorRollersData extends ComponentData
{
	private double speed;
	private boolean running;
	
	private Direction direction;
	
	public enum Direction
	{
		FORWARD,
		REVERSE
	}
	
	public static CollectorRollersData get()
	{
		return (CollectorRollersData) ComponentData.GetComponentData("CollectorRollersData");
	}

	public CollectorRollersData()
	{
		super("CollectorRollersData");
		running = false;
		speed = 0.0;
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
		running = false;
		speed = 0.0;

	}
}
