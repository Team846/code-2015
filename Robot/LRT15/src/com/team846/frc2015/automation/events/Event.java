package com.team846.frc2015.automation.events;

import java.util.ArrayList;

import com.team846.frc2015.automation.Automation;


public abstract class Event {
    private final ArrayList<Automation> start_listeners = new ArrayList<>();
    private final ArrayList<Automation> abort_listeners = new ArrayList<>();
    private final ArrayList<Automation> continue_listeners = new ArrayList<>();

    private boolean lastFired;

    public static final ArrayList<Event> event_vector = new ArrayList<Event>();

    Event() {
        event_vector.add(this);
        lastFired = false;
    }

    protected abstract boolean checkCondition();

    public boolean fired() {
        return checkCondition() && !lastFired;
    }

    public void update() {
        lastFired = checkCondition();
    }

    public void addStartListener(Automation routine) {
        start_listeners.add(routine);
    }

    public void addAbortListener(Automation routine) {
        abort_listeners.add(routine);
    }

    //TODO: evaluate if needed
    public void addContinueListener(Automation routine) {
        continue_listeners.add(routine);
    }

    public ArrayList<Automation> getStartListeners() {
        return start_listeners;
    }

    public ArrayList<Automation> getAbortListeners() {
        return abort_listeners;
    }

    public ArrayList<Automation> getContinueListeners() {
        return continue_listeners;
    }
}
