package com.team846.frc2015.components.drivetrain;

import com.team846.frc2015.automation.Drive;

public class DriveSpeed {
    private double frontLeft;
    private double frontRight;
    private double backLeft;
    private double backRight;

    /**
     * @return the normalized front-left speed (positive is forward)
     */
    public double getFrontLeft() {
        return frontLeft;
    }

    /**
     * @return the normalized front-right speed (positive is forward)
     */
    public double getFrontRight() {
        return frontRight;
    }

    /**
     * @return the normalized back-left speed (positive is forward)
     */
    public double getBackLeft() {
        return backLeft;
    }

    /**
     * @return the normalized back-right speed (positive is forward)
     */
    public double getBackRight() {
        return backRight;
    }

    public DriveSpeed plus(DriveSpeed that) {
        return new DriveSpeed(
            this.getFrontLeft() + that.getFrontLeft(),
            this.getFrontRight() + that.getFrontRight(),
            this.getBackLeft() + that.getBackLeft(),
            this.getBackRight() + that.getBackRight()
        );
    }

    public DriveSpeed(double frontLeft, double frontRight, double backLeft, double backRight) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
    }
}
