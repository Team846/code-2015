package com.lynbrookrobotics.frc2015.automation;

import com.lynbrookrobotics.frc2015.componentData.CarriageHooksData;
import com.lynbrookrobotics.frc2015.componentData.ElevatorData;
import com.lynbrookrobotics.frc2015.componentData.CarriageHooksData.Position;
import com.lynbrookrobotics.frc2015.componentData.ElevatorData.ControlMode;
import com.lynbrookrobotics.frc2015.componentData.ElevatorData.Setpoint;

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
		hooksData.setBackHooksState(Position.DISABLED);
		hooksData.setFrontHooksState(Position.DISABLED);
		
		// bring carriage down to tote grab setpoint
		return true;
	}

	@Override
	protected boolean Abort() {
		elevatorData.setControlMode(ControlMode.SPEED);
		elevatorData.setSpeed(0.0);
		return true;
	}

	@Override
	protected boolean Run() {
		elevatorData.setControlMode(ControlMode.SETPOINT);
		elevatorData.setSetpoint(Setpoint.GRAB_TOTE);
		
		if(elevatorData.isAtSetpoint(Setpoint.GRAB_TOTE) && !movingUp)
		{
			movingUp = true;
			hooksData.setBackHooksState(Position.ENABLED);
			hooksData.setFrontHooksState(Position.ENABLED);
			elevatorData.setSetpoint(Setpoint.HOME);
			
		}
		
		if(elevatorData.isAtSetpoint(Setpoint.HOME))
			return true;
		
		return false;
	}

}
