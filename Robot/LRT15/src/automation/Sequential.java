package automation;

import java.util.ArrayList;
import java.util.Queue;

public class Sequential extends Automation {
	ArrayList<Automation> routines;
	Queue<Automation> queued;
	boolean started;
	
	Sequential(String name, boolean queueIfBlocked, boolean restartable) 
	{
		super(name, false, queueIfBlocked, restartable);
		started = false;
	}

	Sequential(String name, ArrayList<Automation> sequence, boolean queueIfBlocked, boolean restartable) 
	{
		super(name, false, queueIfBlocked, restartable);
		routines = sequence;
		started = false;
	}


	public void AllocateResources()
	{
		for (Automation auto : routines)
			auto.AllocateResources();
	}

	public boolean Start()
	{
		if (routines.isEmpty())
			return false;
		while (!queued.isEmpty())
			queued.remove();
		for (Automation auto : routines)
			queued.add(auto);
		started = false;
		return true;
	}

	protected boolean Run()
	{
		if (!started && ContinueNextStep())
		{
			boolean res = queued.peek().StartAutomation(GetStartEvent());
			if (res)
				started = true;
			else
				return true;
		}
		if (started)
		{
			boolean completed = queued.peek().Update();
			if (completed)
			{
				queued.remove();
				started = false;
			}
		}
		if (queued.isEmpty())
			return true;
		return false;
	}
	
	
	
	public boolean Abort()
	{
		if (!queued.isEmpty())
		{
			boolean res = queued.peek().AbortAutomation(GetAbortEvent());
			if (res)
			{
				while (!queued.isEmpty())
					queued.remove();
			}
			return res;
		}
		return true;
	}

	public void AddAutomation(Automation automation)
	{
		routines.add(automation);
	}

	public void AddAutomation(ArrayList<Automation> automation)
	{
		routines.ensureCapacity(routines.size() + automation.size());
		routines.addAll(routines.size()-1, automation);
	}

	public void ClearSequence()
	{
		routines.clear();
	}

	protected boolean ContinueNextStep()
	{
		return true;
	}

	protected Automation GetCurrentAutomation()
	{
		return queued.peek();
	}
}
