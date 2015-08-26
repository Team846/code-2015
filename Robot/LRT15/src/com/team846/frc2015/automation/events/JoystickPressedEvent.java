package com.team846.frc2015.automation.events;

import com.team846.frc2015.automation.events.Event;
import com.team846.frc2015.driverstation.LRTJoystick;

public class JoystickPressedEvent extends Event {
	private final LRTJoystick m_joystick;
    private final int m_button;
    private final int m_lastFiredButton;
	
	public JoystickPressedEvent(LRTJoystick joystick, int button) {
		m_joystick = joystick;

        m_button = button;	
        m_lastFiredButton = button;
	}
	
	public JoystickPressedEvent(LRTJoystick joy) {
		this(joy, 0);
	}
	
	public int GetButton() {
		return m_lastFiredButton;
	}
	
	public LRTJoystick GetJoystick() {
		return m_joystick;
	}

	public boolean CheckCondition() {
		if (m_button == 0)	
        {	
                for (int i = 1; i <= m_joystick.getNumButtons(); i++)	
                {	
                        if (m_joystick.isButtonDown(i))	
                        {	
                                return true;	
                        }	
                }	
        }	
        else if (m_joystick.isButtonDown(m_button))	
        {	
                return true;	
        }	
		
        return false;
	}
}
