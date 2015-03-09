package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.DrivetrainData;
import com.team846.frc2015.componentData.DrivetrainData.Axis;
import com.team846.frc2015.componentData.DrivetrainData.ControlMode;
import com.team846.frc2015.sensors.DriveEncoders;
import com.team846.frc2015.sensors.DriveEncoders.Side;
import com.team846.frc2015.utils.MathUtils;

public class Drive extends Automation {
	
	DrivetrainData drivetrain;
		
	double distance;
	double maxSpeed;
	double errorThreshold;
	boolean constantSpeed;
	
	public Drive(double distance)
	{
		this(distance, 1.0 , 1 , false);
	}
	
	public Drive(double distance, double maxSpeed)
	{
		this(distance, maxSpeed, 1, false);
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
		this.constantSpeed = continuous;
		drivetrain = DrivetrainData.get();
	}
	
	public void AllocateResources()
	{
		AllocateResource(ControlResource.DRIVE);
	}

	public boolean Start()
	{
		DriveEncoders.Get().Reset();
		if (!constantSpeed)
		{
			drivetrain.SetControlMode(Axis.FORWARD, ControlMode.POSITION_CONTROL);
		}
		else
		{
			drivetrain.SetOpenLoopOutput(Axis.FORWARD, MathUtils.Sign(distance) * maxSpeed);
//			drivetrain.SetControlMode(Axis.FORWARD, ControlMode.VELOCITY_CONTROL);
//			drivetrain.SetVelocitySetpoint(Axis.FORWARD, MathUtils.Sign(distance) * maxSpeed);
		}
		drivetrain.SetPositionSetpoint(Axis.FORWARD, distance);
		drivetrain.SetPositionControlMaxSpeed(Axis.FORWARD, maxSpeed);
		return true;
	}

	public boolean Run()
	{
		if (!constantSpeed)
			drivetrain.setClassicDrive(true);
		else
			drivetrain.SetOpenLoopOutput(Axis.FORWARD, MathUtils.Sign(distance) * maxSpeed);

		double robotLocation = DriveEncoders.Get().GetRobotDist();
		double setpoint = drivetrain.GetPositionSetpoint(Axis.FORWARD);
		double distanceLeft = Math.abs(setpoint - robotLocation);
		System.out.println("left encoder ticks: " + DriveEncoders.Get().GetEncoder(Side.LEFT_BACK).get()); 
		System.out.println("right encoder ticks: " + DriveEncoders.Get().GetEncoder(Side.RIGHT_BACK).get());

		System.out.println("distance left: " + distanceLeft);
		return distanceLeft < errorThreshold;
		
//		if (distance > 0)
//			return DriveEncoders.Get().GetRobotDist() > drivetrain.GetPositionSetpoint(DrivetrainData.FORWARD);
//		else
//			return DriveEncoders.Get().GetRobotDist() < drivetrain.GetPositionSetpoint(DrivetrainData.FORWARD);
	}

	public boolean Abort()
	{
		drivetrain.setClassicDrive(false);
		if (constantSpeed)
		{
			drivetrain.SetControlMode(Axis.FORWARD, ControlMode.POSITION_CONTROL);
		}
		return true;
	}

}
