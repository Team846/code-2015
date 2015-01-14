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
	static Brain m_instance = null;
	
	ArrayList<InputProcessor> m_inputs = new ArrayList<InputProcessor>();
	ArrayList<Automation> m_automation = new ArrayList<Automation>();
	
	LinkedList<Automation> m_runningTasks = new LinkedList<Automation>();
	HashMap<Automation, Event> m_waitingTasks = new HashMap<Automation, Event>();
	
	public static Brain Instance()
	{
		if(m_instance == null)
			m_instance = new Brain();
		return m_instance;
	}
	
	public static void Initialize()
	{
		if (m_instance == null)
			m_instance = new Brain();
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
		for (Map.Entry<Automation, Event> e : m_waitingTasks.entrySet())
		{
		    if (!m_runningTasks.contains(e.getKey())) // If task isn't running
		    {
				if (e.getKey().CheckResources())
				{
					boolean ret = e.getKey().StartAutomation(e.getValue());
					if (ret)
					{
						m_runningTasks.add(e.getKey());
						m_waitingTasks.remove(e); //TODO: check remove/erase compatibbility
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
	        	    if (m_runningTasks.contains(a)) // If task is running
	        	    {
	        	    	boolean ret = a.AbortAutomation(Event.event_list.get(i));
	        		    if (ret)
	        		    {
	        		    	a.DeallocateResources();
	        		        m_runningTasks.remove(a);
	        		    }
	        		}
	        	}
	        	
			    // Tasks started by this event
	        	for ( int j = 0; i < Event.event_list.get(i).GetStartListeners().size(); j++)
	        	{
	        		Automation auto = Event.event_list.get(i).GetStartListeners().get(j);
	      
	        		if (!m_runningTasks.contains(auto) || auto.IsRestartable()) // If task isn't running or is restartable
	        		{
	        			
	        			if (auto.CheckResources())
	        			{
							boolean ret = auto.StartAutomation(Event.event_list.get(i));
							if(ret)
								m_runningTasks.add(auto);
	        			}
	        			else
	        			{
	        				if (auto.QueueIfBlocked())
	        					m_waitingTasks.put(auto, Event.event_list.get(i));
	        			}
	        		}
	        	}

	        	// Tasks continued by this event
	        	for (Automation a : Event.event_list.get(i).GetContinueListeners())
	        	{
	        	    if (m_runningTasks.contains(a))//!= m_runningTasks.end()) // If task is running
	        	    {
	        		    a.ContinueAutomation(Event.event_list.get(i));
	        		}
	        	}
			}
		}
		
		ListIterator<Automation> runningTaskIterator = m_runningTasks.listIterator();
		while(runningTaskIterator.hasNext())
		{
			Automation a = runningTaskIterator.next();
			boolean complete = a.Update();
			if(complete)
			{
				a.DeallocateResources();
				m_runningTasks.remove(a);
				runningTaskIterator.previous();
			}
			
		}
		
//	    // Update running tasks
//	    for (list<Automation*>::iterator a = m_runningTasks.begin(); a != m_runningTasks.end(); a++)
//	    {
//	    	bool complete = (*a)->Update();
//		    if (complete)
//		    {
//		    	(*a)->DeallocateResources();
//		    	m_runningTasks.erase(a++);
//		    	a--;
//		    }
//	    }
	}

	void ProcessInputs()
	{
		for (InputProcessor i : m_inputs)
		{
			if (i.CheckResources())
			{
				i.Update();
			}
		}
	}

//	void Log()
//	{
//		for (vector<Automation*>::iterator it = m_automation.begin(); it < m_automation.end(); it++)
//		{
//			LogToFile(find(m_runningTasks.begin(), m_runningTasks.end(), *it) != m_runningTasks.end(), Event.event_list.get(i)->GetName());
//		}
//	}

//	void PrintRunningAutomation()
//	{
//		BufferedConsole::Printf("Running Automation Routines:\n");
//		for (vector<Automation*>::iterator it = m_automation.begin(); it < m_automation.end(); it++)
//		{
//			if (find(m_runningTasks.begin(), m_runningTasks.end(), *it) != m_runningTasks.end())
//				BufferedConsole::Printf("%s\n", Event.event_list.get(i)->GetName().c_str());
//		}
//	}


}
