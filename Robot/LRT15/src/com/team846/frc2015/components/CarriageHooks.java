package com.team846.frc2015.components;

import com.team846.frc2015.actuators.Pneumatics;
import com.team846.frc2015.actuators.Pneumatics.PneumaticState;
import com.team846.frc2015.componentData.CarriageHooksData;
import com.team846.frc2015.componentData.CarriageHooksData.HookState;
import com.team846.frc2015.oldconfig.ConfigPortMappings;

public class CarriageHooks extends Component{
	
	private final Pneumatics frontHooks;
	private final Pneumatics backHooks;
	
	private final CarriageHooksData hooksData;
	
	public CarriageHooks() 
	{
		
		 frontHooks = new Pneumatics(
				ConfigPortMappings.Instance().get("Pneumatics/FORWARD_HOOKS"), "ForwardHooks");
		 backHooks = new Pneumatics(
				ConfigPortMappings.Instance().get("Pneumatics/BACK_HOOKS"), "BackHooks");
		
		 hooksData = CarriageHooksData.get();
	}

	@Override
	protected void updateEnabled() {
		PneumaticState backState;
		PneumaticState frontState;
				
		frontState = hooksData.getFrontHooksDesiredState() == HookState.DOWN ? PneumaticState.OFF : PneumaticState.FORWARD;
		backState = hooksData.getBackHooksDesiredState() == HookState.DOWN ? PneumaticState.OFF : PneumaticState.FORWARD;
		
		frontHooks.set(frontState);
		backHooks.set(backState);
		
		hooksData.setFrontHooksCurrentState(frontState == PneumaticState.OFF ? HookState.DOWN : HookState.UP);
		hooksData.setBackHooksCurrentState(backState == PneumaticState.OFF ? HookState.DOWN : HookState.UP);
	}

	@Override
	protected void updateDisabled()
	{
		frontHooks.set(PneumaticState.OFF);
		backHooks.set(PneumaticState.OFF);
	}

	@Override
	protected void onEnabled() {}

	@Override
	protected void onDisabled() {}
}
