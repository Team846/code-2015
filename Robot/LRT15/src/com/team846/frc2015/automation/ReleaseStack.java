package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.CarriageExtenderData;
import com.team846.frc2015.componentData.CarriageHooksData;
import com.team846.frc2015.componentData.CollectorArmData;
import com.team846.frc2015.componentData.CollectorArmData.ArmPosition;
import com.team846.frc2015.componentData.CollectorRollersData;
import com.team846.frc2015.componentData.CollectorRollersData.Direction;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.componentData.CarriageHooksData.HookState;
import com.team846.frc2015.componentData.ElevatorData.ElevatorControlMode;
import com.team846.frc2015.componentData.ElevatorData.ElevatorSetpoint;
import com.team846.frc2015.oldconfig.ConfigRuntime;
import com.team846.frc2015.oldconfig.Configurable;

public class ReleaseStack extends Automation implements Configurable {

    private final ElevatorData elevatorData;
    private double startingPosition;
    private int dropHeight;
    private final CarriageHooksData hooksData;
    private final CarriageExtenderData extenderData;
    private final CollectorArmData collectorArmData;
    private final CollectorRollersData collectorRollersData;
    private boolean elevatorToHome;
    private boolean spit;

    public ReleaseStack(boolean spit) {
        super(RoutineOption.REQUIRES_ABORT_CYCLES);

        elevatorData = ElevatorData.get();
        hooksData = CarriageHooksData.get();
        extenderData = CarriageExtenderData.get();
        collectorArmData = CollectorArmData.get();
        collectorRollersData = CollectorRollersData.get();

        dropHeight = 0;
        elevatorToHome = false;

        this.spit = spit;

        ConfigRuntime.Register(this);
    }

    public ReleaseStack() {
        this(false);
    }

    @Override
    public void AllocateResources() {
        AllocateResource(ControlResource.ELEVATOR);
        AllocateResource(ControlResource.CARRIAGE_EXTENDER);
        AllocateResource(ControlResource.CARRIAGE_HOOKS);
        AllocateResource(ControlResource.COLLECTOR_ARMS);
        AllocateResource(ControlResource.COLLECTOR_ROLLERS);
    }

    @Override
    protected boolean Start() {
        startingPosition = elevatorData.getCurrentPosition();
        elevatorToHome = false;
        return true;
    }

    @Override
    protected boolean Abort() {
        return true;
    }



    @Override
    protected boolean Run() {
        if (!spit)
            collectorArmData.setDesiredPosition(ArmPosition.STOWED);
        else
            collectorArmData.setDesiredPosition(ArmPosition.EXTEND);

        elevatorData.setControlMode(ElevatorControlMode.POSITION);
        double targetPosition = startingPosition + dropHeight;
        elevatorData.setDesiredPosition(targetPosition); // down is positive
        System.out.println("ELEVATOR POSITION" + elevatorData.getCurrentPosition() + "TARGET: " + targetPosition);
        System.out.println("READY FOR RELEASE: " + elevatorData.isAtPosition(targetPosition));
        if (elevatorData.isAtPosition(targetPosition) || elevatorToHome) {
            System.out.println("REACHED GOAL FOR RELEASE");
            elevatorToHome = true;
            hooksData.setBackHooksDesiredState(HookState.UP);
            hooksData.setFrontHooksDesiredState(HookState.UP);

            extenderData.setControlMode(CarriageExtenderData.CarriageControlMode.POSITION);
            extenderData.setPositionSetpoint(0.0);

            if (spit) {
                collectorArmData.setDesiredPosition(ArmPosition.EXTEND);
                collectorRollersData.setDirection(Direction.REVERSE);
                collectorRollersData.setRunning(true);
                collectorRollersData.setSpeed(1.0);
            }
        }
        if (Aborting()) {
            elevatorData.setControlMode(ElevatorControlMode.SETPOINT);
            elevatorData.setSetpoint(ElevatorSetpoint.HOME_TOTE);
            hooksData.setBackHooksDesiredState(HookState.DOWN);
            hooksData.setFrontHooksDesiredState(HookState.DOWN);
            collectorArmData.setDesiredPosition(ArmPosition.STOWED);
            if (elevatorData.isAtSetpoint(ElevatorSetpoint.HOME_TOTE)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void configure() {
        dropHeight = GetConfig("dropHeight", 400);
    }

}
