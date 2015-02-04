package automation;

import automation.DelayedEvent;
import automation.JoystickPressedEvent;
import driverstation.LRTJoystick;

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
