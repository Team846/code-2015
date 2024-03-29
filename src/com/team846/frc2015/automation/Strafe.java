package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.DrivetrainData;
import com.team846.frc2015.componentData.DrivetrainData.Axis;
import com.team846.frc2015.sensors.DriveEncoders;
import com.team846.robot.LRT15Robot;

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
    private double startAngle;

    // TODO: implement remaining constructors
//	public Strafe(double time) //seconds
//	{
//		this(time, 1.0);
//	}

    public Strafe(int ticks, double maxSpeed, double errorThreshold) {
        timer = new Timer();
        time = 0;
        this.maxSpeed = maxSpeed;
        this.ticks = ticks;
        this.errorThreshold = errorThreshold;
        drivetrain = DrivetrainData.get();
        encoders = DriveEncoders.Get();
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
        startAngle = LRT15Robot.gyro.getAngle();
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
        boolean good;
        if (ticks < 0) {
            // going left
            good = (startTicks + ticks) > encoders.GetStrafeTicks();
        } else {
            // going right
            good = (startTicks + ticks) < encoders.GetStrafeTicks();
        }

        if (good) {
            drivetrain.SetControlMode(Axis.STRAFE, DrivetrainData.ControlMode.OPEN_LOOP);
            drivetrain.SetOpenLoopOutput(Axis.STRAFE, 0.0);
        }

        return good;
    }

}
