package com.team846.frc2015.components;

import com.team846.frc2015.actuators.Pneumatics;
import com.team846.frc2015.actuators.Pneumatics.State;
import com.team846.frc2015.componentData.ManipulatorHookData;
import com.team846.frc2015.componentData.ManipulatorHookData.Arm;
import com.team846.frc2015.config.ConfigPortMappings;
import com.team846.frc2015.config.DriverStationConfig;

public class ManipulatorHooks extends Component
{
	Pneumatics hooks;
	
	ManipulatorHookData hookData;

	public ManipulatorHooks()
	{
		super("ManipulatorExtender", DriverStationConfig.DigitalIns.NO_DS_DI);
		hookData = ManipulatorHookData.get();
		
		hooks = new Pneumatics(
				ConfigPortMappings.Instance().get("Pneumatics/EXTENDER_LEFT"), "leftExtender");
	}

	@Override
	protected void UpdateEnabled() {
		
		Pneumatics.State hookState;
		
		if(hookData.getHold(Arm.LEFT))
			hookState = State.FORWARD;
		else
			hookState = State.OFF;
		
		
		hooks.set(hookState);

	}

	@Override
	protected void UpdateDisabled() {
		hooks.set(State.OFF);
	}

	@Override
	protected void OnEnabled() {

	}

	@Override
	protected void OnDisabled() {

	}

}
