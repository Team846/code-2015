package com.lynbrookrobotics.frc2015.components;

import com.lynbrookrobotics.frc2015.actuators.Pneumatics;
import com.lynbrookrobotics.frc2015.actuators.Pneumatics.State;
import com.lynbrookrobotics.frc2015.componentData.ManipulatorExtenderData;
import com.lynbrookrobotics.frc2015.componentData.ManipulatorExtenderData.Arm;
import com.lynbrookrobotics.frc2015.componentData.ManipulatorExtenderData.ArmState;
import com.lynbrookrobotics.frc2015.config.ConfigPortMappings;
import com.lynbrookrobotics.frc2015.config.DriverStationConfig;

public class ManipulatorExtender extends Component
{
	Pneumatics leftExtender = null;
	Pneumatics rightExtender = null;
	
	ManipulatorExtenderData extenderData  = null;

	public ManipulatorExtender()
	{
		super("ManipulatorExtender", DriverStationConfig.DigitalIns.NO_DS_DI);
		extenderData = ManipulatorExtenderData.get();
		
		leftExtender = new Pneumatics(
				ConfigPortMappings.Instance().Get("Pneumatics/EXTENDER_LEFT"), "leftExtender");
		rightExtender = new Pneumatics(
				ConfigPortMappings.Instance().Get("Pneumatics/EXTENDER_RIGHT"), "rightExtender");
	}

	@Override
	protected void UpdateEnabled() {
		
		Pneumatics.State leftState;
		Pneumatics.State  rightState;
		if(extenderData.getExtenderState(Arm.LEFT) == ArmState.EXTEND)
			leftState = State.FORWARD;
		else
			leftState = State.REVERSE;
		
		if(extenderData.getExtenderState(Arm.RIGHT) == ArmState.EXTEND)
			rightState = State.FORWARD;
		else
			rightState = State.REVERSE;
		
		leftExtender.Set(leftState);
		rightExtender.Set(rightState);

	}

	@Override
	protected void UpdateDisabled() {
		leftExtender.Set(State.REVERSE);
		rightExtender.Set(State.REVERSE);
	}

	@Override
	protected void OnEnabled() {

	}

	@Override
	protected void OnDisabled() {

	}

}
