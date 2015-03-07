package com.team846.frc2015.automation;

import com.team846.frc2015.automation.events.JoystickReleasedEvent;
import com.team846.frc2015.componentData.CarriageExtenderData;
import com.team846.frc2015.componentData.CarriageHooksData;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.componentData.CarriageHooksData.HookState;
import com.team846.frc2015.componentData.ElevatorData.ElevatorControlMode;
import com.team846.frc2015.componentData.ElevatorData.ElevatorSetpoint;
import com.team846.frc2015.config.ConfigRuntime;
import com.team846.frc2015.config.Configurable;
import com.team846.frc2015.config.DriverStationConfig;
import com.team846.frc2015.driverstation.LRTDriverStation;

public class LoadUprightContainer extends LoadItem implements Configurable {

	private CarriageHooksData hooksData;
	private int toteAnalogValue = 0 ;
	
	public LoadUprightContainer(boolean auto) {
		super("LoadUprightContainer", ElevatorData.ElevatorSetpoint.COLLECT_UPRIGHT_CONTAINER,
				ElevatorData.ElevatorSetpoint.COLLECT_UPRIGHT_CONTAINER, ElevatorData.ElevatorSetpoint.HOME_UPRIGHT_CONTAINER,auto );

		hooksData = CarriageHooksData.get();
		ConfigRuntime.Register(this);
	}
	
	public LoadUprightContainer() {
		this(false);
	}

	@Override
	protected boolean Run()
	{
		boolean ret = super.Run();

		// Override hook states
		if (state == State.COLLECT)
		{
			hooksData.setFrontHooksDesiredState(HookState.UP);
			hooksData.setBackHooksDesiredState(HookState.DOWN);
		}
		else if (state == State.GRAB)
		{
			hooksData.setFrontHooksDesiredState(HookState.DOWN);
			hooksData.setBackHooksDesiredState(HookState.DOWN);
		}
		
		return ret;
	}
	
	@Override
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
		toteAnalogValue  = GetConfig("analog_container_value", 600);
		super.setAnalogThreshold(toteAnalogValue);
		
	}
}

