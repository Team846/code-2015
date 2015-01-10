package automation;

import java.util.ArrayList;

public abstract class InputProcessor {
	
	ArrayList<ControlResource> resources;
	
	public InputProcessor()
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
	
	protected void RegisterResource(ControlResource resource)
	{
		resources.add(resource);
	}

}
