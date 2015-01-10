package automation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Automation {
	public static ArrayList<Automation> automation_vector = new ArrayList<>();
	static Map<ControlResource, Integer> allocated = new HashMap<ControlResource, Integer>();
	ArrayList<ControlResource> resources;
	
	Event m_startEvent;
	Event m_abortEvent;
	Event m_continueEvent;
	
	boolean m_aborting;
	boolean m_restartable;
	boolean m_queueIfBlocked;
	boolean m_requiresAbortCycles;
	
	String m_name;
	
	public Automation(String name, boolean requiresAbortCycles, boolean queueIfBlocked, boolean restartable)
	{
		m_startEvent = null;
		m_abortEvent = null;
		m_continueEvent = null;
		m_aborting = false;
		m_restartable = restartable;
		m_queueIfBlocked = queueIfBlocked;
		m_requiresAbortCycles = requiresAbortCycles;
		m_name = name;
		automation_vector.add(this);
	}

	public boolean Update()
	{
		boolean completed = Run();
		m_continueEvent = null;
		if (completed)
		{
			m_aborting = false;
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
		m_startEvent = trigger;
		m_aborting = false;
		return Start();
	}

	public boolean AbortAutomation(Event trigger)
	{
		m_abortEvent = trigger;
		boolean success = Abort();
		if (success)
		{
			if (RequiresAbortCycles())
			{
				m_aborting = true;
				return false;
			}
			m_startEvent = null;
			m_abortEvent = null;
			return true;
		}
		return false;
	}

	public void ContinueAutomation(Event trigger)
	{
		m_continueEvent = trigger;
	}

	protected boolean Continued()
	{
		return m_continueEvent != null;
	}

	protected Event GetStartEvent()
	{
		return m_startEvent;
	}

	protected Event GetAbortEvent()
	{
		return m_abortEvent;
	}
	
	public Event GetContinueEvent()
	{
		return m_continueEvent;
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
		return m_aborting;
	}

	public boolean IsRestartable()
	{
		return m_restartable;
	}

	public boolean QueueIfBlocked()
	{
		return m_queueIfBlocked;
	}

	public boolean RequiresAbortCycles()
	{
		return m_requiresAbortCycles;
	}

	public String GetName()
	{
		return m_name;
	}
}
