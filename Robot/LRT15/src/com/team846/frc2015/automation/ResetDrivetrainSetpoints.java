package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.DrivetrainData;
import com.team846.frc2015.componentData.DrivetrainData.Axis;
import com.team846.frc2015.componentData.DrivetrainData.ControlMode;
import com.team846.frc2015.sensors.DriveEncoders;
import com.team846.frc2015.utils.AsyncPrinter;

public class ResetDrivetrainSetpoints extends Automation {
	
	private final DrivetrainData drivetrain;
	
	public ResetDrivetrainSetpoints()
	{
		super("ResetDrivetrainSetpoints");
		drivetrain = DrivetrainData.get();
	}

	public void AllocateResources()
	{
		AllocateResource(ControlResource.DRIVE);
		AllocateResource(ControlResource.TURN);
	}

	protected boolean Start()
	{
		drivetrain.setClassicDrive(false);
		drivetrain.SetControlMode(DrivetrainData.Axis.TURN, DrivetrainData.ControlMode.OPEN_LOOP);
		drivetrain.SetControlMode(DrivetrainData.Axis.FORWARD, DrivetrainData.ControlMode.OPEN_LOOP);
		return true;
	}

	protected boolean Run()
	{
		return true;
	}

	protected boolean Abort()
	{
		return true;
	}


}
