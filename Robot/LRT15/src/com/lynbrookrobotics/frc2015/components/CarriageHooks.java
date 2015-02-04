package com.lynbrookrobotics.frc2015.components;

import com.lynbrookrobotics.frc2015.actuators.Pneumatics;
import com.lynbrookrobotics.frc2015.actuators.Pneumatics.State;
import com.lynbrookrobotics.frc2015.componentData.CarriageHooksData;
import com.lynbrookrobotics.frc2015.componentData.CarriageHooksData.Position;
import com.lynbrookrobotics.frc2015.config.ConfigPortMappings;

public class CarriageHooks extends Component{

	Pneumatics frontHooks = new Pneumatics(
			ConfigPortMappings.Instance().Get("Pneumatics/FORWARD_HOOKS"), "CarriageHooks");
	Pneumatics backHooks = new Pneumatics(
			ConfigPortMappings.Instance().Get("Pneumatics/REVERSE_HOOKS"), "CarriageHooks");
	CarriageHooksData hooksData = CarriageHooksData.get();
	
	public CarriageHooks(String name, int driverStationDigitalIn) {
		super("CarriageHooks", driverStationDigitalIn);
	}

	@Override
	protected void UpdateEnabled() {
		State state;
		
		if (hooksData.getFrontHooksDesiredState() == Position.ENABLED)
			state = State.FORWARD;
		else
			state = State.FORWARD;
		
		
		if (hooksData.getBackHooksDesiredState() == Position.ENABLED)
			state = State.FORWARD;
		else
			state = State.FORWARD;
		
		frontHooks.Set(state, false);
		backHooks.Set(state, false);
	}

	@Override
	protected void UpdateDisabled() {
		frontHooks.Set(State.FORWARD, false);
		backHooks.Set(State.FORWARD, false);
	}

	@Override
	protected void OnEnabled() {
		
	}

	@Override
	protected void OnDisabled() {
		
	}

}
