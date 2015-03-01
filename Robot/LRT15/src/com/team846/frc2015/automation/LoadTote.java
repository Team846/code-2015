package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.ElevatorData.ElevatorSetpoint;

public class LoadTote extends LoadItem {

	public LoadTote() {
		super("LoadTote", ElevatorSetpoint.COLLECT_TOTE, ElevatorSetpoint.GRAB_TOTE, ElevatorSetpoint.HOME_TOTE);
	}
}
