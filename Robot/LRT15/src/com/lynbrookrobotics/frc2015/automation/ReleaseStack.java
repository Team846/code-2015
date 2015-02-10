package com.lynbrookrobotics.frc2015.automation;

import com.lynbrookrobotics.frc2015.componentData.ElevatorData;
import com.lynbrookrobotics.frc2015.componentData.ElevatorData.ControlMode;
import com.lynbrookrobotics.frc2015.config.ConfigRuntime;
import com.lynbrookrobotics.frc2015.config.Configurable;

public class ReleaseStack extends Automation implements Configurable {

	private ElevatorData elevatorData;
	private float startingPosition;
	private double dropHeight;

	public ReleaseStack() {
		super("ReleaseStack");
		
		elevatorData = ElevatorData.get();
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
		startingPosition = elevatorData.getPosition();
		return true;
	}

	@Override
	protected boolean Abort() {
		return true;
	}

	@Override
	protected boolean Run() {
		elevatorData.setControlMode(ControlMode.POSITION);
		elevatorData.setPosition((float) (startingPosition - dropHeight));
		return true;
	}

	@Override
	public void Configure() {
		dropHeight = GetConfig("dropHeight", 20.0f); //TODO:Change default once pot comes in
		// TODO Auto-generated method stub
		
	}

}