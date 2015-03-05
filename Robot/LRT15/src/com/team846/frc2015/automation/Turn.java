package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.DrivetrainData;
import com.team846.frc2015.log.AsyncPrinter;
import com.team846.frc2015.sensors.DriveEncoders;

public class Turn extends Automation {
	
	DrivetrainData drivetrain;
	
	private double angle;
	private double maxSpeed;
	private double errorThreshold;

	public Turn(double angle, double maxSpeed, double errorThreshold)
	{
		super("Turn");
		AsyncPrinter.println("srinturn");
		this.angle = angle;
		this.maxSpeed = maxSpeed;
		this.errorThreshold = errorThreshold;
		drivetrain = DrivetrainData.Get();
	}

	public Turn()
	{
		super("Turn");
		angle = 0;
		maxSpeed = 0;
		errorThreshold = 0;
		
		drivetrain = DrivetrainData.Get();
	}

	void setAngle(double angle)
	{
		this.angle = angle;
	}

	void setMaxSpeed(double speed)
	{
		maxSpeed = speed;
	}

	void setErrorThreshold(double error)
	{
		errorThreshold = error;
	}

	double getAngle()
	{
		return angle;
	}

	public void AllocateResources()
	{
		AllocateResource(ControlResource.TURN);
	}

	protected boolean Start()
	{
		drivetrain.SetControlMode(DrivetrainData.Axis.TURN, DrivetrainData.ControlMode.POSITION_CONTROL);
		drivetrain.SetRelativePositionSetpoint(DrivetrainData.Axis.TURN, angle);
		drivetrain.SetPositionControlMaxSpeed(DrivetrainData.Axis.TURN, maxSpeed);
		return true;
	}

	protected boolean Run()
	{
		drivetrain.setClassicDrive(true);
		return Math.abs(DriveEncoders.Get().GetTurnAngle() - drivetrain.GetPositionSetpoint(DrivetrainData.Axis.TURN)) < errorThreshold;
	}

	protected boolean Abort()
	{
		drivetrain.setClassicDrive(false);
		return true;
	}


}
