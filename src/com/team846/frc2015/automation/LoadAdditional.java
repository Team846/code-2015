package com.team846.frc2015.automation;

import com.team846.frc2015.automation.events.JoystickReleasedEvent;
import com.team846.frc2015.componentData.CarriageHooksData;
import com.team846.frc2015.componentData.CollectorArmData;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.componentData.CarriageHooksData.HookState;
import com.team846.frc2015.componentData.CollectorArmData.ArmPosition;
import com.team846.frc2015.componentData.ElevatorData.ElevatorSetpoint;
import com.team846.frc2015.oldconfig.ConfigRuntime;
import com.team846.frc2015.oldconfig.Configurable;
import com.team846.frc2015.oldconfig.DriverStationConfig;
import com.team846.frc2015.driverstation.LRTDriverStation;

public class LoadAdditional extends LoadItem implements Configurable {

    final CarriageHooksData hooksData;
    final ElevatorData elevatorData;
    private final CollectorArmData armData;
    private int toteAnalogValue = 0;
    private double startPosition = 0.0;
    private int hookDisengageDrop;
    private boolean skipPickup  = false;
    private final static int waitDuration = 10; // wait before moving elevator from bottom to let hooks actuate

    public LoadAdditional(boolean auto, boolean skipPickup, ElevatorSetpoint homeSetpoint) {
        super("LoadAdditional", ElevatorSetpoint.COLLECT_ADDITIONAL, ElevatorSetpoint.GRAB_TOTE, homeSetpoint, waitDuration, auto);
        hooksData = CarriageHooksData.get();
        elevatorData = ElevatorData.get();
        armData = CollectorArmData.get();
        this.skipPickup = skipPickup;
        ConfigRuntime.Register(this);
    }

    public LoadAdditional(boolean auto, boolean skipPickup) {
        super("LoadAdditional", ElevatorSetpoint.COLLECT_ADDITIONAL, ElevatorSetpoint.GRAB_TOTE, /*ElevatorSetpoint.COLLECT_ADDITIONAL*/ ElevatorSetpoint.HOME_TOTE, waitDuration, auto);
        hooksData = CarriageHooksData.get();
        elevatorData = ElevatorData.get();
        armData = CollectorArmData.get();
        this.skipPickup = skipPickup;
        ConfigRuntime.Register(this);
    }

    public LoadAdditional(boolean auto, ElevatorSetpoint homeSetpoint) {
        this(auto, false, homeSetpoint);
    }

    public LoadAdditional(boolean auto) {
        this(auto, false);
    }

    public LoadAdditional() {
        this(false, false);
    }

    @Override
    public void configure() {
        toteAnalogValue = GetConfig("analog_tote_value", 1600);
        hookDisengageDrop = 200;//GetConfig("hook_disengage_drop", 500); // TODO
        super.setAnalogThreshold(toteAnalogValue);
    }

    protected boolean Start() {
        startPosition = elevatorData.getCurrentPosition();
        return super.Start();
    }

    @Override
    protected boolean Abort() {
        if (hasItem && (GetAbortEvent() instanceof JoystickReleasedEvent)
                && ((JoystickReleasedEvent) GetAbortEvent()).GetButton() == DriverStationConfig.JoystickButtons.LOAD_ADDITIONAL
                && ((JoystickReleasedEvent) GetAbortEvent()).GetJoystick() == LRTDriverStation.instance().getOperatorStick())
            return false;
        else
            return super.Abort();
    }

    @Override
    public boolean Run() {
        boolean ret = super.Run();
        if (elevatorData.isAtSetpoint(ElevatorSetpoint.COLLECT_ADDITIONAL)) {
            startPosition = elevatorData.getCurrentPosition();
        }
        if (state == State.GRAB) {
            if (skipPickup) {
                armData.setDesiredPosition(ArmPosition.EXTEND);
                return true;
            }
            if (elevatorData.getCurrentPosition() > startPosition + hookDisengageDrop) {
//                hooksData.setBackHooksDesiredState(HookState.UP);
//                hooksData.setFrontHooksDesiredState(HookState.UP);
                armData.setDesiredPosition(ArmPosition.STOWED);
            } else {
                hooksData.setBackHooksDesiredState(HookState.DOWN);
                hooksData.setFrontHooksDesiredState(HookState.DOWN);
            }
        }
        return ret;
    }
}
