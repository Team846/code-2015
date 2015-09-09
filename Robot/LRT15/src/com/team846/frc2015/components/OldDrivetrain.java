package com.team846.frc2015.components;

import java.util.Arrays;

import com.team846.frc2015.actuators.DriveESC;
import com.team846.frc2015.componentData.DrivetrainData;
import com.team846.frc2015.componentData.DrivetrainData.Axis;
import com.team846.frc2015.dashboard.DashboardLogger;
import com.team846.frc2015.oldconfig.ConfigPortMappings;
import com.team846.frc2015.oldconfig.ConfigRuntime;
import com.team846.frc2015.oldconfig.Configurable;
import com.team846.frc2015.oldconfig.RobotConfig;
import com.team846.frc2015.control.PID;
import com.team846.frc2015.sensors.DriveEncoders;
import com.team846.frc2015.logging.AsyncLogger;
import com.team846.frc2015.sensors.LRTGyro;
import com.team846.frc2015.utils.MathUtils;

import edu.wpi.first.wpilibj.CANTalon;

public class OldDrivetrain extends Component implements Configurable {
    public enum Side {
        FRONT_LEFT,
        FRONT_RIGHT,
        BACK_LEFT,
        BACK_RIGHT
    }

    private final static int POSITION = 0;
    private final static int VELOCITY = 1;

    private final static double ANGLE_CORRECTION_GAIN = 0.25;

    private final PID[][] PIDs;

    private final PID[] mecanumDrivePIDs;
    private final DrivetrainData drivetrainData;
    private final DriveEncoders driveEncoders;
    private final DriveESC[] escs = new DriveESC[4];

    private final CANTalon frontLeft;
    private final CANTalon backLeft;
    private final CANTalon frontRight;
    private final CANTalon backRight;

    LRTGyro gyro = LRTGyro.getInstance();

    public OldDrivetrain() {
        PIDs = new PID[2][4];

        for (PID[] row : PIDs)
            Arrays.fill(row, new PID());

        mecanumDrivePIDs = new PID[4];
        Arrays.fill(mecanumDrivePIDs, new PID());

        ConfigPortMappings portMapping = ConfigPortMappings.Instance(); //reduce verbosity

        frontLeft = new CANTalon(portMapping.get("CAN/DRIVE_FRONT_LEFT"));
        backLeft = new CANTalon(portMapping.get("CAN/DRIVE_BACK_LEFT"));

        frontRight = new CANTalon(portMapping.get("CAN/DRIVE_FRONT_RIGHT"));
        backRight = new CANTalon(portMapping.get("CAN/DRIVE_BACK_RIGHT"));

        driveEncoders = new DriveEncoders(frontLeft, frontRight, backLeft, backRight);

        escs[Side.FRONT_LEFT.ordinal()] = new DriveESC(frontLeft);
        escs[Side.FRONT_RIGHT.ordinal()] = new DriveESC(frontRight);
        escs[Side.BACK_LEFT.ordinal()] = new DriveESC(backLeft);
        escs[Side.BACK_RIGHT.ordinal()] = new DriveESC(backRight);

        drivetrainData = DrivetrainData.get();

        ConfigRuntime.Register(this);

        for (DriveESC esc : escs) {
            float current_limit = 0.50f;
            esc.setForwardCurrentLimit(current_limit);
            esc.setReverseCurrentLimit(current_limit);
        }
    }

    private double computeOutput(DrivetrainData.Axis axis) {
        double positionSetpoint = drivetrainData.GetPositionSetpoint(axis);
        double velocitySetpoint = drivetrainData.GetVelocitySetpoint(axis);

        double rawOutput = drivetrainData.GetOpenLoopOutput(axis);


        switch (drivetrainData.GetControlMode(axis)) {
            case POSITION_CONTROL:
                PIDs[POSITION][axis.ordinal()].setInput(axis == Axis.FORWARD ? driveEncoders.GetRobotDist() : driveEncoders.GetTurnAngle());
                PIDs[POSITION][axis.ordinal()].setSetpoint(positionSetpoint);
                velocitySetpoint += PIDs[POSITION][axis.ordinal()].update(1.0 / RobotConfig.LOOP_RATE);
                if (Math.abs(velocitySetpoint) > drivetrainData.GetPositionControlMaxSpeed(axis))
                    velocitySetpoint = MathUtils.sign(velocitySetpoint) * drivetrainData.GetPositionControlMaxSpeed(axis);
                AsyncLogger.warn("RAW PID OUTPUT: " + velocitySetpoint);
                rawOutput = velocitySetpoint;

                break;
            case VELOCITY_CONTROL:
                if (Math.abs(velocitySetpoint) < 2.0E-2)
                    PIDs[VELOCITY][axis.ordinal()].setIIREnabled(true);
                else
                    PIDs[VELOCITY][axis.ordinal()].setIIREnabled(false);

                if (axis == Axis.FORWARD)
                    PIDs[VELOCITY][axis.ordinal()].setInput(
                            driveEncoders.GetNormalizedForwardSpeed());
                else if (axis == Axis.TURN)
                    PIDs[VELOCITY][axis.ordinal()].setInput(
                            driveEncoders.GetNormalizedTurningSpeed());
                else
                    PIDs[VELOCITY][axis.ordinal()].setInput(
                            driveEncoders.GetNormalizedStrafingSpeed());

                PIDs[VELOCITY][axis.ordinal()].setSetpoint(velocitySetpoint);

                rawOutput = PIDs[VELOCITY][axis.ordinal()].update(1.0 / RobotConfig.LOOP_RATE);
                break;
            case OPEN_LOOP:
                break;
            default:
                AsyncLogger.warn("Invalid control mode for axis: " + axis);
                break;
        }

        System.out.println("raw output for" + axis + " = " + rawOutput);

        return rawOutput;
    }

