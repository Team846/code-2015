package com.team846.frc2015.components;

import com.team846.frc2015.actuators.Pneumatics;
import com.team846.frc2015.actuators.Pneumatics.State;
import com.team846.frc2015.componentData.ComponentData;
import com.team846.frc2015.componentData.ManipulatorArmData;
import com.team846.frc2015.componentData.ManipulatorArmData.Arm;
import com.team846.frc2015.config.ConfigPortMappings;
import com.team846.frc2015.config.DriverStationConfig;

import edu.wpi.first.wpilibj.Solenoid;

public class ManipulatorArm extends Component{
	
	private Pneumatics leftArm;
	private Pneumatics rightArm;

	private ManipulatorArmData armData;
	
	public ManipulatorArm() {
		super("ContainerHook", DriverStationConfig.DigitalIns.NO_DS_DI);
		
		armData = ManipulatorArmData.get();
		
		leftArm = new Pneumatics(
				ConfigPortMappings.Instance().get("Pneumatics/MANIPULATOR_LEFT"), "RakeA");
		rightArm = new Pneumatics(
				ConfigPortMappings.Instance().get("Pneumatics/MANIPULATOR_RIGHT"), "RakeB");
	}

	@Override
	protected void UpdateEnabled() 
	{
		Pneumatics.State leftArmState; 
		Pneumatics.State rightArmState;
		
		leftArmState = armData.getDeployed(Arm.LEFT) ? State.FORWARD : State.OFF;
		rightArmState = armData.getDeployed(Arm.RIGHT) ? State.FORWARD : State.OFF;
		
		armData.setCurrentDeployed(Arm.LEFT, armData.getDeployed(Arm.LEFT));
		armData.setCurrentDeployed(Arm.RIGHT, armData.getDeployed(Arm.RIGHT));

		leftArm.set(leftArmState);
		rightArm.set(rightArmState);
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
