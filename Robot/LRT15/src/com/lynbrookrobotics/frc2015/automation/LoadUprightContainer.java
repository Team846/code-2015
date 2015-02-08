package com.lynbrookrobotics.frc2015.automation;

import com.lynbrookrobotics.frc2015.componentData.CarriageExtenderData;
import com.lynbrookrobotics.frc2015.componentData.CarriageHooksData;
import com.lynbrookrobotics.frc2015.componentData.CarriageHooksData.Position;

public class LoadUprightContainer extends Automation {
	
	CarriageHooksData hooksData;
	CarriageExtenderData extenderData;

	public LoadUprightContainer() {	
		super("LoadUprightContainer");
		hooksData = CarriageHooksData.get();
		extenderData = CarriageExtenderData.get();
	}
	

	@Override
	public void AllocateResources() {
		AllocateResource(ControlResource.CARRIAGE_EXTENDER);
		AllocateResource(ControlResource.CARRIAGE_HOOKS);
		AllocateResource(ControlResource.ELEVATOR);
	}

	@Override
	protected boolean Start() {
		hooksData.setBackHooksState(Position.ENABLED);
		return true;
	}

	@Override
	protected boolean Abort() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean Run() {
		
		return false;
	}

}
