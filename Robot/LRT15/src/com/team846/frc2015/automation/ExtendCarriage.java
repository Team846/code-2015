package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.CarriageExtenderData;
import com.team846.frc2015.componentData.CarriageExtenderData.CarriageControlMode;
import com.team846.frc2015.config.Configurable;

public class ExtendCarriage extends Automation implements Configurable{
	private final double carriagePosition;//[0,1]
	private double errorThreshold;
	
	private final CarriageExtenderData extenderData;
	
	public ExtendCarriage(double position)
	{
		super("Extend Carriage");
		carriagePosition = position;	
		extenderData = CarriageExtenderData.get();
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
		extenderData.setControlMode(CarriageControlMode.VELOCITY);
		extenderData.setSpeed(0.0);
		return true;
	}

	@Override
	protected boolean Run() {
		extenderData.setPositionSetpoint(carriagePosition);
		double error = Math.abs(carriagePosition - extenderData.getCurrentPosition());
		return Math.abs(error) < errorThreshold;
	}

	@Override
	public void Configure() {
		errorThreshold = GetConfig("ErrorThreshold", 0.5);
	}

}
