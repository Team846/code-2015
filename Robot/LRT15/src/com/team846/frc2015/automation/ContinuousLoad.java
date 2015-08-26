package com.team846.frc2015.automation;

import com.team846.frc2015.automation.events.JoystickReleasedEvent;
import com.team846.frc2015.componentData.CarriageHooksData;
import com.team846.frc2015.componentData.CollectorArmData;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.componentData.CarriageHooksData.HookState;
import com.team846.frc2015.componentData.ElevatorData.ElevatorControlMode;
import com.team846.frc2015.componentData.ElevatorData.ElevatorSetpoint;
import com.team846.frc2015.config.ConfigRuntime;
import com.team846.frc2015.config.Configurable;
import com.team846.frc2015.config.DriverStationConfig;
import com.team846.frc2015.driverstation.LRTDriverStation;
import com.team846.frc2015.driverstation.LRTJoystick;

public class ContinuousLoad extends LoadAdditional {

	private final CarriageHooksData hooksData;
	private final ElevatorData elevatorData;
	private final CollectorArmData armData;
	private final LRTJoystick operatorStick;
	private boolean last = false;
	
	public ContinuousLoad()
	{
		super(true);
		operatorStick = LRTDriverStation.instance().getOperatorStick();
		hooksData = CarriageHooksData.get();
		elevatorData = ElevatorData.get();
		armData = CollectorArmData.get();
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
				&& ((JoystickReleasedEvent)GetAbortEvent()).GetJoystick() == LRTDriverStation.instance().getOperatorStick())
		{
			last = true;
			return false;
		}
		else
			return super.Abort();
	}
	
	@Override
	public boolean Run()
	{
		boolean ret = super.Run();
		if (operatorStick.isButtonDown(DriverStationConfig.JoystickButtons.HUMAN_LOAD_FINISH))
			last = true;
		if (state == State.HOME && !last)
		{
			hooksData.setBackHooksDesiredState(HookState.DOWN);
			hooksData.setFrontHooksDesiredState(HookState.DOWN);
			
			elevatorData.setControlMode(ElevatorData.ElevatorControlMode.SETPOINT);
			elevatorData.setSetpoint(ElevatorData.ElevatorSetpoint.COLLECT_ADDITIONAL);
			if (elevatorData.isAtSetpoint(ElevatorData.ElevatorSetpoint.COLLECT_ADDITIONAL))
				state = State.COLLECT;
		}
		return ret;
	}
}
