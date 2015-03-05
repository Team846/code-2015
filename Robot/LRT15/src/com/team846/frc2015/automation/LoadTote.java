package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.ElevatorData.ElevatorSetpoint;
import com.team846.frc2015.config.Configurable;

public class LoadTote extends LoadItem implements Configurable {
	
	private int toteAnalogValue = 0;

	public LoadTote() {
		super("LoadTote", ElevatorSetpoint.COLLECT_TOTE, ElevatorSetpoint.GRAB_TOTE, ElevatorSetpoint.HOME_TOTE);
	}

	@Override
	public void Configure() {
		toteAnalogValue = GetConfig("analog_tote_value", 40);
		super.setAnalogThreshold(toteAnalogValue);
		
	}
}
