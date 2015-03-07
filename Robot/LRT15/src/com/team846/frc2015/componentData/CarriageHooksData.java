package com.team846.frc2015.componentData;

import com.team846.frc2015.componentData.ComponentData;
import com.team846.frc2015.utils.AsyncPrinter;

public class CarriageHooksData extends ComponentData{

	public enum HookState
	{
		DOWN,
		UP
	}
	
	private HookState desiredFrontState;
	private HookState desiredBackState;
	
	private HookState currentFrontState;
	private HookState currentBackState;
	
	public CarriageHooksData() {
		super("CarriageHooksData");
		desiredFrontState = HookState.DOWN;
		desiredBackState = HookState.DOWN;
		currentFrontState = HookState.DOWN;
		currentBackState = HookState.DOWN;
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
	
	public void setFrontHooksDesiredState(HookState newState){
		desiredFrontState = newState;
	}
	
	public HookState getBackHooksCurrentState(){
		return currentBackState;
	}
	
	public HookState getBackHooksDesiredState(){
		return desiredBackState;
	}

	public void setBackHooksDesiredState(HookState newState){
		desiredBackState = newState;
	}
	
	public void setFrontHooksCurrentState(HookState curState)
	{
		currentFrontState = curState;
	}
	
	public void setBackHooksCurrentState(HookState curState)
	{
		currentBackState = curState;
	}

	@Override
	protected void ResetCommands() {
		desiredFrontState = HookState.DOWN;
		desiredBackState = HookState.DOWN;
		currentFrontState = HookState.DOWN;
		currentBackState = HookState.DOWN;
		
	}

}
