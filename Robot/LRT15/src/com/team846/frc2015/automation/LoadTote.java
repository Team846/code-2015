package com.team846.frc2015.automation;

import com.team846.frc2015.automation.events.JoystickReleasedEvent;
import com.team846.frc2015.componentData.ElevatorData.ElevatorSetpoint;
import com.team846.frc2015.oldconfig.ConfigRuntime;
import com.team846.frc2015.oldconfig.Configurable;
import com.team846.frc2015.oldconfig.DriverStationConfig;
import com.team846.frc2015.driverstation.LRTDriverStation;

public class LoadTote extends LoadItem implements Configurable {

    private int toteAnalogValue = 0;

    public LoadTote(boolean auto, ElevatorSetpoint homeLocation) {
        super("LoadTote", ElevatorSetpoint.COLLECT_TOTE, ElevatorSetpoint.GRAB_TOTE, homeLocation, 0, auto);
        ConfigRuntime.Register(this);
    }

    public LoadTote(boolean auto) {
        super("LoadTote", ElevatorSetpoint.COLLECT_TOTE, ElevatorSetpoint.GRAB_TOTE, ElevatorSetpoint.HOME_TOTE, 0, auto);
        ConfigRuntime.Register(this);
    }

    public LoadTote() {
        this(false);
    }

    @Override
    public void configure() {
        toteAnalogValue = GetConfig("analog_tote_value", 1600);
        super.setAnalogThreshold(toteAnalogValue);
    }

    @Override
    protected boolean Abort() {
        if (hasItem && (GetAbortEvent() instanceof JoystickReleasedEvent)
                && ((JoystickReleasedEvent) GetAbortEvent()).GetButton() == DriverStationConfig.JoystickButtons.LOAD_TOTE
                && ((JoystickReleasedEvent) GetAbortEvent()).GetJoystick() == LRTDriverStation.instance().getOperatorStick())
            return false;
        else
            return super.Abort();
    }
}
