
package com.lynbrookrobotics.frc2015.automation;

import com.lynbrookrobotics.frc2015.componentData.ElevatorData;
import com.lynbrookrobotics.frc2015.componentData.ElevatorData.ControlMode;
import com.lynbrookrobotics.frc2015.componentData.ElevatorData.Setpoint;
import com.lynbrookrobotics.frc2015.log.AsyncPrinter;

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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean Run() {
		elData.setControlMode(ControlMode.SETPOINT);
		elData.setSetpoint(setpoint);
		
		return elData.isAtSetpoint(setpoint);
	}

}
