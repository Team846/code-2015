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

public class LoadAdditional extends LoadItem implements Configurable {

	private CarriageHooksData hooksData;
	private int toteAnalogValue = 0;

	public LoadAdditional(boolean auto)
	{
		super("LoadAdditional", ElevatorSetpoint.COLLECT_ADDITIONAL, ElevatorSetpoint.GRAB_TOTE, ElevatorSetpoint.HOME_TOTE, 20, auto );
		hooksData = CarriageHooksData.get();
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
		super.setAnalogThreshold(toteAnalogValue);
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
		if (state == State.GRAB)
		{
			hooksData.setBackHooksDesiredState(HookState.DOWN);
			hooksData.setFrontHooksDesiredState(HookState.DOWN);
		}
		return ret;
	}
}
