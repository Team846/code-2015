package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.DrivetrainData;
import com.team846.frc2015.componentData.DrivetrainData.Axis;
import com.team846.frc2015.componentData.DrivetrainData.ControlMode;
import com.team846.frc2015.sensors.DriveEncoders;
import com.team846.frc2015.utils.MathUtils;

public class Drive extends Automation {

    private final DrivetrainData drivetrain;

    private final double distance;
    private final double maxSpeed;
    private final double errorThreshold;
    private final boolean constantSpeed;

    public Drive(double distance) {
        this(distance, 1.0, 1, false);
    }

    public Drive(double distance, double maxSpeed) {
        this(distance, maxSpeed, 1, false);
    }

    public Drive(double distance, double maxSpeed, double errThreshold) {
        this(distance, maxSpeed, errThreshold, false);
    }

    public Drive(double distance, double maxSpeed, double errThreshold, boolean continuous) {
        this.distance = distance;
        this.maxSpeed = maxSpeed;
        this.errorThreshold = errThreshold;
        this.constantSpeed = continuous;
        drivetrain = DrivetrainData.get();
    }

    public void AllocateResources() {
        AllocateResource(ControlResource.DRIVE);
    }

    public boolean Start() {
        drivetrain.setClassicDrive(true);
//		DriveEncoders.get().Reset();
        if (!constantSpeed) {
            drivetrain.SetControlMode(Axis.FORWARD, ControlMode.POSITION_CONTROL);
        } else {
            drivetrain.SetControlMode(Axis.FORWARD, ControlMode.OPEN_LOOP);
            drivetrain.SetOpenLoopOutput(Axis.FORWARD, MathUtils.sign(distance) * maxSpeed);
//			drivetrain.setControlMode(Axis.FORWARD, ControlMode.VELOCITY_CONTROL);
//			drivetrain.SetVelocitySetpoint(Axis.FORWARD, MathUtils.Sign(distance) * maxSpeed);
        }
        drivetrain.SetRelativePositionSetpoint(Axis.FORWARD, distance);
        drivetrain.SetPositionControlMaxSpeed(Axis.FORWARD, maxSpeed);
        drivetrain.SetControlMode(Axis.TURN, ControlMode.POSITION_CONTROL);
        drivetrain.SetRelativePositionSetpoint(Axis.TURN, 0.0);

        return true;
    }

    int ticksLeftForFinish = 0;

    public boolean Run() {
        drivetrain.setClassicDrive(true);
        if (constantSpeed)
            drivetrain.SetOpenLoopOutput(Axis.FORWARD, MathUtils.sign(distance) * maxSpeed);

        drivetrain.SetControlMode(Axis.TURN, ControlMode.POSITION_CONTROL);
        drivetrain.SetRelativePositionSetpoint(Axis.TURN, 0.0);

        double robotLocation = DriveEncoders.Get().GetRobotDist();
        double setpoint = drivetrain.GetPositionSetpoint(Axis.FORWARD);

        double distanceLeft = setpoint - robotLocation;
        if (distance < 0)
            distanceLeft = -distanceLeft; // Going backwards

        System.out.println("robot distance: " + robotLocation);
        System.out.println("fqpoint: " + drivetrain.GetPositionSetpoint(Axis.FORWARD));
        System.out.println("distance left: " + distanceLeft);
        System.out.println("ControlMode: " + drivetrain.GetControlMode(Axis.FORWARD));

        if (distanceLeft >= errorThreshold) {
            ticksLeftForFinish = 20;
        } else {
            ticksLeftForFinish--;
        }

        return ticksLeftForFinish <= 0;

//		if (distance > 0)
//			return DriveEncoders.get().GetRobotDist() > drivetrain.GetPositionSetpoint(Axis.FORWARD);
//		else
//			return DriveEncoders.get().GetRobotDist() < drivetrain.GetPositionSetpoint(Axis.FORWARD);
    }

    public boolean Abort() {
        if (constantSpeed) {
            drivetrain.SetControlMode(Axis.FORWARD, ControlMode.POSITION_CONTROL);
        }

        return true;
    }

}
