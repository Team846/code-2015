package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.DrivetrainData;
import com.team846.frc2015.componentData.DrivetrainData.Axis;
import com.team846.frc2015.utils.AsyncPrinter;

import edu.wpi.first.wpilibj.Timer;

public class Strafe extends Automation {
	double time;
	double maxSpeed;
	private DrivetrainData drivetrain;
	private Timer timer;
	
	public Strafe(double time) //seconds
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
		AsyncPrinter.error("Current Time: " + timer.get() + " thresh: " + time);
		drivetrain.setClassicDrive(false);
		drivetrain.SetOpenLoopOutput(Axis.STRAFE, maxSpeed);
		AsyncPrinter.warn("Applied Throttle: " + drivetrain.GetOpenLoopOutput(Axis.STRAFE));
//		drivetrain.SetOpenLoopOutput(Axis.FORWARD, 0);
//		drivetrain.SetOpenLoopOutput(Axis.TURN, 0);
		return timer.get() > time;
	}

}
