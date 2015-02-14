package com.team846.frc2015.automation.events;

import com.team846.frc2015.automation.events.Event;
import com.team846.frc2015.driverstation.LRTJoystick;

public class JoystickReleasedEvent extends Event {
	private LRTJoystick joystick;
    private int m_button;
    private int m_lastFiredButton;
	
	public JoystickReleasedEvent(LRTJoystick joystick, int button) {
		this.joystick = joystick;
        m_button = button;
        m_lastFiredButton = button;
	}
	
	public JoystickReleasedEvent(LRTJoystick joystick)
	{
		this(joystick, 0);
	}
	
	public boolean CheckCondition() {
	if (m_button == 0)
        {	
            for (int i = 1; i <= joystick.GetNumButtons(); i++)	
            {	
                if (!joystick.IsButtonDown(i))	
                {	
                        return true;	
                }	
            }	
        }	
        else if (!joystick.IsButtonDown(m_button))	
        {	
                return true;	
        }	
        return false;
	}
	
	public int GetButton() {
		return m_lastFiredButton;
	}
	
	public LRTJoystick GetJoystick() {
		return joystick;
	}
}
