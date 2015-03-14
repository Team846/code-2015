package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.DrivetrainData;
import com.team846.frc2015.componentData.DrivetrainData.Axis;
import com.team846.frc2015.utils.AsyncPrinter;

import edu.wpi.first.wpilibj.Timer;

public class TimeDrive extends Automation {
	
	private Timer timer;
	private double maxSpeed;

	private double time;
	private DrivetrainData drivetrain;
	
	public TimeDrive(double time)
	{
		this(time,1.0);
	}
	
	public TimeDrive(double time, double maxSpeed)
	{
		this.maxSpeed = maxSpeed;
		this.time = time;
		timer = new Timer();
		drivetrain = DrivetrainData.get();
	}

	@Override
	public void AllocateResources() {
			AllocateResource(ControlResource.DRIVE);
	}

	@Override
	protected boolean Start() {
		timer.reset();
		timer.start();
		return true;
	}

	@Override
	protected boolean Abort() {
		timer.stop();
		drivetrain.SetOpenLoopOutput(Axis.FORWARD, 0.0);
		return true;
	}

	@Override
	protected boolean Run() {
		drivetrain.setClassicDrive(false);
		drivetrain.SetOpenLoopOutput(Axis.FORWARD, maxSpeed);
		drivetrain.SetOpenLoopOutput(Axis.STRAFE, 0);
		drivetrain.SetOpenLoopOutput(Axis.TURN, 0);
		return timer.get() > time;
	}

}
