package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.ContainerArmData;

//NOTE: should only be used in a paralllel routine
public class ContainerGrab extends Automation {

	private ContainerArmData armData;
	
	public ContainerGrab()
	{
		armData = ContainerArmData.get();
	}
	@Override
	protected void AllocateResources() {
		AllocateResource(ControlResource.CONTAINER_GRABBER);
	}

	@Override
	protected boolean Start() {
		return true;
	}

	@Override
	protected boolean Abort() {
		armData.SetLeftDeployed(false);
		armData.SetRightDeployed(false);
		return true;
	}

	@Override
	protected boolean Run() {
		armData.SetLeftDeployed(true);
		armData.SetRightDeployed(true);
		return false;
	}

}
