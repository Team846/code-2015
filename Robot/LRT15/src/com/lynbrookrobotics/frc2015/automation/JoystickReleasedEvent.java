package com.lynbrookrobotics.frc2015.automation;

import com.lynbrookrobotics.frc2015.automation.Event;
import com.lynbrookrobotics.frc2015.driverstation.LRTJoystick;

public class JoystickReleasedEvent extends Event {
	private LRTJoystick m_joystick;
    private int m_button;
    private int m_lastFiredButton;
	
	JoystickReleasedEvent(LRTJoystick joystick, int button) {
		m_joystick = joystick;
        m_button = button;
        m_lastFiredButton = button;
	}
	
	public boolean CheckCondition() {
	if (m_button == 0)
        {	
                for (int i = 1; i <= m_joystick.GetNumButtons(); i++)	
                {	
                        if (!m_joystick.IsButtonDown(i))	
                        {	
                                return true;	
                        }	
                }	
        }	
        else if (!m_joystick.IsButtonDown(m_button))	
        {	
                return true;	
        }	
        return false;
	}
	
	public int GetButton() {
		return m_lastFiredButton;
	}
	
	public LRTJoystick GetJoystick() {
		return m_joystick;
	}
}
