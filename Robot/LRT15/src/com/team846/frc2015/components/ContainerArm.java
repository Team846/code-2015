package com.team846.frc2015.components;

import com.team846.frc2015.actuators.Pneumatics;
import com.team846.frc2015.actuators.Pneumatics.PneumaticState;
import com.team846.frc2015.componentData.ContainerArmData;
import com.team846.frc2015.config.ConfigPortMappings;
import com.team846.frc2015.config.DriverStationConfig;

public class ContainerArm extends Component{
	
	private final Pneumatics leftArm;
	private final Pneumatics rightArm;

	private final ContainerArmData armData;
	
	public ContainerArm() {
		
		armData = ContainerArmData.get();
		
		leftArm = new Pneumatics(
				ConfigPortMappings.Instance().get("Pneumatics/CONTAINER_ARM_LEFT"), "ContainerArmA");
		rightArm = new Pneumatics(
				ConfigPortMappings.Instance().get("Pneumatics/CONTAINER_ARM_RIGHT"), "ContainerArmB");
	}

	@Override
	protected void UpdateEnabled() 
	{
		leftArm.set(armData.GetLeftDeployed() ? Pneumatics.PneumaticState.FORWARD : Pneumatics.PneumaticState.OFF);
		rightArm.set(armData.GetRightDeployed() ? Pneumatics.PneumaticState.FORWARD : Pneumatics.PneumaticState.OFF);
	}

	@Override
	protected void UpdateDisabled() 
	{
		leftArm.set(PneumaticState.OFF);
		rightArm.set(PneumaticState.OFF);
	}

	@Override
	protected void OnEnabled() {
		
	}

	@Override
	protected void OnDisabled() {
		
	}

}
