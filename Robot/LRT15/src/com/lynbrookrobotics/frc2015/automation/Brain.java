package com.lynbrookrobotics.frc2015.automation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

import com.lynbrookrobotics.frc2015.componentData.CarriageHooksData;
import com.lynbrookrobotics.frc2015.config.DriverStationConfig;
import com.lynbrookrobotics.frc2015.driverstation.GameState;
import com.lynbrookrobotics.frc2015.driverstation.LRTDriverStation;
import com.lynbrookrobotics.frc2015.driverstation.LRTJoystick;
import com.lynbrookrobotics.frc2015.events.DelayedEvent;
import com.lynbrookrobotics.frc2015.events.Event;
import com.lynbrookrobotics.frc2015.events.JoystickMovedEvent;
import com.lynbrookrobotics.frc2015.events.JoystickPressedEvent;
import com.lynbrookrobotics.frc2015.events.JoystickReleasedEvent;
import com.lynbrookrobotics.frc2015.inputProcessors.CarriageExtenderInputs;
import com.lynbrookrobotics.frc2015.inputProcessors.CollectorArmInputs;
import com.lynbrookrobotics.frc2015.inputProcessors.CollectorRollersInputs;
import com.lynbrookrobotics.frc2015.inputProcessors.DrivetrainInputs;
import com.lynbrookrobotics.frc2015.inputProcessors.ElevatorInputs;
import com.lynbrookrobotics.frc2015.inputProcessors.InputProcessor;
import com.lynbrookrobotics.frc2015.utils.Pair;

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
	
	private Brain()
	{
		
		LRTJoystick operatorStick = LRTDriverStation.Instance().GetOperatorStick();
		LRTJoystick driverStick = LRTDriverStation.Instance().GetOperatorStick();
		LRTJoystick driverWheel= LRTDriverStation.Instance().GetOperatorStick();

		
		
		
		inputs.add(new DrivetrainInputs());
		inputs.add(new CollectorArmInputs());
		inputs.add(new CollectorRollersInputs());
		inputs.add(new ElevatorInputs());
		inputs.add(new CarriageExtenderInputs());
		inputs.add(new CarriageHookInputs());

		
		Automation auton = new Autonomous();
//		
//		Event to_auto = new GameModeChangeEvent(GameState.AUTONOMOUS);
//		Event driver_stick_moved = new JoystickMovedEvent(driverStick);
//		Event operator_stick_moved = new JoystickMovedEvent(operatorStick);
//		Event driver_stick_pressed = new JoystickPressedEvent(driverStick);
//		Event operator_stick_pressed = new JoystickPressedEvent(operatorStick);
//		Event disabled_timeout = new DelayedEvent(new GameModeChangeEvent(GameState.DISABLED), 100);
//		
//		Event collect_start = new JoystickPressedEvent(driverStick, DriverStationConfig.JoystickButtons.COLLECT);
//		Event collect_abort = new JoystickReleasedEvent(driverStick, DriverStationConfig.JoystickButtons.COLLECT);
//	
//		Event fire_start_long = new JoystickPressedEvent(LRTDriverStation.Instance().GetOperatorStick(), DriverStationConfig.JoystickButtons.LONG_SHOT);
//		Event fire_start_short = new JoystickPressedEvent(LRTDriverStation.Instance().GetOperatorStick(), DriverStationConfig.JoystickButtons.SHORT_SHOT);
//		
//		Event load_start = new JoystickPressedEvent(LRTDriverStation.Instance().GetOperatorStick(), DriverStationConfig.JoystickButtons.LOAD_LAUNCHER);
//		Event load_abort = new JoystickReleasedEvent(LRTDriverStation.Instance().GetOperatorStick(), DriverStationConfig.JoystickButtons.LOAD_LAUNCHER);
//		Event dribble_start = new JoystickPressedEvent(driverStick, DriverStationConfig.JoystickButtons.DRIBBLE);
//		Event dribble_abort = new JoystickReleasedEvent(driverStick, DriverStationConfig.JoystickButtons.DRIBBLE);
//		Event pass_back_start = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.);
//		Event pass_back_abort = new JoystickReleasedEvent(operatorStick, DriverStationConfig.JoystickButtons.PASS_BACK);
//		
		//map events to tasks
//		to_auto.AddStartListener(auton);
//		driver_stick_moved.AddAbortListener(auton);
//		operator_stick_moved.AddAbortListener(auton);
//		driver_stick_pressed.AddAbortListener(auton);
//		operator_stick_pressed.AddAbortListener(auton);
//		disabled_timeout.AddAbortListener(auton);
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
						waitingTasks.remove(e.getKey()); //TODO: check remove/erase compatibbility
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
//	    for (list<Automation*>.iterator a = runningTasks.begin(); a != runningTasks.end(); a++)
//	    {
//	    	bool complete = (*a).Update();
//		    if (complete)
//		    {
//		    	(*a).DeallocateResources();
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
//		for (vector<Automation*>.iterator it = automation.begin(); it < automation.end(); it++)
//		{
//			LogToFile(find(runningTasks.begin(), runningTasks.end(), *it) != runningTasks.end(), Event.event_list.get(i).GetName());
//		}
//	}

//	void PrintRunningAutomation()
//	{
//		BufferedConsole.Printf("Running Automation Routines:\n");
//		for (vector<Automation*>.iterator it = automation.begin(); it < automation.end(); it++)
//		{
//			if (find(runningTasks.begin(), runningTasks.end(), *it) != runningTasks.end())
//				BufferedConsole.Printf("%s\n", Event.event_list.get(i).GetName().c_str());
//		}
//	}


}
