
package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.componentData.ElevatorData.ElevatorControlMode;
import com.team846.frc2015.componentData.ElevatorData.ElevatorSetpoint;
import com.team846.frc2015.log.AsyncPrinter;

public class Elevate extends Automation {

	ElevatorData elData;
	ElevatorSetpoint setpoint;
	public Elevate(int toteLevel) {
		super("Elevate");
		elData = ElevatorData.get();
		
		if(toteLevel == 1)
			setpoint = ElevatorSetpoint.TOTE_1;
		else if (toteLevel == 2)
			setpoint = ElevatorSetpoint.TOTE_2;
		else if (toteLevel == 3)
			setpoint = ElevatorSetpoint.TOTE_3;
		else if (toteLevel == 4)
			setpoint = ElevatorSetpoint.TOTE_4;
		else
		{
			AsyncPrinter.warn("Invalid tote level, defaulting to 1");
			setpoint = ElevatorSetpoint.TOTE_1;
		}
				
	}

	@Override
	public void AllocateResources() 
	{
		AllocateResource(ControlResource.ELEVATOR);
	}

	@Override
	protected boolean Start() {
		AsyncPrinter.info("srinfeg");
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
		AsyncPrinter.info("nutmeg");
		elData.setControlMode(ElevatorControlMode.SETPOINT);
		elData.setSetpoint(setpoint);
		
		return elData.isAtSetpoint(setpoint);
	}

}
