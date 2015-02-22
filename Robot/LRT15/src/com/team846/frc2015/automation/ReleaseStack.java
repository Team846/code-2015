package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.CarriageExtenderData;
import com.team846.frc2015.componentData.CarriageHooksData;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.componentData.CarriageHooksData.HookState;
import com.team846.frc2015.componentData.ElevatorData.ElevatorControlMode;
import com.team846.frc2015.config.ConfigRuntime;
import com.team846.frc2015.config.Configurable;

public class ReleaseStack extends Automation implements Configurable {

	private ElevatorData elevatorData;
	private double startingPosition;
	private double dropHeight;
	private CarriageHooksData hooksData;
	private CarriageExtenderData extenderData;

	public ReleaseStack() {
		super("ReleaseStack");
		
		elevatorData = ElevatorData.get();
		hooksData = CarriageHooksData.get();
		extenderData = CarriageExtenderData.get();
		dropHeight = 0;
		ConfigRuntime.Register(this);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void AllocateResources() {
		AllocateResource(ControlResource.ELEVATOR);
		
	}

	@Override
	protected boolean Start() {
		startingPosition = elevatorData.getCurrentPosition();
		return true;
	}

	@Override
	protected boolean Abort() {
		return true;
	}

	@Override
	protected boolean Run() {
		elevatorData.setControlMode(ElevatorControlMode.POSITION);
		elevatorData.setDesiredPosition((startingPosition - dropHeight));
		if(elevatorData.isAtPosition(startingPosition - dropHeight))
		{
			hooksData.setBackHooksCurrentState(HookState.DISENGAGED);
			hooksData.setFrontHooksCurrentState(HookState.DISENGAGED);
			extenderData.setControlMode(CarriageExtenderData.CarriageControlMode.POSITION);
			extenderData.setPositionSetpoint(0.0);
			return true;
		}
		return false;
	}

	@Override
	public void Configure() {
		dropHeight = GetConfig("dropHeight", 20.0); //TODO:Change default once pot comes in
		// TODO Auto-generated method stub
		
	}

}
