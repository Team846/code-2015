package com.team846.frc2015.automation;

import com.team846.frc2015.automation.events.JoystickReleasedEvent;
import com.team846.frc2015.componentData.CarriageHooksData;
import com.team846.frc2015.componentData.CollectorArmData;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.componentData.CarriageHooksData.HookState;
import com.team846.frc2015.config.ConfigRuntime;
import com.team846.frc2015.config.Configurable;
import com.team846.frc2015.config.DriverStationConfig;
import com.team846.frc2015.driverstation.LRTDriverStation;
import com.team846.frc2015.driverstation.LRTJoystick;

public class ContinuousLoad extends LoadAdditional {
	
	private final LRTJoystick operatorStick;
	private boolean last = false;
	
	public ContinuousLoad()
	{
		super();
		operatorStick = LRTDriverStation.Instance().GetOperatorStick();
	}
	
	protected boolean Start()
	{
		last = false;
		return super.Start();
	}

	@Override
	protected boolean Abort()
	{
		if (hasItem && (GetAbortEvent() instanceof JoystickReleasedEvent)
				&& ((JoystickReleasedEvent)GetAbortEvent()).GetButton() == DriverStationConfig.JoystickButtons.HUMAN_LOAD_START
				&& ((JoystickReleasedEvent)GetAbortEvent()).GetJoystick() == LRTDriverStation.Instance().GetOperatorStick())
			return false;
		else
			return super.Abort();
	}
	
	@Override
	public boolean Run()
	{
		boolean ret = super.Run();
		if (operatorStick.IsButtonDown(DriverStationConfig.JoystickButtons.HUMAN_LOAD_FINISH))
			last = true;
		if (state == State.HOME && !last)
		{
			state = State.COLLECT;
		}
		return ret;
	}
}
