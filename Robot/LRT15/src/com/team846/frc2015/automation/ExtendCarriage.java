package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.CarriageExtenderData;
import com.team846.frc2015.componentData.CarriageExtenderData.ControlMode;

public class ExtendCarriage extends Automation {
	
	double carriagePosition;//[0,1] 
	double errorThreshold;
	
	CarriageExtenderData extenderData;

	public ExtendCarriage(double position ) //position control
	{
		super("Extend Carriage");
		carriagePosition = position;	
		extenderData = CarriageExtenderData.get();
		errorThreshold = 0.1f; //TODO: make configurable
	}
	
	public ExtendCarriage()
	{
		this(1.0);
	}

	@Override
	public void AllocateResources() {
		AllocateResource(ControlResource.CARRIAGE_EXTENDER);

	}

	@Override
	protected boolean Start() {
		return true;
	}

	@Override
	protected boolean Abort() {
		extenderData.setControlMode(ControlMode.VELOCITY);
		extenderData.setSpeed(0.0);
		return true;
	}

	@Override
	protected boolean Run() {
		extenderData.setPositionSetpoint(carriagePosition);
		double error = Math.abs(carriagePosition - extenderData.getCurrentPosition());
		return Math.abs(error) < errorThreshold;
	}

}
