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

public class LoadAdditional extends LoadItem implements Configurable {

	private final CarriageHooksData hooksData;
	private final ElevatorData elevatorData;
	private int toteAnalogValue = 0;
	private int startPosition = 0;
	private int hookDisengageDrop = 0;

	public LoadAdditional(boolean auto)
	{
		super("LoadAdditional", ElevatorSetpoint.COLLECT_ADDITIONAL, ElevatorSetpoint.GRAB_TOTE, ElevatorSetpoint.HOME_TOTE, 0, auto );
		hooksData = CarriageHooksData.get();
		elevatorData = ElevatorData.get();
		ConfigRuntime.Register(this);
	}
	
	public LoadAdditional()
	{
		this(false);
	}

	@Override
	public void Configure()
	{
		toteAnalogValue = GetConfig("analog_tote_value", 1600);
		hookDisengageDrop = GetConfig("hook_disengage_drop", 500);
		super.setAnalogThreshold(toteAnalogValue);
	}
	
	protected boolean Start()
	{
		startPosition = elevatorData.getCurrentPosition();
		return super.Start();
	}
	
	@Override
	protected boolean Abort()
	{
		if (hasItem && (GetAbortEvent() instanceof JoystickReleasedEvent)
				&& ((JoystickReleasedEvent)GetAbortEvent()).GetButton() == DriverStationConfig.JoystickButtons.LOAD_ADDITIONAL
				&& ((JoystickReleasedEvent)GetAbortEvent()).GetJoystick() == LRTDriverStation.Instance().GetOperatorStick())
			return false;
		else
			return super.Abort();
	}
	
	@Override
	public boolean Run()
	{
		boolean ret = super.Run();
		if (elevatorData.isAtSetpoint(ElevatorSetpoint.COLLECT_ADDITIONAL))
		{
			startPosition = elevatorData.getCurrentPosition();
		}
		if (state == State.GRAB)
		{
			if (elevatorData.getCurrentPosition() > startPosition + hookDisengageDrop)
			{
				hooksData.setBackHooksDesiredState(HookState.UP);
				hooksData.setFrontHooksDesiredState(HookState.UP);
			}
			else
			{
				hooksData.setBackHooksDesiredState(HookState.DOWN);
				hooksData.setFrontHooksDesiredState(HookState.DOWN);
			}
		}
		return ret;
	}
}
