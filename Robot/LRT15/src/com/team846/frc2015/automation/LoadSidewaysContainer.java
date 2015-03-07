package com.team846.frc2015.automation;

import com.team846.frc2015.automation.events.JoystickReleasedEvent;
import com.team846.frc2015.componentData.CarriageHooksData;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.componentData.CarriageHooksData.HookState;
import com.team846.frc2015.componentData.ElevatorData.ElevatorSetpoint;
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
				ElevatorData.ElevatorSetpoint.GRAB_SIDEWAYS_CONTAINER, ElevatorData.ElevatorSetpoint.HOME_SIDEWAYS_CONTAINER, auto);
		ConfigRuntime.Register(this);

	}
	
	protected boolean Abort() {
		if (hasItem && (GetAbortEvent() instanceof JoystickReleasedEvent
				&& ((JoystickReleasedEvent)(GetAbortEvent())).GetButton() == DriverStationConfig.JoystickButtons.LOAD_UPRIGHT_CONTAINER
				&& ((JoystickReleasedEvent)(GetAbortEvent())).GetJoystick() == LRTDriverStation.Instance().GetOperatorStick()))
			return false;
		else
			return super.Abort();
	}

	@Override
	public void Configure() {
		// TODO Auto-generated method stub
		
	}
}
