package com.team846.frc2015.components.drivetrain;

import com.team846.util.monads.Monad;

public class VariableForward extends DriveStyle {
    private Monad<Double> normalizedSpeed;

    /**
     * @param normalizedSpeed the forward speed as a normalized value (negative is backward, positive is forward)
     */
    public VariableForward(Monad<Double> normalizedSpeed) {
        this.normalizedSpeed = normalizedSpeed;
    }

    @Override
    public DriveSpeed getSpeeds() {
        return new DriveSpeed(-normalizedSpeed.get(), normalizedSpeed.get(), normalizedSpeed.get(), -normalizedSpeed.get());
    }
}
