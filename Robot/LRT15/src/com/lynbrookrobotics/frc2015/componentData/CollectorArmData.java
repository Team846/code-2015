package com.lynbrookrobotics.frc2015.componentData;

import com.lynbrookrobotics.frc2015.componentData.ComponentData;

public class CollectorArmData extends ComponentData
{
	public enum Position
	{
		STOWED,
		EXTEND
	}
	
	private Position desiredPosition;
	private Position currentPosition;
	
	public CollectorArmData()
	{
		super("CollectorArmData");
		desiredPosition = Position.STOWED;
		currentPosition = Position.STOWED;
	}
	
	public static CollectorArmData get()
	{
		return (CollectorArmData)ComponentData.GetComponentData("CollectorArmData");
	}
	
	public void setDesiredCollectorPosition(Position newPosition)
	{
		desiredPosition = newPosition;
	}
	
	public Position getDesiredCollectorPosition()
	{
		return desiredPosition;
	}
	
	public Position getCurrentCollectorPosition()
	{
		return currentPosition;
	}
	
	public void setCurrentCollectorPosition(Position p)
	{
		currentPosition = p;
	}
	
	@Override
	protected void ResetCommands() 
	{	}

}
