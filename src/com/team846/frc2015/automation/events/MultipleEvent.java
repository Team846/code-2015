package com.team846.frc2015.automation.events;

import java.util.ArrayList;

public class MultipleEvent extends Event {

    private final ArrayList<Event> eventList;

    public MultipleEvent() {
        eventList = new ArrayList<Event>();
    }

    public MultipleEvent(ArrayList<Event> events) {
        eventList = events;
    }

    public void AddEvent(Event e) {
        eventList.add(e);
    }

    @Override
    public boolean checkCondition() {
        boolean condition = true;
        for (Event e : eventList) {
            if (!e.checkCondition()) {
                condition = false;
                break;
            }
        }
        return condition;
    }

}
