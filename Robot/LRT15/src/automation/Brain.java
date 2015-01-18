package automation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

import utils.Pair;

public class Brain 
{
	static Brain instance = null;
	
	ArrayList<InputProcessor> inputs = new ArrayList<InputProcessor>();
	ArrayList<Automation> automation = new ArrayList<Automation>();
	
	LinkedList<Automation> runningTasks = new LinkedList<Automation>();
	HashMap<Automation, Event> waitingTasks = new HashMap<Automation, Event>();
	
	public static Brain Instance()
	{
		if(instance == null)
			instance = new Brain();
		return instance;
	}
	
	public static void Initialize()
	{
		if (instance == null)
			instance = new Brain();
	}
	
	public void Update()
	{
	 	ProcessAutomationTasks();
		
		ProcessInputs();
		
		for ( Event e : Event.event_list)
		{
			e.Update();
		}
	}

	void ProcessAutomationTasks()
	{
		// Try to start queued tasks
		for (Map.Entry<Automation, Event> e : waitingTasks.entrySet())
		{
		    if (!runningTasks.contains(e.getKey())) // If task isn't running
		    {
				if (e.getKey().CheckResources())
				{
					boolean ret = e.getKey().StartAutomation(e.getValue());
					if (ret)
					{
						runningTasks.add(e.getKey());
						waitingTasks.remove(e); //TODO: check remove/erase compatibbility
					}
				}
	        }
		}
		
		// Start/Abort/Continue tasks which had their events fired
		//Cant use nice nested foreach bc java is stupid
		for ( int i = 0; i< Event.event_list.size();i++)
		{
			if (Event.event_list.get(i).Fired())
			{
	        	// Tasks aborted by this event
	        	for (Automation a : Event.event_list.get(i).GetAbortListeners())
	        	{
	        	    if (runningTasks.contains(a)) // If task is running
	        	    {
	        	    	boolean ret = a.AbortAutomation(Event.event_list.get(i));
	        		    if (ret)
	        		    {
	        		    	a.DeallocateResources();
	        		        runningTasks.remove(a);
	        		    }
	        		}
	        	}
	        	
			    // Tasks started by this event
	        	for ( int j = 0; i < Event.event_list.get(i).GetStartListeners().size(); j++)
	        	{
	        		Automation auto = Event.event_list.get(i).GetStartListeners().get(j);
	      
	        		if (!runningTasks.contains(auto) || auto.IsRestartable()) // If task isn't running or is restartable
	        		{
	        			
	        			if (auto.CheckResources())
	        			{
							boolean ret = auto.StartAutomation(Event.event_list.get(i));
							if(ret)
								runningTasks.add(auto);
	        			}
	        			else
	        			{
	        				if (auto.QueueIfBlocked())
	        					waitingTasks.put(auto, Event.event_list.get(i));
	        			}
	        		}
	        	}

	        	// Tasks continued by this event
	        	for (Automation a : Event.event_list.get(i).GetContinueListeners())
	        	{
	        	    if (runningTasks.contains(a))//!= runningTasks.end()) // If task is running
	        	    {
	        		    a.ContinueAutomation(Event.event_list.get(i));
	        		}
	        	}
			}
		}
		
		ListIterator<Automation> runningTaskIterator = runningTasks.listIterator();
		while(runningTaskIterator.hasNext())
		{
			Automation a = runningTaskIterator.next();
			boolean complete = a.Update();
			if(complete)
			{
				a.DeallocateResources();
				runningTasks.remove(a);
				runningTaskIterator.previous();
			}
			
		}
		
//	    // Update running tasks
//	    for (list<Automation*>::iterator a = runningTasks.begin(); a != runningTasks.end(); a++)
//	    {
//	    	bool complete = (*a)->Update();
//		    if (complete)
//		    {
//		    	(*a)->DeallocateResources();
//		    	runningTasks.erase(a++);
//		    	a--;
//		    }
//	    }
	}

	void ProcessInputs()
	{
		for (InputProcessor i : inputs)
		{
			if (i.CheckResources())
			{
				i.Update();
			}
		}
	}

//	void Log()
//	{
//		for (vector<Automation*>::iterator it = automation.begin(); it < automation.end(); it++)
//		{
//			LogToFile(find(runningTasks.begin(), runningTasks.end(), *it) != runningTasks.end(), Event.event_list.get(i)->GetName());
//		}
//	}

//	void PrintRunningAutomation()
//	{
//		BufferedConsole::Printf("Running Automation Routines:\n");
//		for (vector<Automation*>::iterator it = automation.begin(); it < automation.end(); it++)
//		{
//			if (find(runningTasks.begin(), runningTasks.end(), *it) != runningTasks.end())
//				BufferedConsole::Printf("%s\n", Event.event_list.get(i)->GetName().c_str());
//		}
//	}


}
