package com.team846.frc2015.components.drivetrain;

import com.team846.frc2015.actuators.DriveESC;
import com.team846.frc2015.components.Component;
import com.team846.frc2015.sensors.DriveEncoders;
import com.team846.util.monads.CachedMonad;
import com.typesafe.config.Config;
import edu.wpi.first.wpilibj.CANTalon;

import java.util.List;

public class Drivetrain extends Component {
    private DriveStyle currentStyle;

    // TODO: replace with bean-like public grab(class) bottling?
    private CachedMonad<Config> configMonad = null;

    private CachedMonad<Config> currentLimiting = configMonad.map(config -> config.getConfig("current-limiting"));
    private CachedMonad<Double> forwardLimit = currentLimiting.map(config -> config.getDouble("forward-limit"));
    private CachedMonad<Double> reverseLimit = currentLimiting.map(config -> config.getDouble("reverse-limit"));

    private CachedMonad<Config> motorPorts = configMonad.map(config -> config.getConfig("motor-ports"));
    private CachedMonad<CANTalon> frontLeftCAN = motorPorts.map(config -> config.getInt("front-left")).map(CANTalon::new);
    private CachedMonad<CANTalon> backLeftCAN = motorPorts.map(config -> config.getInt("back-left")).map(CANTalon::new);
    private CachedMonad<CANTalon> frontRightCAN = motorPorts.map(config -> config.getInt("front-right")).map(CANTalon::new);
    private CachedMonad<CANTalon> backRightCAN = motorPorts.map(config -> config.getInt("back-right")).map(CANTalon::new);

    private CachedMonad<DriveEncoders> driveEncoders = CachedMonad.sequence(
            frontLeftCAN,
            frontRightCAN,
            backLeftCAN,
            backRightCAN).map(motors ->
        new DriveEncoders(motors.get(0), motors.get(1), motors.get(2), motors.get(3))
    );

    private CachedMonad<DriveESC> frontLeft = frontLeftCAN.map(DriveESC::new);
    private CachedMonad<DriveESC> frontRight = frontRightCAN.map(DriveESC::new);
    private CachedMonad<DriveESC> backLeft = backLeftCAN.map(DriveESC::new);
    private CachedMonad<DriveESC> backRight = backRightCAN.map(DriveESC::new);
    private CachedMonad<List<DriveESC>> allMotors = CachedMonad.sequence(frontLeft, frontRight, backLeft, backRight);

    public void setCurrentStyle(DriveStyle currentStyle) {
        this.currentStyle = currentStyle;
    }

    public Drivetrain() {
    }

    public double currentLimit(double targetSpeed, double currentSpeed) {
        // negation of value with negated parameters
        if (currentSpeed < 0) {
            return -currentLimit(-targetSpeed, -currentSpeed);
        }

        // At this point speed >= 0
        if (targetSpeed > currentSpeed) { // Current limit accelerating
            targetSpeed = Math.min(targetSpeed, currentSpeed + forwardLimit.get());
        } else if (targetSpeed < 0) { // Current limit reversing direction
            double limitedDutyCycle = -reverseLimit.get() / (1.0 + currentSpeed); // speed >= 0 so dutyCycle < -currentLimit
            targetSpeed = Math.max(targetSpeed, limitedDutyCycle); // Both are negative
        }

        return targetSpeed;
    }

    @Override
    protected void updateEnabled() {
        DriveSpeed speeds = currentStyle.getSpeeds();

        // TODO: get rid of enums
        frontLeft.get().setDutyCycle(currentLimit(speeds.getFrontLeft(),driveEncoders.get().GetNormalizedSpeed(DriveEncoders.Side.LEFT_FRONT)));
        frontRight.get().setDutyCycle(currentLimit(speeds.getFrontRight(), driveEncoders.get().GetNormalizedSpeed(DriveEncoders.Side.RIGHT_FRONT)));
        backLeft.get().setDutyCycle(currentLimit(speeds.getBackLeft(), driveEncoders.get().GetNormalizedSpeed(DriveEncoders.Side.LEFT_BACK)));
        backRight.get().setDutyCycle(currentLimit(speeds.getBackRight(), driveEncoders.get().GetNormalizedSpeed(DriveEncoders.Side.RIGHT_BACK)));
    }

    @Override
    protected void updateDisabled() {
        allMotors.get().forEach(motor -> motor.setDutyCycle(0));
    }

    @Override
    protected void onEnabled() {

    }

    @Override
    protected void onDisabled() {

    }
}
