package com.team846.frc2015.components.drivetrain;

import com.team846.util.monads.Monad;

public class VariableStrafe extends DriveStyle {
    private Monad<Double> normalizedSpeed;

    /**
     * @param normalizedSpeed the strafe speed as a normalized value (negative is left, positive is right)
     */
    public VariableStrafe(Monad<Double> normalizedSpeed) {
        this.normalizedSpeed = normalizedSpeed;
    }

    @Override
    public DriveSpeed getSpeeds() {
        return new DriveSpeed(normalizedSpeed.get(), normalizedSpeed.get(), normalizedSpeed.get(), normalizedSpeed.get());
    }
}
