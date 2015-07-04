package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.DrivetrainData;
import com.team846.frc2015.sensors.LRTGyro;
import com.team846.frc2015.componentData.DrivetrainData.Axis;
import com.team846.frc2015.sensors.DriveEncoders;
import com.team846.frc2015.utils.AsyncPrinter;

import edu.wpi.first.wpilibj.Timer;

public class Strafe extends Automation {
	private final double time;
	private final double maxSpeed;
	private final DrivetrainData drivetrain;
	private final Timer timer;
	private int ticks;
	private double errorThreshold;
	private int startTicks;
	DriveEncoders encoders;
	private float startAngle;
	LRTGyro gyro;

	// TODO: implement remaining constructors
//	public Strafe(double time) //seconds
//	{
//		this(time, 1.0);
//	}
	
	public Strafe(int ticks, double maxSpeed, double errorThreshold)
	{
		timer = new Timer();
		time = 0;
		this.maxSpeed = maxSpeed;
		this.ticks = ticks;
		this.errorThreshold = errorThreshold;
		drivetrain = DrivetrainData.get();
		encoders = DriveEncoders.Get();
		gyro = LRTGyro.Get();
	}
	
//	public Strafe(double time, double maxSpeed)
//	{
//		super("Strafe");
//		this.time = time;
//		this.maxSpeed = maxSpeed;
//		timer = new Timer();
//		drivetrain = DrivetrainData.get();
//	}
	@Override
	public void AllocateResources() {
		AllocateResource(ControlResource.STRAFE);
		AllocateResource(ControlResource.TURN);
	}

	@Override
	protected boolean Start() {
		timer.reset();
		timer.start();
		drivetrain.setClassicDrive(false);
		drivetrain.SetControlMode(DrivetrainData.Axis.TURN, DrivetrainData.ControlMode.POSITION_CONTROL);
		drivetrain.SetRelativePositionSetpoint(DrivetrainData.Axis.TURN, ticks);
		drivetrain.SetPositionControlMaxSpeed(DrivetrainData.Axis.TURN, maxSpeed);
		startTicks = encoders.GetStrafeTicks();
		startAngle = gyro.getY();
		return true;
	}

	@Override
	protected boolean Abort() {
		timer.stop();
		drivetrain.SetOpenLoopOutput(Axis.STRAFE, 0.0);
		return true;
	}

	@Override
	protected boolean Run() {
		drivetrain.setClassicDrive(false);
		drivetrain.SetOpenLoopOutput(Axis.STRAFE, maxSpeed * Math.signum(ticks));
		drivetrain.SetOpenLoopOutput(Axis.TURN, (startAngle - gyro.getY()) * 0.1);
//		return Math.abs(startTicks - encoders.GetStrafeTicks()) < errorThreshold;
		return Math.abs(DriveEncoders.Get().GetTurnAngle() - drivetrain.GetPositionSetpoint(DrivetrainData.Axis.TURN)) < errorThreshold;
	}

}
