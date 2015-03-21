package com.team846.frc2015.componentData;


public class ManipulatorHookData extends ComponentData{

	private boolean[] armStates;
	
	public enum Arm
	{
		LEFT,
		RIGHT
	}
	
	public ManipulatorHookData(){
		super("ContainerHookData");
		armStates = new boolean[]{false,false};
	}
	
	public static ManipulatorHookData get()
	{
		return (ManipulatorHookData) ComponentData.GetComponentData("ContainerHookData");
	}
	
	public boolean getHold(Arm arm)
	{
		return armStates[arm.ordinal()];	
	}
	
	public void setHold(Arm arm, boolean on)
	{
		armStates[arm.ordinal()] = on;	
	}
	
	@Override
	protected void ResetCommands() 
	{ 
	}
}
