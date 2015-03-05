package com.team846.frc2015.automation.events;

import com.team846.frc2015.automation.events.Event;
import com.team846.frc2015.driverstation.LRTJoystick;

public class JoystickMovedEvent extends Event {
	LRTJoystick m_joystick;
    int m_axis;	
    float m_sensitivity;
    int m_lastFiredAxis;
	
	public JoystickMovedEvent(LRTJoystick joystick, int axis, float sensitivity) {
		m_joystick = joystick;
        m_axis = axis;
        m_sensitivity = sensitivity;
        m_lastFiredAxis = axis;
	}
	
	public JoystickMovedEvent(LRTJoystick joy)
	{
		this(joy, 0,0.1f);
	}
	
	public boolean CheckCondition() {
        if (m_axis == 0)	
        {	
                for (int i = 1; i <= m_joystick.GetNumAxes(); i++)
                {	
                        if (Math.abs(m_joystick.getRawAxis(i)) >= m_sensitivity)	
                        {	
                                return true;	
                        }	
                }	
        }	
        else if (Math.abs(m_joystick.getRawAxis(m_axis)) >= m_sensitivity)
        {	
                return true;	
        }
	
        return false;
	}
	
	public int GetAxis() {
		return m_lastFiredAxis;
	}
	
	public LRTJoystick GetJoystick() {
		return m_joystick;
	}
}
