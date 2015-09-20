package com.team846.frc2015.components.drivetrain;

import com.team846.util.monads.Monad;

public class VariableTurn extends DriveStyle {
    private Monad<Double> normalizedSpeed;

    /**
     * @param normalizedSpeed the rotational speed as a normalized value (negative is counterclockwise, positive is clockwise)
     */
    public VariableTurn(Monad<Double> normalizedSpeed) {
        this.normalizedSpeed = normalizedSpeed;
    }

    @Override
    public DriveSpeed getSpeeds() {
        return new DriveSpeed(normalizedSpeed.get(), -normalizedSpeed.get(), normalizedSpeed.get(), -normalizedSpeed.get());
    }
}