    public void updateEnabled() {
        double leftFrontOutput;
        double rightFrontOutput;

        double leftBackOutput;
        double rightBackOutput;

        if (drivetrainData.getClassicDrive()) //hack to keep nice closed loop position/velocity on drive/turn axes, should fix later
        {
            double fwdOutput = computeOutput(Axis.FORWARD); //positive means forward
            double turnOutput = correctedAngularVelocity(computeOutput(Axis.TURN)); //positive means turning counter-clockwise. Matches the way driveEncoders work.

            leftFrontOutput = fwdOutput - turnOutput;
            rightFrontOutput = fwdOutput + turnOutput;

            leftBackOutput = leftFrontOutput;
            rightBackOutput = rightFrontOutput;
        } else {
            double fwdOutput = drivetrainData.GetOpenLoopOutput(Axis.FORWARD);
            double turnOutput = correctedAngularVelocity(drivetrainData.GetOpenLoopOutput(Axis.TURN));
            double strafeOutput = drivetrainData.GetOpenLoopOutput(Axis.STRAFE);

            double leftFrontRawOutput = fwdOutput + turnOutput + strafeOutput;
            double rightFrontRawOutput = fwdOutput - turnOutput - strafeOutput;
            double leftBackRawOutput = fwdOutput + turnOutput - strafeOutput;
            double rightBackRawOutput = fwdOutput - turnOutput + strafeOutput;

            leftFrontOutput = computeMecanumOutput(DriveEncoders.Side.LEFT_FRONT, leftFrontRawOutput);
            rightFrontOutput = computeMecanumOutput(DriveEncoders.Side.RIGHT_FRONT, rightFrontRawOutput);
            leftBackOutput = computeMecanumOutput(DriveEncoders.Side.LEFT_BACK, leftBackRawOutput);
            rightBackOutput = computeMecanumOutput(DriveEncoders.Side.RIGHT_BACK, rightBackRawOutput);
        }

        leftFrontOutput = MathUtils.clamp(leftFrontOutput, -1.0, 1.0);
        rightFrontOutput = MathUtils.clamp(rightFrontOutput, -1.0, 1.0);
        leftBackOutput = MathUtils.clamp(leftBackOutput, -1.0, 1.0);
        rightBackOutput = MathUtils.clamp(rightBackOutput, -1.0, 1.0);

        double frontLeftSpeed = driveEncoders.GetEncoder(DriveEncoders.Side.LEFT_FRONT).getRate() / DriveEncoders.GetMaxEncoderRate();
        double frontRightSpeed = driveEncoders.GetEncoder(DriveEncoders.Side.RIGHT_FRONT).getRate() / DriveEncoders.GetMaxEncoderRate();
        double backLeftSpeed = driveEncoders.GetEncoder(DriveEncoders.Side.LEFT_BACK).getRate() / DriveEncoders.GetMaxEncoderRate();
        double backRightSpeed = driveEncoders.GetEncoder(DriveEncoders.Side.RIGHT_BACK).getRate() / DriveEncoders.GetMaxEncoderRate();

        double currentLimitedFrontLeft = escs[Side.FRONT_LEFT.ordinal()].currentLimit(leftFrontOutput, frontLeftSpeed);
        double currentLimitedFrontRight = escs[Side.FRONT_RIGHT.ordinal()].currentLimit(rightFrontOutput, frontRightSpeed);
        double currentLimitedBackLeft = escs[Side.BACK_LEFT.ordinal()].currentLimit(leftBackOutput, backLeftSpeed);
        double currentLimitedBackRight = escs[Side.BACK_RIGHT.ordinal()].currentLimit(rightBackOutput, backRightSpeed);

        escs[Side.FRONT_LEFT.ordinal()].setDutyCycle(currentLimitedFrontLeft);
        escs[Side.FRONT_RIGHT.ordinal()].setDutyCycle(currentLimitedFrontRight);

        escs[Side.BACK_LEFT.ordinal()].setDutyCycle(currentLimitedBackLeft);
        escs[Side.BACK_RIGHT.ordinal()].setDutyCycle(currentLimitedBackRight);

        DashboardLogger.getInstance().logDouble("drivetrain-leftFront", driveEncoders.GetEncoder(DriveEncoders.Side.LEFT_FRONT).getRate());
        DashboardLogger.getInstance().logDouble("drivetrain-leftBack", driveEncoders.GetEncoder(DriveEncoders.Side.LEFT_BACK).getRate());
        DashboardLogger.getInstance().logDouble("drivetrain-rightFront", driveEncoders.GetEncoder(DriveEncoders.Side.RIGHT_FRONT).getRate());
        DashboardLogger.getInstance().logDouble("drivetrain-rightBack", driveEncoders.GetEncoder(DriveEncoders.Side.RIGHT_BACK).getRate());

        frontLeft.enableBrakeMode(false);
        backLeft.enableBrakeMode(false);
        frontRight.enableBrakeMode(false);
        backRight.enableBrakeMode(false);
    }

