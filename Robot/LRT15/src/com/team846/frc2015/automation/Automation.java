package com.team846.frc2015.automation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.team846.frc2015.automation.events.Event;

public abstract class Automation {
	public static ArrayList<Automation> automation_vector = new ArrayList<>();
	static Map<ControlResource, Integer> allocated = new HashMap<ControlResource, Integer>();
	ArrayList<ControlResource> resources = new ArrayList<ControlResource>();
	
	Event startEvent;
	Event abortEvent;
	Event continueEvent;
	
	boolean aborting;
	boolean restartable;
	boolean queueIfBlocked;
	boolean requiresAbortCycle;
	
	String name;
	
	public Automation(String name)
	{
		this(name, false, false, false);
	}
	
	//TODO: implement automation options instead of sequence of booleans
	public Automation(String name, boolean requiresAbortCycles, boolean queueIfBlocked, boolean restartable)
	{
		startEvent = null;
		abortEvent = null;
		continueEvent = null;
		aborting = false;
		this.restartable = restartable;
		this.queueIfBlocked = queueIfBlocked;
		this.requiresAbortCycle = requiresAbortCycles;
		this.name = name;
		automation_vector.add(this);
	}
	
	public boolean Update()
	{
		boolean completed = Run();
		continueEvent = null;
		if (completed)
		{
			aborting = false;
			return true;
		}
		return false;
	}
	
	public abstract void AllocateResources();

	public boolean CheckResources()
	{
		Map<ControlResource, Integer > original = allocated;
		AllocateResources();
		boolean success = true;
		for (Map.Entry<ControlResource, Integer> entry : allocated.entrySet())
		{
			if (entry.getValue() > 1) // Resource allocated by this routine twice or resource already allocated by another routine
			{
				if (!original.containsKey(entry.getKey())) // Resource allocated twice
				{
					entry.setValue(1);
				}
				else // Resource already allocated by another routine
				{
					success = false;
				}
			}
		}
		if (!success)
		{
			allocated = original;
		}
		else
		{
			for (Map.Entry<ControlResource, Integer> entry : allocated.entrySet())
			{
				if (!original.containsKey(entry.getKey()))
					resources.add(entry.getKey());
			}
		}
		return success;
	}

	public boolean StartAutomation(Event trigger)
	{
		startEvent = trigger;
		aborting = false;
		return Start();
	}

	public boolean AbortAutomation(Event trigger)
	{
		abortEvent = trigger;
		boolean success = Abort();
		if (success)
		{
			if (RequiresAbortCycles())
			{
				aborting = true;
				return false;
			}
			startEvent = null;
			abortEvent = null;
			return true;
		}
		return false;
	}

	public void ContinueAutomation(Event trigger)
	{
		continueEvent = trigger;
	}

	protected boolean Continued()
	{
		return continueEvent != null;
	}

	protected Event GetStartEvent()
	{
		return startEvent;
	}

	protected Event GetAbortEvent()
	{
		return abortEvent;
	}
	
	public Event GetContinueEvent()
	{
		return continueEvent;
	}
	
	protected abstract boolean Start();
	protected abstract boolean Abort();
	protected abstract boolean Run();

	protected boolean AllocateResource(ControlResource resource)
	{
		if (!allocated.containsKey(resource))
		{
			allocated.put(resource, 1);
			return true;
		}
		allocated.put(resource, allocated.get(resource) + 1);
		return false;
	}

	public void DeallocateResources()
	{
		for (ControlResource c : resources)
		{
			if (allocated.containsKey(c))
				allocated.remove(c);
		}
		resources.clear();
	}

	public static boolean GetAllocation(ControlResource resource)
	{
		if (allocated.containsKey(resource))
			return allocated.get(resource) == 1;
		return false;
	}

	protected boolean Aborting()
	{
		return aborting;
	}

	public boolean IsRestartable()
	{
		return restartable;
	}

	public boolean QueueIfBlocked()
	{
		return queueIfBlocked;
	}

	public boolean RequiresAbortCycles()
	{
		return requiresAbortCycle;
	}

	public String GetName()
	{
		return name;
	}
}
