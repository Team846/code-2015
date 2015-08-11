package com.team846.frc2015.automation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.team846.frc2015.automation.events.Event;

public abstract class Automation {
	private static final ArrayList<Automation> automation_vector = new ArrayList<>();
	private static Map<ControlResource, Integer> allocated = new HashMap<ControlResource, Integer>();
	private final ArrayList<ControlResource> resources = new ArrayList<ControlResource>();
	
	final ArrayList<RoutineOption> routineOptions = new ArrayList<RoutineOption>();
	
	private Event startEvent;
	private Event abortEvent;
	private Event continueEvent;
	
	private boolean aborting;
	
	private String name;
	
	public enum RoutineOption
	{
		REQUIRES_ABORT_CYCLES,
		QUEUE_IF_BLOCKED,
		RESTARTABLE
	}
	
	public Automation(RoutineOption... options)
	{
		this(null, options);
	}
	
	Automation(String name, RoutineOption... options)
	{
		startEvent = null;
		abortEvent = null;
		continueEvent = null;
		aborting = false;
		for(RoutineOption option: options)
		{
			if(!routineOptions.contains(option))
				routineOptions.add(option);
		}
		
		if(name == null)
		{
			this.name = this.getClass().getName();
			this.name = this.name.substring(this.name.lastIndexOf('.') + 1);
		}
		else
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
	
	protected abstract void AllocateResources();

	public boolean CheckResources()
	{
		Map<ControlResource, Integer > original = new HashMap<ControlResource, Integer >(allocated);
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

	Event GetStartEvent()
	{
		return startEvent;
	}

	Event GetAbortEvent()
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

	boolean AllocateResource(ControlResource resource)
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

	boolean Aborting()
	{
		return aborting;
	}

	public boolean IsRestartable()
	{
		return routineOptions.contains(RoutineOption.RESTARTABLE);
	}

	public boolean QueueIfBlocked()
	{
		return routineOptions.contains(RoutineOption.QUEUE_IF_BLOCKED);
	}

	boolean RequiresAbortCycles()
	{
		return routineOptions.contains(RoutineOption.REQUIRES_ABORT_CYCLES);
	}

	String GetName()
	{
		return name;
	}
}
