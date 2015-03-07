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
	private boolean additional = false;
	private ElevatorData elevatorData;
	private CarriageHooksData hooksData;
	AnalogInput sensor;
	private boolean reachedCollectSetpoint = false;

	public LoadTote(boolean auto)
	{
		super("LoadTote", ElevatorSetpoint.COLLECT_TOTE, ElevatorSetpoint.GRAB_TOTE, ElevatorSetpoint.HOME_TOTE,40, auto );
		sensor = SensorFactory.GetAnalogInput(ConfigPortMappings.Instance().get("Analog/COLLECTOR_PROXIMITY"));
		elevatorData = ElevatorData.get();
		hooksData = CarriageHooksData.get();
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
	protected boolean Start()
	{
		additional = false;
		if (sensor.getAverageValue() > toteAnalogValue)
		{
			additional = true;
		}
		reachedCollectSetpoint = false;
		return super.Start();
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
	
	@Override
	protected boolean Run()
	{
		boolean ret = super.Run();
		if (additional)
		{
			if (state == State.COLLECT)
				elevatorData.setSetpoint(ElevatorData.ElevatorSetpoint.COLLECT_ADDITIONAL);
			else if (state == State.GRAB)
			{
				if (!reachedCollectSetpoint)
				{
					elevatorData.setSetpoint(ElevatorData.ElevatorSetpoint.COLLECT_TOTE);
					hooksData.setFrontHooksDesiredState(HookState.DOWN);
					hooksData.setBackHooksDesiredState(HookState.DOWN);
					if (elevatorData.isAtSetpoint(ElevatorData.ElevatorSetpoint.COLLECT_TOTE))
					{
						reachedCollectSetpoint = true;
					}
				}
			}
		}
		return ret;
	}
}
