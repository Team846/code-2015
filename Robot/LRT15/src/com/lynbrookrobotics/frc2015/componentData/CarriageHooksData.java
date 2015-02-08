package com.lynbrookrobotics.frc2015.componentData;

import com.lynbrookrobotics.frc2015.componentData.ComponentData;

public class CarriageHooksData extends ComponentData{

	public enum Position
	{
		ENABLED,
		DISABLED
	}
	
	private Position desiredFrontState;
	private Position desiredBackState;
	
	private Position currentFrontState;
	private Position currentBackState;
	
	public CarriageHooksData() {
		super("CarriageHooksData");
		desiredFrontState = Position.ENABLED;
		desiredBackState = Position.ENABLED;
		currentFrontState = Position.ENABLED;
		currentBackState = Position.ENABLED;
	}
	
	public static CarriageHooksData get(){
		return (CarriageHooksData) ComponentData.GetComponentData("CarriageHooksData");
	}
	
	public Position getFrontHooksCurrentState(){
		return currentFrontState;
	}
	
	public Position getFrontHooksDesiredState(){
		return desiredFrontState;
	}
	
	public void setFrontHooksState(Position newState){
		desiredFrontState = newState;
	}
	
	public Position getBackHooksCurrentState(){
		return currentBackState;
	}
	
	public Position getBackHooksDesiredState(){
		return desiredBackState;
	}

	public void setBackHooksState(Position newState){
		desiredBackState = newState;
	}
	
	public void setFrontHooksCurrentState(Position curState)
	{
		currentFrontState = curState;
	}
	
	public void setBackHooksCurrentState(Position curState)
	{
		currentFrontState = curState;
	}

	@Override
	protected void ResetCommands() {
		desiredFrontState = Position.ENABLED;
		desiredBackState = Position.ENABLED;
		currentFrontState = Position.ENABLED;
		currentBackState = Position.ENABLED;
		
	}

}
