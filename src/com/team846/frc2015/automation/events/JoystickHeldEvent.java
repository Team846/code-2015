package com.team846.frc2015.automation.events;

import com.team846.frc2015.automation.events.DelayedEvent;
import com.team846.frc2015.automation.events.JoystickPressedEvent;
import com.team846.frc2015.driverstation.LRTJoystick;

//TODO: evaluate if needed
class JoystickHeldEvent extends DelayedEvent {
    private final LRTJoystick m_joystick;
    private final int m_lastFiredButton;


    JoystickHeldEvent(LRTJoystick joystick, int button, int cycles) {
        super(new JoystickPressedEvent(joystick, button), cycles);

        m_joystick = joystick;
        m_lastFiredButton = button;
    }

    //TODO: evaluate if needed
    public int getButton() {
        return m_lastFiredButton;
    }

    public LRTJoystick getJoystick() {
        return m_joystick;
    }
}