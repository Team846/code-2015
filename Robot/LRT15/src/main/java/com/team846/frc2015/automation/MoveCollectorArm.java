package com.team846.frc2015.automation;

import com.team846.frc2015.automation.events.JoystickReleasedEvent;
import com.team846.frc2015.componentData.CarriageHooksData;
import com.team846.frc2015.componentData.CollectorArmData;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.componentData.CarriageHooksData.HookState;
import com.team846.frc2015.componentData.CollectorArmData.ArmPosition;
import com.team846.frc2015.componentData.DrivetrainData.ControlMode;
import com.team846.frc2015.componentData.ElevatorData.ElevatorControlMode;
import com.team846.frc2015.componentData.ElevatorData.ElevatorSetpoint;
import com.team846.frc2015.config.ConfigRuntime;
import com.team846.frc2015.config.Configurable;
import com.team846.frc2015.config.DriverStationConfig;
import com.team846.frc2015.driverstation.LRTDriverStation;

public class MoveCollectorArm extends Automation {

	private final CollectorArmData armData;

	public MoveCollectorArm()
	{
		armData = CollectorArmData.get();
	}

	@Override
	protected void AllocateResources() {
		AllocateResource(ControlResource.COLLECTOR_ARMS);
	}

	protected boolean Start()
	{
		return true;
	}
	
	@Override
	protected boolean Abort()
	{
		return true;
	}
	
	@Override
	public boolean Run()
	{
		armData.setDesiredPosition(ArmPosition.EXTEND);
		return false;
	}
}
