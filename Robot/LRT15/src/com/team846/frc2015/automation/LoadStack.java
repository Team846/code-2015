package com.team846.frc2015.automation;

import com.team846.frc2015.automation.events.JoystickReleasedEvent;
import com.team846.frc2015.componentData.CarriageHooksData;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.componentData.CarriageHooksData.HookState;
import com.team846.frc2015.componentData.ElevatorData.ElevatorSetpoint;
import com.team846.frc2015.config.ConfigPortMappings;
import com.team846.frc2015.config.ConfigRuntime;
import com.team846.frc2015.config.Configurable;
import com.team846.frc2015.config.DriverStationConfig;
import com.team846.frc2015.driverstation.LRTDriverStation;
import com.team846.frc2015.sensors.SensorFactory;

import edu.wpi.first.wpilibj.AnalogInput;

public class LoadStack extends LoadItem implements Configurable {

	private CarriageHooksData hooksData;
	private int toteAnalogValue = 0;

	public LoadStack(boolean auto)
	{
		super("LoadStack", ElevatorSetpoint.GRAB_TOTE, ElevatorSetpoint.GRAB_TOTE, ElevatorSetpoint.HOME_TOTE, 0, auto);
		hooksData = CarriageHooksData.get();
		ConfigRuntime.Register(this);
	}
	
	public LoadStack()
	{
		this(false);
	}

	@Override
	public void Configure()
	{
		toteAnalogValue = GetConfig("analog_tote_value", 1600);
		super.setAnalogThreshold(toteAnalogValue);
	}
	
	@Override
	protected boolean Abort()
	{
		if (hasItem && (GetAbortEvent() instanceof JoystickReleasedEvent)
				&& ((JoystickReleasedEvent)GetAbortEvent()).GetButton() == DriverStationConfig.JoystickButtons.LOAD_STACK
				&& ((JoystickReleasedEvent)GetAbortEvent()).GetJoystick() == LRTDriverStation.Instance().GetOperatorStick())
			return false;
		else
			return super.Abort();
	}
	
	@Override
	public boolean Run()
	{
		boolean ret = super.Run();
		if (state == State.COLLECT)
		{
			hooksData.setBackHooksDesiredState(HookState.UP);
			hooksData.setFrontHooksDesiredState(HookState.UP);
		}
		if (state == State.GRAB)
		{
			hooksData.setBackHooksDesiredState(HookState.DOWN);
			hooksData.setFrontHooksDesiredState(HookState.DOWN);
		}
		return ret;
	}
}