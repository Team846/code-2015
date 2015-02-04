package com.lynbrookrobotics.frc2015.componentData;

import java.util.ArrayList;

import com.lynbrookrobotics.frc2015.actuators.Pneumatics;
import com.lynbrookrobotics.frc2015.actuators.Pneumatics.State;
import com.lynbrookrobotics.frc2015.utils.Pair;


public class ManipulatorArmData extends ComponentData{

	private ArmState[] armStates;
	
	public enum ArmState
	{
		UNDEPLOYED,
		DEPLOYED
	}
	
	public enum Arm
	{
		LEFT,
		RIGHT
	}
	
	public ManipulatorArmData(){
		super("ContainerHook");
		armStates = new ArmState[2];
		armStates[0] = ArmState.UNDEPLOYED;
		armStates[1] = ArmState.UNDEPLOYED;
		ResetCommands();
	}
	
	public static ManipulatorArmData get()
	{
		return (ManipulatorArmData) ComponentData.GetComponentData("ContainerHook");
	}

	@Override
	protected void ResetCommands() 
	{ 
		
	}
	
	public ArmState getDesiredArmState(Arm arm)
	{
		return armStates[arm.ordinal()];
		
	}
	
	public void setDesiredArmState(Arm arm, ArmState state)
	{
		armStates[arm.ordinal()] = state;	
	}
	
}
