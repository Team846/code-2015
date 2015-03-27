package com.team846.frc2015.components;

import com.team846.frc2015.actuators.Pneumatics;
import com.team846.frc2015.actuators.Pneumatics.State;
import com.team846.frc2015.componentData.ClampData;
import com.team846.frc2015.componentData.ClampData.ClampState;
import com.team846.frc2015.config.ConfigPortMappings;
import com.team846.frc2015.config.DriverStationConfig;

public class CarriageClamp extends Component {
	
	private final Pneumatics clamp;
	private final ClampData clampData;

	public CarriageClamp() {
		super("CarriageClamp", DriverStationConfig.DigitalIns.NO_DS_DI);
		clampData = ClampData.get();
		clamp = new Pneumatics(ConfigPortMappings.Instance().get("Pneumatics/CARRIAGE_CLAMP"),
				"CarriageClamp");
	}

	@Override
	protected void UpdateEnabled() {
		if(clampData.getDesiredState() == ClampState.UP)
			clamp.set(State.FORWARD);
		else
			clamp.set(State.OFF);
		
		clampData.setCurrentState(clampData.getDesiredState());

		
	}

	@Override
	protected void UpdateDisabled() {
		clamp.set(State.OFF);
		
	}

	@Override
	protected void OnEnabled() {
		
	}

	@Override
	protected void OnDisabled() {
		
	}
	
	

}
