package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.CarriageHooksData;
import com.team846.frc2015.componentData.CollectorArmData;
import com.team846.frc2015.componentData.CollectorArmData.ArmPosition;
import com.team846.frc2015.componentData.CollectorRollersData.Direction;
import com.team846.frc2015.componentData.CollectorRollersData;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.componentData.CarriageHooksData.HookState;
import com.team846.frc2015.componentData.ElevatorData.ElevatorControlMode;
import com.team846.frc2015.componentData.ElevatorData.ElevatorSetpoint;
import com.team846.frc2015.oldconfig.ConfigPortMappings;
import com.team846.frc2015.oldconfig.DriverStationConfig;
import com.team846.frc2015.driverstation.LRTDriverStation;
import com.team846.frc2015.driverstation.LRTJoystick;
import com.team846.frc2015.sensors.SensorFactory;

import edu.wpi.first.wpilibj.AnalogInput;

public abstract class LoadItem extends Automation {

    private final ElevatorData elevatorData;
    private final CarriageHooksData hooksData;
    private final CollectorArmData armData;
    private final CollectorRollersData rollersData;

    private final ElevatorSetpoint collect;
    private final ElevatorSetpoint grab;
    private final ElevatorSetpoint home;

    private final LRTJoystick driverStick;
    private final LRTJoystick operatorStick;
    private final int requiredWaitCycles;
    private int waitTicks;

    boolean hasItem = false;

    private final AnalogInput sensor;

    private int analogThreshold = 0;

    private int ticksLeftForElevatorDown = 0;

    enum State {
        COLLECT,
        GRAB,
        ARMS,
        WAIT,
        HOME
    }

    State state;
    private final boolean auto;

    LoadItem(String name, ElevatorSetpoint collectSetpoint, ElevatorSetpoint grabSetpoint,
             ElevatorSetpoint homeSetpoint, boolean auto) {
        this(name, collectSetpoint, grabSetpoint, homeSetpoint, 20, auto);
    }

    LoadItem(String name, ElevatorSetpoint collectSetpoint, ElevatorSetpoint grabSetpoint,
             ElevatorSetpoint homeSetpoint, int waitCycles, boolean auto) {
        super(name);
        state = State.COLLECT;
        this.auto = auto;
        elevatorData = ElevatorData.get();
        hooksData = CarriageHooksData.get();
        armData = CollectorArmData.get();
        rollersData = CollectorRollersData.get();
        driverStick = LRTDriverStation.instance().getDriverStick();

        sensor = SensorFactory.getAnalogInput(ConfigPortMappings.Instance().get("Analog/COLLECTOR_PROXIMITY"));
        collect = collectSetpoint;
        grab = grabSetpoint;
        home = homeSetpoint;

        this.requiredWaitCycles = waitCycles;
        waitTicks = 0;
    }

    @Override
    public void AllocateResources() {
        AllocateResource(ControlResource.COLLECTOR_ARMS);
        AllocateResource(ControlResource.COLLECTOR_ROLLERS);
        AllocateResource(ControlResource.CARRIAGE_HOOKS);
        AllocateResource(ControlResource.ELEVATOR);
    }

    @Override
    protected boolean Start() {
        state = State.COLLECT;
        hasItem = false;
        waitTicks = 0;
        return true;
    }

    @Override
    protected boolean Abort() {
        elevatorData.setControlMode(ElevatorControlMode.VELOCITY);
        elevatorData.setDesiredSpeed(0.0);

        armData.setDesiredPosition(ArmPosition.STOWED);
        rollersData.setRunning(false);

        hooksData.setBackHooksDesiredState(HookState.DOWN);
        hooksData.setFrontHooksDesiredState(HookState.DOWN);
        return true;
    }

