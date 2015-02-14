package com.team846.frc2015.componentData;

import com.team846.frc2015.componentData.ManipulatorHookData.Arm;

public class ManipulatorArmData extends ComponentData {
	
	private boolean[] armStates;
	private boolean[] armExtendStates;
	
	public enum Arm
	{
		LEFT,
		RIGHT
	}

	public ManipulatorArmData() {
		super("RakeHookData");
		armStates = new boolean[]{false, false};
		armExtendStates = new boolean[]{false, false};
	}
	
	public static ManipulatorArmData get(){
		return (ManipulatorArmData) ComponentData.GetComponentData("RakeExtenderData");
	}
	
	public boolean getDeployed(Arm arm)
	{
		return armStates[arm.ordinal()];
	}
	
	public void setDeployed(Arm arm, boolean state)
	{
		armStates[arm.ordinal()] = state;	
	}
	
	public boolean getExtend(Arm arm)
	{
		return armExtendStates[arm.ordinal()];
	}
	
	public void setExtend(Arm arm, boolean state)
	{
		armExtendStates[arm.ordinal()] = state;	
	}

	@Override
	protected void ResetCommands() 
	{
	}

}
