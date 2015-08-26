package com.team846.frc2015.automation.events;

import com.team846.frc2015.automation.events.Event;

public class DelayedEvent extends Event {
	private final Event m_event;
	private final int m_delay;
	private int m_count;
	
	public DelayedEvent(Event event, int delayCycles) {
		m_event = event;
		m_delay = delayCycles;
		m_count = 0;
	}

	public boolean CheckCondition() {
		return m_count >= m_delay;
	}
	
	public void Update() {
		m_event.Update();
		
		if (m_event.CheckCondition()) {
			m_count++;
		} else {
			m_count = 0;
		}
	}
}
