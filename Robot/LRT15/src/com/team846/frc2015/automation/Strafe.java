package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.DrivetrainData;
import com.team846.frc2015.componentData.DrivetrainData.Axis;
import com.team846.frc2015.componentData.DrivetrainData.ControlMode;
import com.team846.frc2015.sensors.DriveEncoders;

import edu.wpi.first.wpilibj.Timer;


//NOTE: Must change to position control once I figure it out
public class Strafe extends Automation {
	double time;
	double maxSpeed;
	private DrivetrainData drivetrain;
	private Timer timer;
	
	public Strafe(double time)
	{
		this(time, 1.0);
	}
	
	public Strafe(double time, double maxSpeed)
	{
		super("Strafe");
		this.time = time;
		this.maxSpeed = maxSpeed;
		timer = new Timer();
		drivetrain = DrivetrainData.get();
	}
	@Override
	public void AllocateResources() {
		AllocateResource(ControlResource.STRAFE);
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
		drivetrain.SetOpenLoopOutput(Axis.STRAFE, 0.0);
		return true;
	}

	@Override
	protected boolean Run() {
		
		drivetrain.setClassicDrive(false);
		drivetrain.SetOpenLoopOutput(Axis.STRAFE, maxSpeed);
		return timer.get() > time;
	}

}
