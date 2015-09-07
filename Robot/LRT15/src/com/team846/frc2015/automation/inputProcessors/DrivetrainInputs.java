package com.team846.frc2015.automation.inputProcessors;

import com.team846.frc2015.automation.ControlResource;
import com.team846.frc2015.componentData.DrivetrainData;
import com.team846.frc2015.componentData.DrivetrainData.ControlMode;
import com.team846.frc2015.oldconfig.Configurable;
import com.team846.frc2015.control.Pivot;
import com.team846.frc2015.driverstation.LRTDriverStation;
import com.team846.frc2015.driverstation.LRTJoystick;
import com.team846.frc2015.utils.MathUtils;
import com.team846.frc2015.control.UdpFetcher;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.AxisType;

public class DrivetrainInputs extends InputProcessor implements Configurable {

    private final LRTJoystick driverStick;
    private final LRTJoystick driverWheel;

    boolean lastStop;
    //	double negInertiaScalar;
//	double negInertiaAccumulator;
//	double lastTurn;
//	double stoppedForward;
//	double stoppedTurn;
    private boolean constRadius;
    private int blendExponent;
    private int turnExponent;
    private int constRadiusTurnExponent;
    private int throttleExponent;
    private double deadband;
    private double strafeExponent;

    private final Axis axis;
    private final DrivetrainData drivetrainData;

    private Pivot pivot = new Pivot(2);
    private UdpFetcher fetcher = new UdpFetcher("beaglebonehaha", 4000);


    public enum Axis {
        DRIVE,
        TURN,
        STRAFE
    }

    public DrivetrainInputs(Axis axis) {
        driverStick = LRTDriverStation.instance().getDriverStick();
        driverWheel = LRTDriverStation.instance().getDriverWheel();

        drivetrainData = DrivetrainData.get();
        this.axis = axis;
        if (axis == Axis.DRIVE)
            RegisterResource(ControlResource.DRIVE);
        else if (axis == Axis.TURN)
            RegisterResource(ControlResource.TURN);
        else
            RegisterResource(ControlResource.STRAFE);

    }

    public void Update() {
        double forward = -driverStick.getAxis(Joystick.AxisType.kY);

        int signForward = MathUtils.sign(forward);
        //System.out.println("fwd: " + forward);
        if (Math.abs(forward) < deadband)
            forward = 0.0;
        else {

            forward -= signForward * deadband;
            forward /= 1.0 - deadband;
        }

        if (axis == Axis.DRIVE) {
            drivetrainData.SetControlMode(DrivetrainData.Axis.FORWARD, ControlMode.OPEN_LOOP);
            drivetrainData.SetVelocitySetpoint(DrivetrainData.Axis.FORWARD, (float) (forward));
            drivetrainData.SetOpenLoopOutput(DrivetrainData.Axis.FORWARD, (float) (forward * 0.75));
        } else if (axis == Axis.TURN) {
            //drivetrain.setControlMode(Axis.TURN, ControlMode.OPEN_LOOP);
            double turn = 0.0;
            turn = driverWheel.getAxis(AxisType.kX);
            drivetrainData.SetOpenLoopOutput(DrivetrainData.Axis.TURN, (float) turn);
            //	turn = -m_driver_stick.GetAxis(Joystick.kZAxis);

            int sign = turn > 0 ? 1 : -1;

            if (constRadius) {
                if (constRadiusTurnExponent % 2 == 0)
                    turn = sign * Math.pow(turn, constRadiusTurnExponent);
                else
                    turn = Math.pow(turn, constRadiusTurnExponent);
            } else {
                if (turnExponent % 2 == 0)
                    turn = sign * Math.pow(turn, turnExponent);
                else
                    turn = Math.pow(turn, turnExponent);
            }

//			// Negative Inertia routine
//			double negInertia = turn - lastTurn;
//			lastTurn = turn;
//			
//			double negInertiaPower = negInertia * negInertiaScalar;
//			negInertiaAccumulator += negInertiaPower;
//			turn += negInertiaAccumulator;
//			
//			if (negInertiaAccumulator > 1) {
//				negInertiaAccumulator -= 1;
//			} else if (negInertiaAccumulator < -1) {
//				negInertiaAccumulator += 1;
//			} else {
//				negInertiaAccumulator = 0;
//			}

            // Blending routine
            double absForward = Math.abs(forward); // To ensure correct arc when switching direction

            double blend = Math.pow((1 - absForward), blendExponent); // Always between 0 and 1, raised to an exponent to adjust transition between in place and arc.

            double turnInPlace = turn; // Normal turn
            double constRadiusTurn = turn * absForward; // Arc turn

            double turnComposite;

//			if (constRadius && !driverWheel.IsButtonDown(DriverStationConfig.JoystickButtons.QUICK_TURN))
//				turnComposite = constRadiusTurn;
//			else
            turnComposite = turnInPlace * (blend) + constRadiusTurn * (1 - blend); // Blended function

            drivetrainData.SetControlMode(DrivetrainData.Axis.TURN, DrivetrainData.ControlMode.OPEN_LOOP);
            drivetrainData.SetVelocitySetpoint(DrivetrainData.Axis.TURN, (float) turnComposite);

        }
        if (axis == Axis.STRAFE) {
            double strafe = driverStick.getAxis(Joystick.AxisType.kX);

            drivetrainData.SetControlMode(DrivetrainData.Axis.STRAFE, DrivetrainData.ControlMode.OPEN_LOOP);
            drivetrainData.SetVelocitySetpoint(DrivetrainData.Axis.STRAFE, (float) strafe);
            drivetrainData.SetOpenLoopOutput(DrivetrainData.Axis.STRAFE, (float) strafe);
        }

//        if (driverWheel.IsButtonDown(DriverStationConfig.JoystickButtons.PIVOT)) {
//            double wheelRotation = driverWheel.getAxis(Joystick.AxisType.kX);
//            Pair<Double, Double> drivetrainValues = pivot.get(wheelRotation);
//            drivetrainData.SetVelocitySetpoint(DrivetrainData.Axis.TURN, drivetrainValues.getFirst());
//            drivetrainData.SetVelocitySetpoint(DrivetrainData.Axis.STRAFE, drivetrainValues.getSecond());
//        }

        if (false) { // TODO: Figure out a way to put the robot in vision mode
            String lastResponse = fetcher.getLastResponse();
            String[] split = lastResponse.split("\\s");
            double speedStrafe = Double.parseDouble(split[0]);
            double speedForward = Double.parseDouble(split[1]);
            drivetrainData.SetControlMode(DrivetrainData.Axis.FORWARD, ControlMode.VELOCITY_CONTROL);
            drivetrainData.SetControlMode(DrivetrainData.Axis.STRAFE, ControlMode.VELOCITY_CONTROL);
            drivetrainData.SetVelocitySetpoint(DrivetrainData.Axis.FORWARD, speedForward);
            drivetrainData.SetVelocitySetpoint(DrivetrainData.Axis.STRAFE, speedStrafe);
        }
    }

    public void configure() {
        blendExponent = GetConfig("blend_exponent", 1);
        turnExponent = GetConfig("turn_exponent", 2);
        constRadiusTurnExponent = GetConfig("const_radius_turn_exponent", 1);
        throttleExponent = GetConfig("throttle_exponent", 1);
        strafeExponent = GetConfig("strafeExponent", 1);
        deadband = GetConfig("deadband", 0.04);

        //negInertiaScalar = GetConfig("neg_inertia_scalar", 5.0);
    }

}
