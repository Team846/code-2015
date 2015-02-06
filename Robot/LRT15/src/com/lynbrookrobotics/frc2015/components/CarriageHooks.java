package com.lynbrookrobotics.frc2015.components;

import com.lynbrookrobotics.frc2015.actuators.Pneumatics;
import com.lynbrookrobotics.frc2015.actuators.Pneumatics.State;
import com.lynbrookrobotics.frc2015.componentData.CarriageHooksData;
import com.lynbrookrobotics.frc2015.componentData.CarriageHooksData.Position;
import com.lynbrookrobotics.frc2015.config.ConfigPortMappings;
import com.lynbrookrobotics.frc2015.config.DriverStationConfig;

public class CarriageHooks extends Component{
	
	Pneumatics frontHooks;
	Pneumatics backHooks;
	
	CarriageHooksData hooksData;
	
	public CarriageHooks() 
	{
		super("CarriageHooks", DriverStationConfig.DigitalIns.NO_DS_DI);
		
		 frontHooks = new Pneumatics(
				ConfigPortMappings.Instance().Get("Pneumatics/FORWARD_HOOKS"), "ForwardHooks");
		 backHooks = new Pneumatics(
				ConfigPortMappings.Instance().Get("Pneumatics/BACK_HOOKS"), "BackHooks");
		
		 hooksData = CarriageHooksData.get();
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
		
		frontHooks.Set(state);
		backHooks.Set(state);
	}

	@Override
	protected void UpdateDisabled() {
		frontHooks.Set(State.FORWARD);
		backHooks.Set(State.FORWARD);
	}

	@Override
	protected void OnEnabled() {
		
	}

	@Override
	protected void OnDisabled() {
		
	}

}
