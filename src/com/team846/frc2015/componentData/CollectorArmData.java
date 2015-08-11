package com.team846.frc2015.componentData;

import com.team846.frc2015.componentData.ComponentData;

public class CollectorArmData extends ComponentData
{
	public enum ArmPosition
	{
		STOWED,
		EXTEND
	}
	
	private ArmPosition desiredPosition;
	private ArmPosition currentPosition;
	
	public CollectorArmData()
	{
		super("CollectorArmData");
		desiredPosition = ArmPosition.STOWED;
		currentPosition = ArmPosition.STOWED;
	}
	
	public static CollectorArmData get()
	{
		return (CollectorArmData)ComponentData.GetComponentData("CollectorArmData");
	}
	
	public void setDesiredPosition(ArmPosition newPosition)
	{
		desiredPosition = newPosition;
	}
	
	public ArmPosition getDesiredCollectorPosition()
	{
		return desiredPosition;
	}
	
	public ArmPosition getCurrentPosition()
	{
		return currentPosition;
	}
	
	public void setCurrentCollectorPosition(ArmPosition p)
	{
		currentPosition = p;
	}
	
	@Override
	protected void ResetCommands() 
	{
		desiredPosition = ArmPosition.STOWED;
	}

}
