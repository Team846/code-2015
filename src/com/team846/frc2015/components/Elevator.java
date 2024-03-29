package com.team846.frc2015.components;

import java.util.Arrays;

import com.team846.frc2015.componentData.CarriageExtenderData;
import com.team846.frc2015.componentData.CollectorArmData;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.componentData.ElevatorData.ElevatorControlMode;
import com.team846.frc2015.componentData.ElevatorData.ElevatorSetpoint;
import com.team846.frc2015.components.stackSecurity.StackSecurity;
import com.team846.frc2015.logging.Logger;
import com.team846.frc2015.oldconfig.ConfigPortMappings;
import com.team846.frc2015.oldconfig.ConfigRuntime;
import com.team846.frc2015.oldconfig.Configurable;
import com.team846.frc2015.control.RunningSum;
import com.team846.frc2015.dashboard.DashboardLogger;
import com.team846.frc2015.sensors.SensorFactory;
import com.team846.frc2015.logging.AsyncLogger;

import com.team846.frc2015.utils.MathUtils;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Timer;

public class Elevator extends Component implements Configurable {

    private final ElevatorData elevatorData;
    private RunningSum positionSum = new RunningSum(RunningSum.IIR_DECAY(5.0));
    double currentPosition = 0.0;
    private RunningSum velocitySum = new RunningSum(RunningSum.IIR_DECAY(5.0));

    private int topSoftLimit;
    private int bottomSoftLimit;

    private final CANTalon motorA;
    private final CANTalon motorB;

    private final AnalogInput elevatorPot;

    private final int[] elevatorSetpoints;

    private int errorThreshold;
    private boolean freezePosition;

    private double positionGain;
    private int collectorOutThreshold;
    private final CollectorArmData armData;
    private final CarriageExtenderData extenderData;

    private final Timer stallTimer = new Timer();
    private int stallPosition = 0;

    private double positionError = 0;

    private int atPositionCounter = 0;

    private ElevatorData.ElevatorControlMode lastMode;
    private ElevatorData.ElevatorSetpoint lastSetpoint;
    private boolean direction; // up true, down false

    private int currentTick = 0;


    public Elevator() {

        motorA = new CANTalon(
                ConfigPortMappings.Instance().get("CAN/ELEVATOR_MOTOR_A"));
        motorB = new CANTalon(
                ConfigPortMappings.Instance().get("CAN/ELEVATOR_MOTOR_B"));

        elevatorPot = SensorFactory.getAnalogInput(
                ConfigPortMappings.Instance().get("Analog/ELEVATOR_POT"));

        elevatorData = ElevatorData.get();
        armData = CollectorArmData.get();
        extenderData = CarriageExtenderData.get();

        positionGain = 0;

        elevatorSetpoints = new int[ElevatorData.ElevatorSetpoint.values().length];
        Arrays.fill(elevatorSetpoints, 0);

//		motorA.setVoltageRampRate(100.0);
//		motorB.setVoltageRampRate(100.0);

        ConfigRuntime.Register(this);
    }

    private double deadbandError(double error, int deadband) {
        if (error < 0)
            return -deadbandError(-error, deadband);

        deadband = deadband/2;
        if (error > deadband)
            return error - deadband;

        return 0;
    }

