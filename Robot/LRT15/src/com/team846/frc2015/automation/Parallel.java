package com.team846.frc2015.automation;

import java.util.ArrayList;
import java.util.ListIterator;

public class Parallel extends Automation {

	 private boolean abortOnFirst = false;
	 private ArrayList<Automation> routines;
	 private ArrayList<Automation> running = new ArrayList<Automation>();
	 
	 public Parallel(String name)
	 {
		 this(name, false, false, false);
	 }
	 
	 public Parallel(String name, ArrayList<Automation> sequence)
	 {
		 super(name, false , false, false);
		 routines = sequence;
	 }

	public Parallel(String name, boolean qIfBlocked, boolean restartable, boolean abortOnFirst) 
	 {
		 super(name, false, qIfBlocked, restartable);
		 this.abortOnFirst = abortOnFirst;
	 }

	@Override
	public void AllocateResources() {
		for(Automation a: routines)
		{
			a.AllocateResources();
		}

	}

	@Override
	protected boolean Start() {
		boolean success = true;
		running.clear();
		for(Automation a : routines)
		{
			boolean ret = a.StartAutomation(GetStartEvent());
			if(!ret)
				success = false;
		}
		if(success)
		{
			running.addAll(0, routines);
		}
		return success;
	}

	@Override
	protected boolean Run() {
		boolean completed = !abortOnFirst;
		ListIterator<Automation> iter = running.listIterator();
		while(iter.hasNext())
		{
			Automation a = iter.next();
			iter.previous();
			if(!a.Update())
			{
				if(!abortOnFirst)
					completed = false;
			}
			else
			{
				iter.next();
				iter.remove();
				iter.previous();
				iter.previous();
				if(abortOnFirst)
					completed = true;
			}
		}
		if(completed && abortOnFirst)
			Abort();
		return completed;
	}
	
	
	@Override
	protected boolean Abort() {
		
		boolean success = true;
		ListIterator<Automation> iter = running.listIterator();
		while(iter.hasNext())
		{
			Automation a = iter.next();
			iter.previous();
			boolean ret = a.AbortAutomation(GetAbortEvent());
			if(!ret)
				success = false;
			else
			{
				iter.next();
				iter.remove();
				iter.previous();
				iter.previous();
			}
		}
		return success;
	}
	
	
	public void AddAutomation(Automation auto)
	{
		routines.add(auto);
	}
	
	public void AddAutomation(ArrayList<Automation> automation)
	{
		routines.ensureCapacity(routines.size() + automation.size());
		routines.addAll(automation);
	}
	
	public void ClearAutomation()
	{
		routines.clear();
	}

}
