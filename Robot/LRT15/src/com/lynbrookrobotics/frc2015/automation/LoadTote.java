package com.lynbrookrobotics.frc2015.automation;

public class LoadTote extends Automation {

	public LoadTote() {
		super("LoadElevator");
	}

	@Override
	public void AllocateResources() {
		AllocateResource(ControlResource.COLLECTOR_ARMS);
		AllocateResource(ControlResource.COLLECTOR_ROLLERS);
		AllocateResource(ControlResource.CARRIAGE_EXTENDER);
		AllocateResource(ControlResource.CARRIAGE_HOOKS);
		AllocateResource(ControlResource.ELEVATOR);
	}

	@Override
	protected boolean Start() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected boolean Abort() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean Run() {
		// TODO Auto-generated method stub
		return false;
	}

}
