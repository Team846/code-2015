package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.CarriageHooksData;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.componentData.CarriageHooksData.HookState;
import com.team846.frc2015.componentData.ElevatorData.ElevatorSetpoint;


public class LoadContainer extends Automation {

	private CarriageHooksData hooksData;
	private ElevatorData elevatorData;

	public LoadContainer() {
		super("LoadContainer");
		hooksData = CarriageHooksData.get();
		elevatorData = ElevatorData.get();
	}

	@Override
	public void AllocateResources() {
		AllocateResource(ControlResource.CARRIAGE_HOOKS);
		AllocateResource(ControlResource.CARRIAGE_HOOKS);
	}

	@Override
	protected boolean Start() {
		return true;
	}

	@Override
	protected boolean Abort() {
		return true;
	}

	@Override
	protected boolean Run() {
		hooksData.setBackHooksState(HookState.ENGAGED);
		elevatorData.setSetpoint(ElevatorSetpoint.HOME);
		return elevatorData.isAtSetpoint(ElevatorSetpoint.HOME);
	}

}
