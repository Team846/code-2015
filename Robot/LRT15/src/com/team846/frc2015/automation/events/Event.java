package com.team846.frc2015.automation.events;
import java.util.ArrayList;

import com.team846.frc2015.automation.Automation;


public abstract class Event 
{
	ArrayList<Automation> start_listeners = new ArrayList<>();
	ArrayList<Automation> abort_listeners = new ArrayList<>();
	ArrayList<Automation> continue_listeners = new ArrayList<>();
	
	boolean lastFired;
	
	public static ArrayList<Event> event_vector = new ArrayList<Event>();
	
	public Event()
	{
		event_vector.add(this);
		lastFired = false;
	}
	
	public abstract boolean CheckCondition();
	
	public boolean Fired()
	{
		return CheckCondition() && !lastFired;
	}
	
	public void Update()
	{
		lastFired = CheckCondition();
	}
	
	public void AddStartListener(Automation routine)
	{
		start_listeners.add(routine);
	}
	
	public void AddAbortListener(Automation routine)
	{
		abort_listeners.add(routine);
	}
	
	public void AddContinueListener(Automation routine)
	{
		continue_listeners.add(routine);
	}
	
	public ArrayList<Automation> GetStartListeners()
	{
		return start_listeners;
	}
	
	public ArrayList<Automation> GetAbortListeners()
	{
		return abort_listeners;
	}
	
	public ArrayList<Automation> GetContinueListeners()
	{
		return continue_listeners;
	}
}
