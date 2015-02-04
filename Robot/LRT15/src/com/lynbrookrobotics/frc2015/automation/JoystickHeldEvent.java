package com.lynbrookrobotics.frc2015.automation;

import com.lynbrookrobotics.frc2015.automation.DelayedEvent;
import com.lynbrookrobotics.frc2015.automation.JoystickPressedEvent;
import com.lynbrookrobotics.frc2015.driverstation.LRTJoystick;

public class JoystickHeldEvent extends DelayedEvent {
	LRTJoystick m_joystick;
	int m_lastFiredButton;
	
	JoystickHeldEvent(LRTJoystick joystick, int button, int cycles) {
		super(new JoystickPressedEvent(joystick, button), cycles);
		
		m_joystick = joystick;
		m_lastFiredButton = button;
	}
	
	public int GetButton() {
		return m_lastFiredButton;
	}
	
	public LRTJoystick GetJoystick() {
		return m_joystick;
	}
}