    @Override
    protected void updateEnabled() {
        currentTick++;
        double lastPosition = currentPosition;

        {
            int rawPosition = elevatorPot.getAverageValue();
            currentPosition = positionSum.UpdateSum(rawPosition);
//            System.out.println("RAW POSITION: " + rawPosition + " FILTERED: " + currentPosition);
        }

        System.out.println(currentPosition);

        double velocity = currentPosition - lastPosition;
        velocity = velocitySum.UpdateSum(velocity);
        elevatorData.velocity = velocity;

//        System.out.println("ELEVATOR VELOCITY: " + velocity);

        Logger.warning("Current Pos: " + currentPosition);
        elevatorData.setCurrentPosition(currentPosition);

        DashboardLogger.getInstance().logDouble("elevator-pot", currentPosition);
//		AsyncPrinter.println("Position: " + currentPosition);

        if (elevatorData.getControlMode() == ElevatorControlMode.VELOCITY) {
            sendOutput(elevatorData.getSpeed());
        } else if (elevatorData.getControlMode() == ElevatorControlMode.POSITION
                || elevatorData.getControlMode() == ElevatorControlMode.SETPOINT) {
            double desiredPos = elevatorData.getDesiredPosition();
            if (elevatorData.getControlMode() == ElevatorControlMode.SETPOINT) {
                if (elevatorData.getDesiredSetpoint() == ElevatorSetpoint.NONE) {
                    sendOutput(0.0);
                    return;
                }
//				AsyncPrinter.println("Setpoint: " + elevatorData.getDesiredSetpoint().toString());
                desiredPos = elevatorSetpoints[elevatorData.getDesiredSetpoint().ordinal()];
//				AsyncPrinter.println("Setpoint: " + desiredPos);


                if (elevatorData.getFast()) { // bang-bang control
                    if (!(lastMode == ElevatorControlMode.SETPOINT &&
                          lastSetpoint == elevatorData.getDesiredSetpoint())) { // just got new setpoint
                        direction = currentPosition > desiredPos; // previously (desiredPos - currentPosition > 0) ? false : true
                    }
                }
            }

            if (desiredPos <= topSoftLimit || desiredPos >= bottomSoftLimit) {
                System.out.println("DESIRED: " + desiredPos + " TOP LIMIT: " + topSoftLimit + " BOTTOM LIMIT: " + bottomSoftLimit);
                AsyncLogger.error("Setpoint out of bounds");
                sendOutput(0.0);
                return;
            }

            positionError = desiredPos - currentPosition;
//            System.out.println("POSITION ERROR: " + positionError);
            positionError = deadbandError(positionError, errorThreshold);

            double speed;
            if (elevatorData.getFast()) {
                // UNTESTED
                if (positionError == 0) {
                    speed = 0; // NEW CODE
                } else {
                    speed = positionError > 0 ? 1.0 : -1.0;
                }
            } else {
                double minSpeed = 0.2;

                speed = positionError * positionGain;
                if (speed > 0) {
                    speed = MathUtils.rescale(speed, 0.0, 1.0, minSpeed, 1.0);
                } else if (speed < 0) {
                    speed = -MathUtils.rescale(-speed, 0.0, 1.0, minSpeed, 1.0);
                }
            }

//            System.out.println("CLIPPED ERROR: " + positionError + " SPEED OUTPUT: " + speed);

//            System.out.println("IS FAST: " + elevatorData.getFast());
            if (!elevatorData.getFast()) { // proportional control
                // TODO: fix speed check hack
                boolean readyForCountdown =  Math.abs(velocity) < 10.0 &&
                        Math.abs(positionError) < 3 * errorThreshold;
//                System.out.println("AT POSITION: " + readyForCountdown);
                if (readyForCountdown && elevatorData.getControlMode() == ElevatorControlMode.SETPOINT) {
//                    System.out.println("COUNTDOWN: " + atPositionCounter);
                    if (--atPositionCounter <= 0)
                        elevatorData.setCurrentPosition(elevatorData.getDesiredSetpoint());
                    else
                        elevatorData.setCurrentPosition(ElevatorSetpoint.NONE);
                } else {
//                    System.out.println("RESETTING COUNTDOWN " + "CONTROL MODE: " + elevatorData.getControlMode());
                    atPositionCounter = 2;
                    elevatorData.setCurrentPosition(ElevatorSetpoint.NONE);
                }
            } else {
                if (direction) { // up
                    if (currentPosition < desiredPos)
                        elevatorData.setCurrentPosition(elevatorData.getDesiredSetpoint());
                    else
                        elevatorData.setCurrentPosition(ElevatorSetpoint.NONE);
                } else { // down
                    if (currentPosition > desiredPos)
                        elevatorData.setCurrentPosition(elevatorData.getDesiredSetpoint());
                    else
                        elevatorData.setCurrentPosition(ElevatorSetpoint.NONE);
                }
            }

            sendOutput(speed);
            lastSetpoint = elevatorData.getDesiredSetpoint();
        }
        lastMode = elevatorData.getControlMode();
    }

    private void sendOutput(double value) {
        int currentPosition = elevatorPot.getAverageValue();
        if (currentPosition <= topSoftLimit) {
            if (value < 0)
                value = 0;
        } else if (currentPosition >= bottomSoftLimit) {
            if (value > 0)
                value = 0;
        }
//		else if(currentPosition >= collectorOutThreshold && 
//				Math.abs(extenderData.getCurrentPosition()) > 0.1 &&
//				armData.getCurrentPosition() == ArmPosition.EXTEND)
//		{
//				if (value > 0)
//					value = 0;
//		}
        if (value > 0.1) {
            stallTimer.start();
            if (stallTimer.get() > 3.0 && Math.abs(currentPosition - stallPosition) < 100) {
                value = 0;
                AsyncLogger.error("Elevator stalling (timer: " + stallTimer.get() + ", output: " + value);
            }
        } else {
            stallTimer.stop();
            stallTimer.reset();
            stallPosition = currentPosition;
        }

        if (StackSecurity.currentInstance == null || StackSecurity.currentInstance.getCurrentStyle().isSecurityDown()) {
            value = 0;
        }

        if (value == 0 && currentTick % 6 == 0) {
            motorA.enableBrakeMode(true);
            motorA.enableBrakeMode(true);
        } else {
            motorA.enableBrakeMode(false);
            motorB.enableBrakeMode(false);
        }

        motorA.set(value);
        motorB.set(value);
    }

