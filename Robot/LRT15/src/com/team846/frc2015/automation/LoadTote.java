package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.CarriageHooksData;
import com.team846.frc2015.componentData.CollectorArmData;
import com.team846.frc2015.componentData.CollectorArmData.ArmPosition;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.componentData.CarriageHooksData.HookState;
import com.team846.frc2015.componentData.ElevatorData.ElevatorControlMode;
import com.team846.frc2015.componentData.ElevatorData.ElevatorSetpoint;

public class LoadTote extends Automation {

	private ElevatorData elevatorData;
	private CarriageHooksData hooksData;
	private boolean movingUp;
	private CollectorArmData armData;

	public LoadTote() {
		super("LoadElevator");
		movingUp = false;
		elevatorData = ElevatorData.get();
		hooksData = CarriageHooksData.get();
		armData = CollectorArmData.get();
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
		//moving carriage down, disengage hooks
		if(!movingUp)
		{
			hooksData.setBackHooksState(HookState.DISENGAGED);
			hooksData.setFrontHooksState(HookState.DISENGAGED);
					
			armData.setDesiredPosition(ArmPosition.STOWED);
			elevatorData.setControlMode(ElevatorControlMode.SETPOINT);
			elevatorData.setSetpoint(ElevatorSetpoint.GRAB_TOTE);
		}
		
		// going back up, engage hooks
		else if((elevatorData.isAtSetpoint(ElevatorSetpoint.GRAB_TOTE) && !movingUp) || (movingUp && !elevatorData.isAtSetpoint(ElevatorSetpoint.HOME)))
		{
			movingUp = true;
			hooksData.setBackHooksState(HookState.ENGAGED);
			hooksData.setFrontHooksState(HookState.ENGAGED);
			elevatorData.setSetpoint(ElevatorSetpoint.HOME);
	
		}
		
		else if(elevatorData.isAtSetpoint(ElevatorSetpoint.HOME))
			return true;
		
		return false;
	}

}
