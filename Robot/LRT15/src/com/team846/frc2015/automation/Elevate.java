
package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.componentData.ElevatorData.ElevatorControlMode;
import com.team846.frc2015.componentData.ElevatorData.Setpoint;
import com.team846.frc2015.log.AsyncPrinter;

public class Elevate extends Automation {

	ElevatorData elData;
	ElevatorData.Setpoint setpoint;
	public Elevate(int toteLevel) {
		super("Elevate");
		elData = ElevatorData.get();
		
		if(toteLevel == 1)
			setpoint = Setpoint.TOTE_1;
		else if (toteLevel == 2)
			setpoint = Setpoint.TOTE_2;
		else if (toteLevel == 3)
			setpoint = Setpoint.TOTE_3;
		else if (toteLevel == 4)
			setpoint = Setpoint.TOTE_4;
		else
		{
			AsyncPrinter.warn("Invalid tote level, defaulting to 1");
			setpoint = Setpoint.TOTE_1;
		}
				
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
		return false;
	}

	@Override
	protected boolean Run() {
		elData.setControlMode(ElevatorControlMode.SETPOINT);
		elData.setSetpoint(setpoint);
		
		return elData.isAtSetpoint(setpoint);
	}

}
