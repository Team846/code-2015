package com.team846.frc2015.componentData;

import com.team846.frc2015.componentData.ComponentData;

public class CarriageHooksData extends ComponentData{

	public enum HookState
	{
		ENGAGED,
		DISENGAGED
	}
	
	private HookState desiredFrontState;
	private HookState desiredBackState;
	
	private HookState currentFrontState;
	private HookState currentBackState;
	
	public CarriageHooksData() {
		super("CarriageHooksData");
		desiredFrontState = HookState.ENGAGED;
		desiredBackState = HookState.ENGAGED;
		currentFrontState = HookState.ENGAGED;
		currentBackState = HookState.ENGAGED;
	}
	
	public static CarriageHooksData get(){
		return (CarriageHooksData) ComponentData.GetComponentData("CarriageHooksData");
	}
	
	public HookState getFrontHooksCurrentState(){
		return currentFrontState;
	}
	
	public HookState getFrontHooksDesiredState(){
		return desiredFrontState;
	}
	
	public void setFrontHooksState(HookState newState){
		desiredFrontState = newState;
	}
	
	public HookState getBackHooksCurrentState(){
		return currentBackState;
	}
	
	public HookState getBackHooksDesiredState(){
		return desiredBackState;
	}

	public void setBackHooksState(HookState newState){
		desiredBackState = newState;
	}
	
	public void setFrontHooksCurrentState(HookState curState)
	{
		currentFrontState = curState;
	}
	
	public void setBackHooksCurrentState(HookState curState)
	{
		currentFrontState = curState;
	}

	@Override
	protected void ResetCommands() {
		desiredFrontState = HookState.ENGAGED;
		desiredBackState = HookState.ENGAGED;
		currentFrontState = HookState.ENGAGED;
		currentBackState = HookState.ENGAGED;
		
	}

}
