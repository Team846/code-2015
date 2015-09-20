package com.team846.frc2015.components.drivetrain;

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

    public DriveSpeed(double frontLeft, double frontRight, double backLeft, double backRight) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
    }
}
