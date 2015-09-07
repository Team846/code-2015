package com.team846.frc2015.control;

import com.team846.frc2015.utils.Pair;

public class Pivot {
    private final double R;

    public Pivot(double r) {
        this.R = r;
    }

    public Pair<Double, Double> get(double angularChange) {
        double W = angularVelocity(angularChange);
        double strafe = strafeVelocity(W);

        return new Pair<Double, Double>(W, strafe);
    }

    private double angularVelocity(double angleChange) {
        return angleChange / 0.02;
    }

    private double strafeVelocity(double W) {
        return W * R;
    }
}
