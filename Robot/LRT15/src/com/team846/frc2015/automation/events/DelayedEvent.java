package com.team846.frc2015.automation.events;

public class DelayedEvent extends Event {
	private final Event m_event;
	private final int m_delay;
	private int m_count;
	
	public DelayedEvent(Event event, int delayCycles) {
		m_event = event;
		m_delay = delayCycles;
		m_count = 0;
	}

	public boolean checkCondition() {
		return m_count >= m_delay;
	}
	
	public void update() {
		m_event.update();
		
		if (m_event.checkCondition()) {
			m_count++;
		} else {
			m_count = 0;
		}
	}
}
