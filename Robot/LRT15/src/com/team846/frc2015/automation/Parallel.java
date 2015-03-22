package com.team846.frc2015.automation;

import java.util.ArrayList;
import java.util.ListIterator;

import com.team846.frc2015.automation.Automation.RoutineOption;

public class Parallel extends Automation {

	 private boolean abortOnFirst = false;
	 private ArrayList<Automation> routines = new ArrayList<Automation>();
	 private final ArrayList<Automation> running = new ArrayList<Automation>();
	 private boolean aborting = false;
	 
	 public Parallel(String name)
	 {
		 this(name, false);
	 }
	 
	 public Parallel(String name, ArrayList<Automation> sequence)
	 {
		 super(name);
		 routines = sequence;
		 aborting = false;
	 }

	public Parallel(String name, boolean abortOnFirst, RoutineOption...options) 
	 {
		 super(name, options);
		 if(routineOptions.contains(RoutineOption.REQUIRES_ABORT_CYCLES))
				routineOptions.remove(RoutineOption.REQUIRES_ABORT_CYCLES);
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
		aborting = false;
		return success;
	}

	@Override
	protected boolean Run() {
		boolean completed = !abortOnFirst;
		ListIterator<Automation> iter = running.listIterator();
		while(iter.hasNext())
		{
			Automation a = iter.next();
			//iter.previous();
			if(!a.Update())
			{
				if(!abortOnFirst)
					completed = false;
			}
			else
			{
				iter.remove();
				//iter.previous();
				if(abortOnFirst)
					completed = true;
			}
		}
		if(completed && abortOnFirst && !aborting)
		{
			boolean ret = Abort();
			if (!ret)
			{
				aborting = true;
				return false;
			}
		}
		return completed;
	}
	
	
	@Override
	protected boolean Abort() {
		
		boolean success = true;
		ListIterator<Automation> iter = running.listIterator();
		while(iter.hasNext())
		{
			Automation a = iter.next();
			boolean ret = a.AbortAutomation(GetAbortEvent());
			if(!ret)
				success = false;
			else
			{
				iter.remove();
				//iter.previous();
				//.previous();
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
