package com.team846.frc2015.componentData;

import java.util.Arrays;

import com.team846.frc2015.componentData.ManipulatorHookData.Arm;

public class ManipulatorArmData extends ComponentData {
	
	private boolean[] armStates;
	private boolean[] currentArmStates;
	
	public enum Arm
	{
		LEFT,
		RIGHT
	}

	public ManipulatorArmData() {
		super("ManipulatorArmData");
		armStates = new boolean[2];
		currentArmStates = new boolean[2];
		
		Arrays.fill(armStates, false);
		Arrays.fill(currentArmStates, false);
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
	
	public void setCurrentDeployed(Arm arm, boolean state)
	{
		currentArmStates[arm.ordinal()] = state;
	}
	
	public boolean getCurrentDeployed(Arm arm)
	{
		return currentArmStates[arm.ordinal()];
	}

	@Override
	protected void ResetCommands() 
	{
	}

}
