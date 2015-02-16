package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.CarriageExtenderData;
import com.team846.frc2015.componentData.CarriageHooksData;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.componentData.CarriageHooksData.HookState;
import com.team846.frc2015.componentData.ElevatorData.ElevatorControlMode;
import com.team846.frc2015.componentData.ElevatorData.Setpoint;

public class LoadUprightContainer extends Automation {
	
	private CarriageHooksData hooksData;
	private CarriageExtenderData extenderData;
	private ElevatorData elevatorData;

	public LoadUprightContainer()
	{	
		super("LoadUprightContainer");
		hooksData = CarriageHooksData.get();
		extenderData = CarriageExtenderData.get();
		elevatorData = ElevatorData.get();
	}
	

	@Override
	public void AllocateResources()
	{
		AllocateResource(ControlResource.CARRIAGE_EXTENDER);
		AllocateResource(ControlResource.CARRIAGE_HOOKS);
		AllocateResource(ControlResource.ELEVATOR);
	}

	@Override
	protected boolean Start() {
		return true;
	}

	@Override
	protected boolean Abort() {
		elevatorData.setControlMode(ElevatorControlMode.SPEED);
		elevatorData.setSpeed(0.0);
		return true;
	}

	@Override
	protected boolean Run() 
	{
		hooksData.setFrontHooksState(HookState.ENGAGED);
		elevatorData.setControlMode(ElevatorControlMode.SETPOINT);
		elevatorData.setSetpoint(Setpoint.HOME);
		 
		return elevatorData.isAtSetpoint(Setpoint.HOME);
	}

}
