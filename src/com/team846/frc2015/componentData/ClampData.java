package com.team846.frc2015.componentData;

public class ClampData extends ComponentData {
	
	private ClampState desiredState;
	private ClampState currentState;

	public enum ClampState
	{
		UP,
		DOWN
	}
	public ClampData() {
		super("ClampData");
		desiredState = currentState = ClampState.UP;
	}
	
	public static ClampData get()
	{
		return (ClampData)ComponentData.GetComponentData("ClampData");
	}
	
	public ClampState getDesiredState()
	{
		return desiredState;
	}
	
	public void setDesiredState(ClampState state)
	{
		desiredState = state;
	}
	
	public ClampState getCurrentState()
	{
		return currentState;
	}
	
	public void setCurrentState(ClampState state)
	{
		 currentState = state;
	}

	@Override
	protected void ResetCommands() {
		desiredState = ClampState.UP;

	}

}
