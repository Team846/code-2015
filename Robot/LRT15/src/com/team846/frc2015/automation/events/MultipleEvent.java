package com.team846.frc2015.automation.events;

import java.util.ArrayList;
import java.util.Arrays;

public class MultipleEvent extends Event {
	
	ArrayList<Event> eventList;
	
	public MultipleEvent()
	{
		eventList = new ArrayList<Event>();
	}
	
	public MultipleEvent(ArrayList<Event> events) {
		eventList = events;
	}
	
	public void AddEvent(Event e)
	{
		eventList.add(e);
	}

	@Override
	public boolean CheckCondition() {
		boolean condition = true;
		for(Event e : eventList)
		{
			if(!e.CheckCondition())
			{
				condition = false;
				break;
			}
		}
		return condition;
	}

}
