package com.lynbrookrobotics.frc2015.components;

import com.lynbrookrobotics.frc2015.actuators.Pneumatics;
import com.lynbrookrobotics.frc2015.actuators.Pneumatics.State;
import com.lynbrookrobotics.frc2015.componentData.ComponentData;
import com.lynbrookrobotics.frc2015.componentData.ManipulatorArmData;
import com.lynbrookrobotics.frc2015.componentData.ManipulatorArmData.Arm;
import com.lynbrookrobotics.frc2015.componentData.ManipulatorArmData.ArmState;
import com.lynbrookrobotics.frc2015.config.ConfigPortMappings;
import com.lynbrookrobotics.frc2015.config.DriverStationConfig;

import edu.wpi.first.wpilibj.Solenoid;

public class ManipulatorArm extends Component{
	
	private Pneumatics leftArm = null;
	private Pneumatics rightArm = null;

	private ManipulatorArmData armData;
	
	public ManipulatorArm() {
		super("ContainerHook", DriverStationConfig.DigitalIns.NO_DS_DI);
		
		armData = ManipulatorArmData.get();
		
		leftArm = new Pneumatics(
				ConfigPortMappings.Instance().Get("Pneumatics/MANIPULATOR_LEFT"), "RakeA");
		rightArm = new Pneumatics(
				ConfigPortMappings.Instance().Get("Pneumatics/MANIPULATOR_RIGHT"), "RakeB");
	}

	@Override
	protected void UpdateEnabled() 
	{
		Pneumatics.State leftArmState;
		Pneumatics.State rightArmState;
		
		if(armData.getDesiredArmState(Arm.LEFT) == ArmState.DEPLOYED)
			leftArmState = State.FORWARD;
		else
			leftArmState = State.REVERSE;
		if(armData.getDesiredArmState(Arm.RIGHT) == ArmState.DEPLOYED)
			rightArmState = State.FORWARD;
		else
			rightArmState = State.REVERSE;
		
		leftArm.Set(leftArmState);
		rightArm.Set(rightArmState);	

	}

	@Override
	protected void UpdateDisabled() 
	{
		leftArm.Set(State.OFF);
		rightArm.Set(State.OFF);
	}

	@Override
	protected void OnEnabled() {
		
	}

	@Override
	protected void OnDisabled() {
		
	}

}