    @Override
    protected void updateDisabled() {
        System.out.println("ELEVATOR POSITION: " + elevatorPot.getAverageValue());
//        DashboardLogger.getInstance().logInt("drivetrain-leftFront", elevatorPot.getAverageValue());
        motorA.set(0.0);
        motorB.set(0.0);
        motorA.enableBrakeMode(false);
        motorB.enableBrakeMode(false);
    }

    @Override
    protected void onEnabled() {

    }

    @Override
    protected void onDisabled() {
    }

    @Override
    public void configure() {
        positionGain = GetConfig("positionGain", 0.01);

        errorThreshold = GetConfig("errorThreshold", 15);
        elevatorData.errorThreshold = errorThreshold;

        // Setpoints
        topSoftLimit = GetConfig("topLimit", 100);

        // Everything is offset from topSoftLimit
        bottomSoftLimit = topSoftLimit + GetConfig("bottomLimit", 10);

        collectorOutThreshold = topSoftLimit + GetConfig("collectorInterlock", 2000);

        elevatorSetpoints[ElevatorSetpoint.STEP.ordinal()] = topSoftLimit + GetConfig("step", 1800);
        elevatorSetpoints[ElevatorSetpoint.TOTE_1.ordinal()] = topSoftLimit + GetConfig("tote1", 50);
        elevatorSetpoints[ElevatorSetpoint.TOTE_2.ordinal()] = topSoftLimit + GetConfig("tote2", 60);
        elevatorSetpoints[ElevatorSetpoint.TOTE_3.ordinal()] = topSoftLimit + GetConfig("tote3", 80);
        elevatorSetpoints[ElevatorSetpoint.TOTE_4.ordinal()] = topSoftLimit + GetConfig("tote4", 80);

        elevatorSetpoints[ElevatorSetpoint.HOME_TOTE.ordinal()] = topSoftLimit + GetConfig("home_tote", 2000);
        elevatorSetpoints[ElevatorSetpoint.HOME_UPRIGHT_CONTAINER.ordinal()] = topSoftLimit + GetConfig("home_upright_container", 2000);
        elevatorSetpoints[ElevatorSetpoint.HOME_SIDEWAYS_CONTAINER.ordinal()] = topSoftLimit + GetConfig("home_sideways_container", 2200);

        elevatorSetpoints[ElevatorSetpoint.COLLECT_TOTE.ordinal()] = topSoftLimit + GetConfig("collect_tote", 2000);
        elevatorSetpoints[ElevatorSetpoint.COLLECT_UPRIGHT_CONTAINER.ordinal()] = topSoftLimit + GetConfig("collect_upright_container", 2050);
        elevatorSetpoints[ElevatorSetpoint.COLLECT_SIDEWAYS_CONTAINER.ordinal()] = topSoftLimit + GetConfig("collect_sideways_container", 1800);
        elevatorSetpoints[ElevatorSetpoint.COLLECT_ADDITIONAL.ordinal()] = topSoftLimit + GetConfig("collect_additional", 1600);

        elevatorSetpoints[ElevatorSetpoint.GRAB_TOTE.ordinal()] = topSoftLimit + GetConfig("grab_tote", 2100);
        elevatorSetpoints[ElevatorSetpoint.GRAB_SIDEWAYS_CONTAINER.ordinal()] = topSoftLimit + GetConfig("grab_sideways_container", 2400);

        elevatorSetpoints[ElevatorSetpoint.HUMAN_LOAD_PREPARE.ordinal()] = topSoftLimit + GetConfig("human_load_prepare", 1200);
        elevatorSetpoints[ElevatorSetpoint.HUMAN_LOAD_GRAB.ordinal()] = topSoftLimit + GetConfig("human_load_grab", 1800);

        elevatorSetpoints[ElevatorSetpoint.SWEEP_CONTAINER.ordinal()] = topSoftLimit + GetConfig("sweep_container", 1700);
    }


}
