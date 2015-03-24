package com.team846.frc2015.components;

import com.team846.frc2015.actuators.Pneumatics;
import com.team846.frc2015.actuators.Pneumatics.State;
import com.team846.frc2015.componentData.ContainerArmData;
import com.team846.frc2015.config.ConfigPortMappings;
import com.team846.frc2015.config.DriverStationConfig;

public class ContainerArm extends Component{
	
	private final Pneumatics leftArm;
	private final Pneumatics rightArm;

	private final ContainerArmData armData;
	
	public ContainerArm() {
		super("ContainerArm", DriverStationConfig.DigitalIns.NO_DS_DI);
		
		armData = ContainerArmData.get();
		
		leftArm = new Pneumatics(
				ConfigPortMappings.Instance().get("Pneumatics/CONTAINER_ARM_LEFT"), "ContainerArmA");
		rightArm = new Pneumatics(
				ConfigPortMappings.Instance().get("Pneumatics/CONTAINER_ARM_RIGHT"), "ContainerArmB");
	}

	@Override
	protected void UpdateEnabled() 
	{
		leftArm.set(armData.GetLeftDeployed() ? Pneumatics.State.FORWARD : Pneumatics.State.OFF);
		rightArm.set(armData.GetRightDeployed() ? Pneumatics.State.FORWARD : Pneumatics.State.OFF);
	}

	@Override
	protected void UpdateDisabled() 
	{
		leftArm.set(State.OFF);
		rightArm.set(State.OFF);
	}

	@Override
	protected void OnEnabled() {
		
	}

	@Override
	protected void OnDisabled() {
		
	}

}
