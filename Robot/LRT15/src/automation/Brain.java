package automation;
import java.util.Map.Entry;


public class Brain {
	
	private static Brain instance = null;
	
	
	public static Brain Instance()
	{
		if(instance == null)
		{
			instance = new Brain();
		}
		return instance;
	}
	
	public static void Initialize()
	{
		if(instance == null)
		{
			instance = new Brain();
		}
	}
	
	private Brain()
	{
		//Create input processors
		
		//Create automation tasks
		
		//Create events
		
		
		//Map events to tasks
	}
	
//	public void Update()
//	{
//		
//		ProcessAutomationTasks();
//		
//		ProcessInputs();
//		
//		for (Event e : event_vector)
//		{
//			e.Update();
//		}
//	}
//	
//	private void ProcessAutomationTasks()
//	{
//		for(Entry<Automation, Event> pair: waitingTasks)
//		{
//			
//		}
//		
//		for(Event a : event_list)
//		{
//			if(a.Fired())
//			{
//				
//			}
//		}
//	}

}
