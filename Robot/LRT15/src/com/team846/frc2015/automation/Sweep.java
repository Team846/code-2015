package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.CollectorArmData;
import com.team846.frc2015.componentData.CollectorArmData.ArmPosition;
import com.team846.frc2015.componentData.CollectorRollersData.Direction;
import com.team846.frc2015.componentData.CollectorRollersData;

import edu.wpi.first.wpilibj.AnalogInput;

public class Sweep extends Automation {

	private CollectorArmData armData;
	private CollectorRollersData rollersData;
	
	enum Direction
	{
		LEFT,
		RIGHT
	}
	private Direction direction;

	public Sweep(Direction dir)
	{
		super("Sweep");
		direction = dir;
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
		if (direction == Direction.LEFT)
			rollersData.setDirection(CollectorRollersData.Direction.SWEEP_LEFT);
		else
			rollersData.setDirection(CollectorRollersData.Direction.SWEEP_RIGHT);
	
		return false;
	}
}
