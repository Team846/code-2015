package com.team846.frc2015.componentData;

import java.util.Arrays;

public class ContainerArmData extends ComponentData {
	
	private boolean leftState = false;
	private boolean rightState = false;
	
	public ContainerArmData() {
		super("ContainerArmData");
	}

	public static ContainerArmData get()
	{
		return (ContainerArmData)ComponentData.GetComponentData("ContainerArmData");
	}
	
	public void SetLeftDeployed(boolean deploy)
	{
		leftState = deploy;
	}

	public void SetRightDeployed(boolean deploy)
	{
		rightState = deploy;
	}
	
	public boolean GetLeftDeployed()
	{
		return leftState;
	}
	
	public boolean GetRightDeployed()
	{
		return rightState;
	}

	@Override
	protected void ResetCommands() 
	{
		leftState = false;
		rightState = false;
	}

}
