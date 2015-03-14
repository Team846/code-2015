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

public class LoadTote extends LoadItem implements Configurable {
	
	private int toteAnalogValue = 0;

	public LoadTote(boolean auto)
	{
		super("LoadTote", ElevatorSetpoint.COLLECT_TOTE, ElevatorSetpoint.GRAB_TOTE, ElevatorSetpoint.HOME_TOTE,20, auto );
		ConfigRuntime.Register(this);
	}
	
	public LoadTote()
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
				&& ((JoystickReleasedEvent)GetAbortEvent()).GetButton() == DriverStationConfig.JoystickButtons.LOAD_TOTE
				&& ((JoystickReleasedEvent)GetAbortEvent()).GetJoystick() == LRTDriverStation.Instance().GetOperatorStick())
			return false;
		else
			return super.Abort();
	}
}
