package com.team846.frc2015.components.drivetrain;

public class DriveSpeed {
    private double frontLeft;
    private double frontRight;
    private double backLeft;
    private double backRight;

    public double getFrontLeft() {
        return frontLeft;
    }

    public double getFrontRight() {
        return frontRight;
    }

    public double getBackLeft() {
        return backLeft;
    }

    public double getBackRight() {
        return backRight;
    }

    public DriveSpeed(double frontLeft, double frontRight, double backLeft, double backRight) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
    }
}
