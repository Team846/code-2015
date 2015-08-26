package com.team846.frc2015.automation.inputProcessors;

import java.util.ArrayList;

import com.team846.frc2015.automation.Automation;
import com.team846.frc2015.automation.ControlResource;

public abstract class InputProcessor {
	
	private final ArrayList<ControlResource> resources = new ArrayList<ControlResource>();
	
	InputProcessor()
	{
	}
	
	public abstract void Update();
	
	public boolean CheckResources()
	{
		for(ControlResource resource : resources)
		{
			if(Automation.GetAllocation(resource))
				return false;
		}
		return true;
	}
	
	void RegisterResource(ControlResource resource)
	{
		resources.add(resource);
	}

}
