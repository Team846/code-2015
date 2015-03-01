package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.CarriageHooksData;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.componentData.CarriageHooksData.HookState;
import com.team846.frc2015.componentData.ElevatorData.ElevatorSetpoint;


public class LoadSidewaysContainer extends LoadItem {

	public LoadSidewaysContainer() {
		super("LoadSidewaysContainer", ElevatorData.ElevatorSetpoint.COLLECT_SIDEWAYS_CONTAINER,
				ElevatorData.ElevatorSetpoint.GRAB_SIDEWAYS_CONTAINER, ElevatorData.ElevatorSetpoint.HOME_SIDEWAYS_CONTAINER);
	}
}
