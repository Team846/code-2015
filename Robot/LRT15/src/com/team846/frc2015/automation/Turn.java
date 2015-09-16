package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.DrivetrainData;
import com.team846.frc2015.componentData.DrivetrainData.Axis;
import com.team846.frc2015.componentData.DrivetrainData.ControlMode;
import com.team846.frc2015.sensors.DriveEncoders;

public class Turn extends Automation {

    private final DrivetrainData drivetrain;

    private double angle;
    private double maxSpeed;
    private double errorThreshold;

    public Turn(double angle, double maxSpeed, double errorThreshold) {
        this.angle = angle;
        this.maxSpeed = maxSpeed;
        this.errorThreshold = errorThreshold;
        drivetrain = DrivetrainData.get();
    }

    public Turn() {
        this(0, 0, 0);
    }

    public Turn(double angle) {
        this(angle, 1.0, 5);
    }

    public Turn(double angle, double maxSpeed) {
        this(angle, maxSpeed, 5);
    }

    void setAngle(double angle) {
        this.angle = angle;
    }

    void setMaxSpeed(double speed) {
        maxSpeed = speed;
    }

    void setErrorThreshold(double error) {
        errorThreshold = error;
    }

    double getAngle() {
        return angle;
    }

    public void AllocateResources() {
        AllocateResource(ControlResource.TURN);
    }

    protected boolean Start() {
        drivetrain.setClassicDrive(true);
        drivetrain.SetControlMode(DrivetrainData.Axis.TURN, DrivetrainData.ControlMode.POSITION_CONTROL);
        drivetrain.SetRelativePositionSetpoint(DrivetrainData.Axis.TURN, angle);
        drivetrain.SetPositionControlMaxSpeed(DrivetrainData.Axis.TURN, maxSpeed);
        return true;
    }

    protected boolean Run() {
        System.out.println("TURN ANGLE ENCODERS: " + DriveEncoders.Get().GetTurnAngle());

        System.out.println(DriveEncoders.Get().GetTurnAngle());
        drivetrain.setClassicDrive(true);
        return Math.abs(DriveEncoders.Get().GetTurnAngle() - drivetrain.GetPositionSetpoint(DrivetrainData.Axis.TURN)) < errorThreshold;
    }

    protected boolean Abort() {
        drivetrain.setClassicDrive(false);
        drivetrain.SetControlMode(Axis.TURN, ControlMode.OPEN_LOOP);
        drivetrain.SetOpenLoopOutput(Axis.TURN, 0.0);
        return true;
    }


}
