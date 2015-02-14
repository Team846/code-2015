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
	
	private Pneumatics leftExtender;
	private Pneumatics rightExtender;

	private ManipulatorArmData armData;
	
	public ManipulatorArm() {
		super("ContainerHook", DriverStationConfig.DigitalIns.NO_DS_DI);
		
		armData = ManipulatorArmData.get();
		
		leftArm = new Pneumatics(
				ConfigPortMappings.Instance().get("Pneumatics/MANIPULATOR_LEFT"), "RakeA");
		rightArm = new Pneumatics(
				ConfigPortMappings.Instance().get("Pneumatics/MANIPULATOR_RIGHT"), "RakeB");
		
		leftExtender = new Pneumatics(
				ConfigPortMappings.Instance().get("Pneumatics/EXTENDER_LEFT"), "leftExtender");
		rightExtender = new Pneumatics(
				ConfigPortMappings.Instance().get("Pneumatics/EXTENDER_RIGHT"), "rightExtender");
	}

	@Override
	protected void UpdateEnabled() 
	{
		Pneumatics.State leftArmState;
		Pneumatics.State rightArmState;
		
		Pneumatics.State leftExtenderState;
		Pneumatics.State  rightExtenderState;
		
		//Arm
		if(armData.getDeployed(Arm.LEFT))
			leftArmState = State.FORWARD;
		else
			leftArmState = State.OFF;
		if(armData.getDeployed(Arm.RIGHT))
			rightArmState = State.FORWARD;
		else
			rightArmState = State.OFF;
		
		
		if(armData.getExtend(Arm.LEFT))
			leftExtenderState = State.FORWARD;
		else
			leftExtenderState = State.OFF;
		
		if(armData.getExtend(Arm.RIGHT))
			rightExtenderState = State.FORWARD;
		else
			rightExtenderState = State.OFF;
		
		leftArm.set(leftArmState);
		rightArm.set(rightArmState);	
		
		leftExtender.set(leftExtenderState);
		rightExtender.set(rightExtenderState);

	}

	@Override
	protected void UpdateDisabled() 
	{
		leftArm.set(State.OFF);
		rightArm.set(State.OFF);
		

		leftExtender.set(State.OFF);
		rightExtender.set(State.OFF);
	}

	@Override
	protected void OnEnabled() {
		
	}

	@Override
	protected void OnDisabled() {
		
	}

}
