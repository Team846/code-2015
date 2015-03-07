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
import com.team846.frc2015.driverstation.LRTJoystick;
import com.team846.frc2015.sensors.SensorFactory;

import edu.wpi.first.wpilibj.AnalogInput;

public class HumanLoad extends LoadTote {
	
	private ElevatorData elevatorData;
	private CarriageHooksData hooksData;
	private HumanLoadState humanLoadState;
	private LRTJoystick operatorStick;
	
	enum HumanLoadState
	{
		PREPARE,
		GRAB,
		FINISHED,
	}

	public HumanLoad()
	{
		elevatorData = ElevatorData.get();
		hooksData = CarriageHooksData.get();
		operatorStick = LRTDriverStation.Instance().GetOperatorStick();
	}
	
	protected boolean Start()
	{
		humanLoadState = HumanLoadState.PREPARE;
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
	protected boolean Run()
	{
		boolean ret = super.Run();
		if (state == State.GRAB)
		{
			switch (humanLoadState)
			{
			case PREPARE:
				elevatorData.setSetpoint(ElevatorData.ElevatorSetpoint.HUMAN_LOAD_PREPARE);
				hooksData.setFrontHooksDesiredState(HookState.DOWN);
				hooksData.setBackHooksDesiredState(HookState.DOWN);
				if (operatorStick.IsButtonJustPressed(DriverStationConfig.JoystickButtons.HUMAN_LOAD_START))
					humanLoadState = HumanLoadState.GRAB;
				if (operatorStick.IsButtonJustPressed(DriverStationConfig.JoystickButtons.HUMAN_LOAD_FINISH))
					humanLoadState = HumanLoadState.FINISHED;
				break;
			case GRAB:
				elevatorData.setSetpoint(ElevatorData.ElevatorSetpoint.HUMAN_LOAD_GRAB);
				hooksData.setFrontHooksDesiredState(HookState.DOWN);
				hooksData.setBackHooksDesiredState(HookState.DOWN);
				if (elevatorData.isAtSetpoint(ElevatorData.ElevatorSetpoint.HUMAN_LOAD_GRAB))
					humanLoadState = HumanLoadState.PREPARE;
				break;
			case FINISHED:
				break;
			}
		}
		return ret;
	}
}