    int tick = 0;

    public void updateDisabled() {
        escs[Side.FRONT_LEFT.ordinal()].setDutyCycle(0.0);
        escs[Side.BACK_LEFT.ordinal()].setDutyCycle(0.0);

        escs[Side.FRONT_RIGHT.ordinal()].setDutyCycle(0.0);
        escs[Side.BACK_RIGHT.ordinal()].setDutyCycle(0.0);

        frontLeft.enableBrakeMode(false);
        backLeft.enableBrakeMode(false);
        frontRight.enableBrakeMode(false);
        backRight.enableBrakeMode(false);

        DashboardLogger.getInstance().logDouble("drivetrain-leftFront", driveEncoders.GetEncoder(DriveEncoders.Side.LEFT_FRONT).getRate());
        DashboardLogger.getInstance().logDouble("drivetrain-leftBack", driveEncoders.GetEncoder(DriveEncoders.Side.LEFT_BACK).getRate());
        DashboardLogger.getInstance().logDouble("drivetrain-rightFront", driveEncoders.GetEncoder(DriveEncoders.Side.RIGHT_FRONT).getRate());
        DashboardLogger.getInstance().logDouble("drivetrain-rightBack", driveEncoders.GetEncoder(DriveEncoders.Side.RIGHT_BACK).getRate());

        tick++;
    }

    public void onEnabled() {
    }

    public void onDisabled() {
    }

    public void configure() {
//		Kv = GetConfig("Kv", 1.0);

        configurePIDObject(PIDs[VELOCITY][Axis.TURN.ordinal()], "velocity_turn", true);
        configurePIDObject(PIDs[VELOCITY][Axis.FORWARD.ordinal()], "velocity_fwd", true);

        configurePIDObject(PIDs[POSITION][Axis.TURN.ordinal()], "position_turn", false);
        configurePIDObject(PIDs[POSITION][Axis.FORWARD.ordinal()], "position_fwd", false);

        configurePIDObject(mecanumDrivePIDs[Side.BACK_LEFT.ordinal()], "mecanum", true);
        configurePIDObject(mecanumDrivePIDs[Side.BACK_RIGHT.ordinal()], "mecanum", true);
        configurePIDObject(mecanumDrivePIDs[Side.FRONT_LEFT.ordinal()], "mecanum", true);
        configurePIDObject(mecanumDrivePIDs[Side.FRONT_RIGHT.ordinal()], "mecanum", true);

//		ConfigureForwardCurrentLimit();
//		ConfigureReverseCurrentLimit();
    }

    private void configurePIDObject(PID pid, String pidName, boolean feedForward) {
        double p = GetConfig(pidName + "_P", 1.0);
        double i = GetConfig(pidName + "_I", 0.0);
        double d = GetConfig(pidName + "_D", 0.0);

        pid.setParameters(p, i, d, 1.0, 0.87, feedForward, 0.0);
    }

    private double computeMecanumOutput(DriveEncoders.Side wheel, double desiredOutput) {
        mecanumDrivePIDs[wheel.ordinal()].setSetpoint(desiredOutput);
        mecanumDrivePIDs[wheel.ordinal()].setInput(driveEncoders.GetNormalizedSpeed(wheel));
        return mecanumDrivePIDs[wheel.ordinal()].update(1.0 / RobotConfig.LOOP_RATE);
    }


    private double correctedAngularVelocity(double targetAngularVelocity) {
        double differenceFromTarget = targetAngularVelocity - getRobotAngularVelocity();

        return targetAngularVelocity + (ANGLE_CORRECTION_GAIN * differenceFromTarget);
    }

    private double getRobotAngularVelocity() {
        double normalizedGyroVelocity = -gyro.getVel() / driveEncoders.GetMaxTurnRate();

        return -(.6 * normalizedGyroVelocity + .4 * driveEncoders.GetNormalizedTurningSpeed());
    }
//	void ConfigureForwardCurrentLimit()
//	{
//		escs[LEFT].setForwardCurrentLimit(GetConfig("forwardCurrentLimit", 50.0 / 100.0));
//		escs[RIGHT].setForwardCurrentLimit(GetConfig("forwardCurrentLimit", 50.0 / 100.0));
//	}
//
//	void ConfigureReverseCurrentLimit()
//	{
//		escs[LEFT].setReverseCurrentLimit(GetConfig("reverseCurrentLimit", 50.0 / 100.0));
//		escs[RIGHT].setReverseCurrentLimit(GetConfig("reverseCurrentLimit", 50.0 / 100.0));
//	}
}
