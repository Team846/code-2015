package automation;

import automation.Event;

public class DelayedEvent extends Event {
	Event m_event;
	int m_delay;
	int m_count;
	
	DelayedEvent(Event event, int delayCycles) {
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
