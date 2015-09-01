package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.CollectorArmData;
import com.team846.frc2015.componentData.CollectorArmData.ArmPosition;

public class MoveCollectorArm extends Automation {

	private final CollectorArmData armData;

	public MoveCollectorArm()
	{
		armData = CollectorArmData.get();
	}

	@Override
	protected void AllocateResources() {
		AllocateResource(ControlResource.COLLECTOR_ARMS);
	}

	protected boolean Start()
	{
		return true;
	}
	
	@Override
	protected boolean Abort()
	{
		return true;
	}
	
	@Override
	public boolean Run()
	{
		armData.setDesiredPosition(ArmPosition.EXTEND);
		return false;
	}
}
