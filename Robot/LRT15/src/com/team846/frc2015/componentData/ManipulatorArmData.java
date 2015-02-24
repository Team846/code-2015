package com.team846.frc2015.componentData;

import com.team846.frc2015.componentData.ManipulatorHookData.Arm;

public class ManipulatorArmData extends ComponentData {
	
	private boolean[] armStates;
	
	public enum Arm
	{
		LEFT,
		RIGHT
	}

	public ManipulatorArmData() {
		super("ManipulatorArmData");
		armStates = new boolean[]{false, false};
	}
	
	public static ManipulatorArmData get(){
		return (ManipulatorArmData) ComponentData.GetComponentData("ManipulatorArmData");
	}
	
	public boolean getDeployed(Arm arm)
	{
		return armStates[arm.ordinal()];
	}
	
	public void setDeployed(Arm arm, boolean state)
	{
		armStates[arm.ordinal()] = state;	
	}

	@Override
	protected void ResetCommands() 
	{
	}

}
