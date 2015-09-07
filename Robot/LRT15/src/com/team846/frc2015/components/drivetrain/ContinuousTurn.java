package com.team846.frc2015.components.drivetrain;

public class ContinuousTurn extends DriveStyle {
    private double normalizedSpeed;

    /**
     * @param normalizedSpeed the rotational speed as a normalized value (negative is counterclockwise, positive is clockwise)
     */
    public ContinuousTurn(double normalizedSpeed) {
        this.normalizedSpeed = normalizedSpeed;
    }

    @Override
    public DriveSpeed getSpeeds() {
        return new DriveSpeed(normalizedSpeed, -normalizedSpeed, normalizedSpeed, -normalizedSpeed);
    }
}
