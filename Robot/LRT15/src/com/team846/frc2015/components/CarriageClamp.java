package com.team846.frc2015.components;

import com.team846.frc2015.actuators.Pneumatics;
import com.team846.frc2015.actuators.Pneumatics.PneumaticState;
import com.team846.frc2015.componentData.ClampData;
import com.team846.frc2015.componentData.ClampData.ClampState;
import com.team846.frc2015.config.ConfigPortMappings;
import com.team846.frc2015.config.DriverStationConfig;

public class CarriageClamp extends Component {
	
	private final Pneumatics clamp;
	private final ClampData clampData;

	public CarriageClamp()
	{
		clampData = ClampData.get();
		clamp = new Pneumatics(ConfigPortMappings.Instance().get("Pneumatics/CARRIAGE_CLAMP"),
				"CarriageClamp");
	}

	@Override
	protected void UpdateEnabled() {
		if(clampData.getDesiredState() == ClampState.UP)
			clamp.set(PneumaticState.FORWARD);
		else
			clamp.set(PneumaticState.OFF);
		clampData.setCurrentState(clampData.getDesiredState());
	}

	@Override
	protected void UpdateDisabled() {
		clamp.set(PneumaticState.OFF);
		}

	@Override
	protected void OnEnabled() {}

	@Override
	protected void OnDisabled() {}
}
