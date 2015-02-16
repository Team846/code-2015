package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.DrivetrainData;
import com.team846.frc2015.componentData.DrivetrainData.Axis;
import com.team846.frc2015.componentData.DrivetrainData.ControlMode;
import com.team846.frc2015.sensors.DriveEncoders;
import com.team846.frc2015.utils.MathUtils;

public class Drive extends Automation {
	
	DrivetrainData drivetrain;
		
	double distance;
	double maxSpeed;
	double errorThreshold;
	boolean continuous;
	
	public Drive(double distance)
	{
		this(distance, 1.0 , 0.5 , false);
	}
	
	public Drive(double distance, double maxSpeed)
	{
		this(distance, maxSpeed, 0.5, false);
	}
	
	public Drive(double distance, double maxSpeed, double errThreshold)
	{
		this(distance, maxSpeed, errThreshold, false);
	}
	
	public Drive(double distance, double maxSpeed, double errThreshold, boolean continuous)
	{
		super("Drive");
		this.distance = distance;
		this.maxSpeed = maxSpeed;
		this.errorThreshold = errThreshold;
		this.continuous = continuous;
		drivetrain = DrivetrainData.Get();
	}
	
	public void AllocateResources()
	{
		AllocateResource(ControlResource.DRIVE);
	}

	public boolean Start()
	{
		if (!continuous)
		{
			drivetrain.SetControlMode(Axis.FORWARD, ControlMode.POSITION_CONTROL);
		}
		else
		{
			drivetrain.SetControlMode(Axis.FORWARD, ControlMode.VELOCITY_CONTROL);
			drivetrain.SetVelocitySetpoint(Axis.FORWARD, MathUtils.Sign(distance) * maxSpeed);
		}
		drivetrain.SetRelativePositionSetpoint(Axis.FORWARD, distance);
		drivetrain.SetPositionControlMaxSpeed(Axis.FORWARD, maxSpeed);
		return true;
	}

	public boolean Run()
	{
		drivetrain.setClassicDrive(true);
		double robotLocation = DriveEncoders.Get().GetRobotDist();
		double setpoint = drivetrain.GetPositionSetpoint(Axis.FORWARD);
		double distanceLeft = Math.abs(setpoint - robotLocation);
		
		return distanceLeft < errorThreshold;
		
//		if (distance > 0)
//			return DriveEncoders.Get().GetRobotDist() > drivetrain.GetPositionSetpoint(DrivetrainData.FORWARD);
//		else
//			return DriveEncoders.Get().GetRobotDist() < drivetrain.GetPositionSetpoint(DrivetrainData.FORWARD);
	}

	public boolean Abort()
	{
		drivetrain.setClassicDrive(false);
		if (continuous)
		{
			drivetrain.SetControlMode(Axis.FORWARD, ControlMode.POSITION_CONTROL);
		}
		return true;
	}

}