    @Override
    protected boolean Run() {
        armData.setDesiredPosition(ArmPosition.STOWED);
        rollersData.setRunning(false);

        boolean collectIsDown = operatorStick.isButtonDown(DriverStationConfig.JoystickButtons.COLLECT_OPERATOR)
                              || driverStick.isButtonDown(DriverStationConfig.JoystickButtons.COLLECT);

        boolean advStateIsDown = operatorStick.isButtonDown(DriverStationConfig.JoystickButtons.ADVANCE_STATE_OPERATOR)
                              || driverStick.isButtonDown(DriverStationConfig.JoystickButtons.ADVANCE_STATE);

        System.out.println("collectIsDown: " + collectIsDown);
        System.out.println("advStateIsDown: " + advStateIsDown);

        switch (state) {
            case COLLECT: {
                hasItem = false;
                hooksData.setBackHooksDesiredState(HookState.DOWN);
                hooksData.setFrontHooksDesiredState(HookState.DOWN);

                elevatorData.setControlMode(ElevatorControlMode.SETPOINT);
                elevatorData.setSetpoint(collect);

                // EXPERIMENTAL {
                if (auto) {
                    armData.setDesiredPosition(ArmPosition.EXTEND);
                    rollersData.setRunning(true);
                    rollersData.setDirection(Direction.INTAKE);
                    rollersData.setSpeed(1.0);
                }
                // }

                if (elevatorData.isAtSetpoint(collect)) {
                    System.out.println("AT ELEVATOR SETPOINT");
                    elevatorData.setControlMode(ElevatorControlMode.VELOCITY);
                    elevatorData.setDesiredSpeed(0.0);

                    if (collectIsDown || auto) {
                        armData.setDesiredPosition(ArmPosition.EXTEND);
                        rollersData.setRunning(true);
                        rollersData.setDirection(Direction.INTAKE);
                        rollersData.setSpeed(1.0);

                        System.out.println(sensor.getAverageValue());
                        if ((advStateIsDown && !auto)
                                || (auto && sensor.getAverageValue() > analogThreshold)) {
                            rollersData.setSpeed(1.0);
                            hasItem = true;


                            state = State.ARMS;
                            rollersData.setRunning(true);
                            rollersData.setDirection(Direction.INTAKE);
                            rollersData.setSpeed(1.0);
                            armData.setDesiredPosition(ArmPosition.STOWED);
                            ticksLeftForElevatorDown = 5; // arbitrary value for number of ticks
                        }
                    }
                }
                break;
            }

            case ARMS: {
                armData.setDesiredPosition(ArmPosition.STOWED);
                ticksLeftForElevatorDown--;

                if (ticksLeftForElevatorDown == 0) {
                    state = State.GRAB;
                }

                break;
            }

            case GRAB: {
                hooksData.setBackHooksDesiredState(HookState.UP);
                hooksData.setFrontHooksDesiredState(HookState.UP);
                elevatorData.setControlMode(ElevatorControlMode.SETPOINT);
                elevatorData.setSetpoint(grab);
                elevatorData.setFast(true);
                armData.setDesiredPosition(ArmPosition.STOWED);

                //AsyncPrinter.warn(elevatorData.getCurrentSetpoint().toString());
                if (elevatorData.isAtSetpoint(grab)) {
                    hooksData.setBackHooksDesiredState(HookState.DOWN);
                    hooksData.setFrontHooksDesiredState(HookState.DOWN);
                    state = State.WAIT;
                }

                break;
            }

            case WAIT: {
                rollersData.setRunning(true);
                rollersData.setDirection(Direction.INTAKE);
                rollersData.setSpeed(0.1);
                armData.setDesiredPosition(ArmPosition.STOWED);
                hooksData.setBackHooksDesiredState(HookState.DOWN);
                hooksData.setFrontHooksDesiredState(HookState.DOWN);
                elevatorData.setFast(false);
                if (waitTicks++ > requiredWaitCycles) {
                    state = State.HOME;
                }
                break;
            }
            case HOME: {
                rollersData.setRunning(true);
                rollersData.setDirection(Direction.INTAKE);
                rollersData.setSpeed(0.0);
                armData.setDesiredPosition(ArmPosition.STOWED);
                hooksData.setBackHooksDesiredState(HookState.DOWN);
                hooksData.setFrontHooksDesiredState(HookState.DOWN);
                elevatorData.setControlMode(ElevatorControlMode.SETPOINT);
                elevatorData.setSetpoint(home);
                if (elevatorData.isAtSetpoint(home)) {
                    elevatorData.setControlMode(ElevatorControlMode.VELOCITY);
                    elevatorData.setDesiredSpeed(0.0);
                    armData.setDesiredPosition(ArmPosition.EXTEND);

                    if (auto) {
                        armData.setDesiredPosition(ArmPosition.STOWED);
                        rollersData.setRunning(false);

                        hooksData.setBackHooksDesiredState(HookState.DOWN);
                        hooksData.setFrontHooksDesiredState(HookState.DOWN);
                        return true;
                    }

                }
                break;
            }
        }
//		System.out.println(state);
        return false;
    }

    void setAnalogThreshold(int a) {
        analogThreshold = a;
    }
}
