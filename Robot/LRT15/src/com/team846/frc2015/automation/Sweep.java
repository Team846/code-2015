package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.CollectorArmData;
import com.team846.frc2015.componentData.CollectorArmData.ArmPosition;
import com.team846.frc2015.componentData.CollectorRollersData;

public class Sweep extends Automation {

	private final CollectorArmData armData;
	private final CollectorRollersData rollersData;
	
	public enum Direction
	{
		LEFT,
		RIGHT
	}
	private final Direction direction;

	public Sweep(Direction dir)
	{
		direction = dir;
		armData = CollectorArmData.get();
		rollersData = CollectorRollersData.get();
	}

	@Override
	public void AllocateResources() {
		AllocateResource(ControlResource.COLLECTOR_ARMS);
		AllocateResource(ControlResource.COLLECTOR_ROLLERS);
	}

	@Override
	protected boolean Start() {
		return true;
	}

	@Override
	protected boolean Abort() {
		armData.setDesiredPosition(ArmPosition.STOWED);
		rollersData.setRunning(false);
		return true;
	}

	@Override
	protected boolean Run() {
		armData.setDesiredPosition(ArmPosition.EXTEND);
		rollersData.setRunning(true);
		rollersData.setSpeed(1.0);
		if (direction == Direction.LEFT)
			rollersData.setDirection(CollectorRollersData.Direction.SWEEP_LEFT);
		else
			rollersData.setDirection(CollectorRollersData.Direction.SWEEP_RIGHT);
	
		return false;
	}
}
