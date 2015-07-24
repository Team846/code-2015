
package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.componentData.ElevatorData.ElevatorControlMode;
import com.team846.frc2015.componentData.ElevatorData.ElevatorSetpoint;
import com.team846.frc2015.utils.AsyncPrinter;

public class Elevate extends Automation {

	private final ElevatorData elData;
	private final ElevatorSetpoint setpoint;
	public Elevate(int toteLevel) {
		elData = ElevatorData.get();
		
		if(toteLevel == 1)
			setpoint = ElevatorSetpoint.TOTE_1;
		else if (toteLevel == 2)
			setpoint = ElevatorSetpoint.TOTE_2;
		else if (toteLevel == 3)
			setpoint = ElevatorSetpoint.TOTE_3;
		else if (toteLevel == 4)
			setpoint = ElevatorSetpoint.TOTE_4;
		else if (toteLevel == 5)
			setpoint = ElevatorSetpoint.SWEEP_CONTAINER;
		else
		{
			AsyncPrinter.warn("Invalid tote level, defaulting to 1");
			setpoint = ElevatorSetpoint.TOTE_1;
		}
	}
	
	public Elevate(ElevatorSetpoint setpoint)
	{
		elData = ElevatorData.get();
		this.setpoint = setpoint;
	}

	@Override
	public void AllocateResources() 
	{
		AllocateResource(ControlResource.ELEVATOR);
	}

	@Override
	protected boolean Start() {
		return true;
	}

	@Override
	protected boolean Abort() {
		elData.setControlMode(ElevatorControlMode.VELOCITY);
		elData.setDesiredSpeed(0.0);
		return true;
	}

	@Override
	protected boolean Run() {
		elData.setControlMode(ElevatorControlMode.SETPOINT);
		elData.setSetpoint(setpoint);
		
		return elData.isAtSetpoint(setpoint);
	}

}
