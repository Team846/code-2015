package com.team846.frc2015.components;

import com.team846.frc2015.actuators.Pneumatics;
import com.team846.frc2015.actuators.Pneumatics.State;
import com.team846.frc2015.componentData.CarriageHooksData;
import com.team846.frc2015.componentData.CarriageHooksData.HookState;
import com.team846.frc2015.config.ConfigPortMappings;
import com.team846.frc2015.config.DriverStationConfig;

public class CarriageHooks extends Component{
	
	private final Pneumatics frontHooks;
	private final Pneumatics backHooks;
	
	private final CarriageHooksData hooksData;
	
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
		
		if (hooksData.getFrontHooksDesiredState() == HookState.DOWN)
			frontState = State.OFF;
		else
			frontState = State.FORWARD;
		
		
		if (hooksData.getBackHooksDesiredState() == HookState.DOWN)
			backState = State.OFF;
		else
			backState = State.FORWARD;
		
		frontHooks.set(frontState);
		backHooks.set(backState);
		
		hooksData.setFrontHooksCurrentState(frontState == State.OFF ? HookState.DOWN : HookState.UP);
		hooksData.setBackHooksCurrentState(backState == State.OFF ? HookState.DOWN : HookState.UP);
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
