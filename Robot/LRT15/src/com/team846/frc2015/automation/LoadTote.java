package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.CarriageHooksData;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.componentData.CarriageHooksData.HookState;
import com.team846.frc2015.componentData.ElevatorData.ElevatorControlMode;
import com.team846.frc2015.componentData.ElevatorData.Setpoint;

public class LoadTote extends Automation {

	private ElevatorData elevatorData;
	private CarriageHooksData hooksData;
	private boolean movingUp;

	public LoadTote() {
		super("LoadElevator");
		movingUp = false;
		elevatorData = ElevatorData.get();
		hooksData = CarriageHooksData.get();
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
		//turn off hooks
		hooksData.setBackHooksState(HookState.DISENGAGED);
		hooksData.setFrontHooksState(HookState.DISENGAGED);
		
		// bring carriage down to tote grab setpoint
		return true;
	}

	@Override
	protected boolean Abort() {
		elevatorData.setControlMode(ElevatorControlMode.SPEED);
		elevatorData.setSpeed(0.0);
		return true;
	}

	@Override
	protected boolean Run() {
		elevatorData.setControlMode(ElevatorControlMode.SETPOINT);
		elevatorData.setSetpoint(Setpoint.GRAB_TOTE);
		
		if(elevatorData.isAtSetpoint(Setpoint.GRAB_TOTE) && !movingUp)
		{
			movingUp = true;
			hooksData.setBackHooksState(HookState.ENGAGED);
			hooksData.setFrontHooksState(HookState.ENGAGED);
			elevatorData.setSetpoint(Setpoint.HOME);
			
		}
		
		if(elevatorData.isAtSetpoint(Setpoint.HOME))
			return true;
		
		return false;
	}

}
