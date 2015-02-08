package com.lynbrookrobotics.frc2015.automation;

import org.luaj.vm2.ast.Stat.Return;

import com.lynbrookrobotics.frc2015.componentData.CarriageExtenderData;

public class ExtendCarriage extends Automation {
	
	float carriagePosition;//[0,1] 
	float errorThreshold;
	
	CarriageExtenderData extenderData;

	public ExtendCarriage(float position ) //position control
	{
		super("Extend Carriage");
		carriagePosition = position;	
		extenderData = CarriageExtenderData.get();
		errorThreshold = 0.1f; //TODO: make configurable
	}

	@Override
	public void AllocateResources() {
		AllocateResource(ControlResource.CARRIAGE_EXTENDER);

	}

	@Override
	protected boolean Start() {
		extenderData.setDesiredPositionSetpoint(carriagePosition);
		return true;
	}

	@Override
	protected boolean Abort() {
		
		return true;
	}

	@Override
	protected boolean Run() {
		float error = Math.abs(carriagePosition - extenderData.getCurrentPosition());
		return error < errorThreshold;
	}

}