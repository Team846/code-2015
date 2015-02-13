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
				ConfigPortMappings.Instance().get("Pneumatics/FORWARD_HOOKS"), "ForwardHooks");
		 backHooks = new Pneumatics(
				ConfigPortMappings.Instance().get("Pneumatics/BACK_HOOKS"), "BackHooks");
		
		 hooksData = CarriageHooksData.get();
	}

	@Override
	protected void UpdateEnabled() {
		State backState;
		State frontState;
		
		if (hooksData.getFrontHooksDesiredState() == Position.ENABLED)
			frontState = State.FORWARD;
		else
			frontState = State.OFF;
		
		
		if (hooksData.getBackHooksDesiredState() == Position.ENABLED)
			backState = State.FORWARD;
		else
			backState = State.OFF;
		
		frontHooks.set(frontState);
		backHooks.set(backState);
		
		hooksData.setFrontHooksCurrentState(frontState == State.FORWARD ? Position.ENABLED : Position.DISABLED);
		hooksData.setBackHooksCurrentState(backState == State.FORWARD ? Position.ENABLED : Position.DISABLED);
	}

	@Override
	protected void UpdateDisabled() {
		frontHooks.set(State.OFF);
		backHooks.set(State.OFF);
	}

	@Override
	protected void OnEnabled() {
		
	}

	@Override
	protected void OnDisabled() {
		
	}

}
