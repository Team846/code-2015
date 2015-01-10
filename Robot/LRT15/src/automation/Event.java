package automation;
import java.util.ArrayList;


public abstract class Event 
{
	ArrayList<Automation> start_listeners = new ArrayList<>();
	ArrayList<Automation> abort_listeners = new ArrayList<>();
	ArrayList<Automation> continue_listeners = new ArrayList<>();
	
	boolean lastFired;
	
	public static ArrayList<Event> event_list = new ArrayList<Event>();
	
	public Event()
	{
		event_list.add(this);
		lastFired = false;
	}
	
	public abstract boolean CheckCondition();
	
	public boolean Fired()
	{
		return CheckCondition() && !lastFired;
	}
	
	
	public void Update()
	{
		lastFired = CheckCondition();
	}
	
	public void AddStartListener(Automation routine)
	{
		start_listeners.add(routine);
	}
	
	public void AddAbortListener(Automation routine)
	{
		abort_listeners.add(routine);
	}
	
	public void AddContinueListener(Automation routine)
	{
		continue_listeners.add(routine);
	}
	
	public ArrayList<Automation> GetStartListeners()
	{
		return start_listeners;
	}
	
	public ArrayList<Automation> GetAbortListeners()
	{
		return abort_listeners;
	}
	
	public ArrayList<Automation> GetContinueListeners()
	{
		return continue_listeners;
	}

}
