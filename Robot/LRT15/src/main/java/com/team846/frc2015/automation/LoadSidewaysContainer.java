package com.team846.frc2015.automation;

import com.team846.frc2015.automation.events.JoystickReleasedEvent;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.config.ConfigRuntime;
import com.team846.frc2015.config.Configurable;
import com.team846.frc2015.config.DriverStationConfig;
import com.team846.frc2015.driverstation.LRTDriverStation;


public class LoadSidewaysContainer extends LoadItem implements Configurable {

	public LoadSidewaysContainer() {
		this(false);
	}
	
	public LoadSidewaysContainer(boolean auto) {
		super("LoadSidewaysContainer", ElevatorData.ElevatorSetpoint.COLLECT_SIDEWAYS_CONTAINER,
				ElevatorData.ElevatorSetpoint.GRAB_SIDEWAYS_CONTAINER, ElevatorData.ElevatorSetpoint.HOME_SIDEWAYS_CONTAINER,0, auto);
		ConfigRuntime.Register(this);

	}
	
	protected boolean Abort() {
		if (hasItem && (GetAbortEvent() instanceof JoystickReleasedEvent
				&& ((JoystickReleasedEvent)(GetAbortEvent())).GetButton() == DriverStationConfig.JoystickButtons.LOAD_SIDEWAYS_CONTAINER
				&& ((JoystickReleasedEvent)(GetAbortEvent())).GetJoystick() == LRTDriverStation.instance().getOperatorStick()))
			return false;
		else
			return super.Abort();
	}
	
	

	@Override
	public void Configure() {
		int toteAnalogValue = GetConfig("analog_container_value", 600);
		super.setAnalogThreshold(toteAnalogValue);
		
	}
}
