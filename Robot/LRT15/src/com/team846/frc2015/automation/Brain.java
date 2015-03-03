package com.team846.frc2015.automation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import com.team846.frc2015.automation.events.DelayedEvent;
import com.team846.frc2015.automation.events.Event;
import com.team846.frc2015.automation.events.GameModeChangeEvent;
import com.team846.frc2015.automation.events.JoystickMovedEvent;
import com.team846.frc2015.automation.events.JoystickPressedEvent;
import com.team846.frc2015.automation.events.JoystickReleasedEvent;
import com.team846.frc2015.automation.inputProcessors.CarriageExtenderInputs;
import com.team846.frc2015.automation.inputProcessors.CarriageHooksInputs;
import com.team846.frc2015.automation.inputProcessors.CollectorArmInputs;
import com.team846.frc2015.automation.inputProcessors.CollectorRollersInputs;
import com.team846.frc2015.automation.inputProcessors.DrivetrainInputs;
import com.team846.frc2015.automation.inputProcessors.ElevatorInputs;
import com.team846.frc2015.automation.inputProcessors.InputProcessor;
import com.team846.frc2015.automation.inputProcessors.DrivetrainInputs.Axis;
import com.team846.frc2015.componentData.CarriageHooksData;
import com.team846.frc2015.config.DriverStationConfig;
import com.team846.frc2015.driverstation.GameState;
import com.team846.frc2015.driverstation.LRTDriverStation;
import com.team846.frc2015.driverstation.LRTJoystick;
import com.team846.frc2015.utils.Pair;

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
		
		createInputProcessors();

		
		// All automation routines		
		Automation auton = new Autonomous();
	
		Automation load_tote = new LoadTote();
		Automation load_sideways_container = new LoadSidewaysContainer();
		Automation load_upright_container = new LoadUprightContainer();
		
		Automation releaseStack = new ReleaseStack();
		
		// Declare event triggers
		Event to_auto = new GameModeChangeEvent(GameState.AUTONOMOUS);
		Event driver_stick_moved = new JoystickMovedEvent(driverStick);
		Event operator_stick_moved = new JoystickMovedEvent(operatorStick);
		Event driver_stick_pressed = new JoystickPressedEvent(driverStick);
		Event operator_stick_pressed = new JoystickPressedEvent(operatorStick);
		Event disabled_timeout = new DelayedEvent(new GameModeChangeEvent(GameState.DISABLED), 100);
		
		Event load_tote_start = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_TOTE);
		Event load_tote_abort = new JoystickReleasedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_TOTE);
		
		Event load_upright_container_start = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_UPRIGHT_CONTAINER); 
		Event load_upright_container_abort = new JoystickReleasedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_UPRIGHT_CONTAINER);
		
		Event load_sideways_container_start = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_SIDEWAYS_CONTAINER);
		Event load_sideways_container_abort = new JoystickReleasedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_SIDEWAYS_CONTAINER);
		
		Event release_stack_start = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.DEPLOY_STACK);
		Event release_stack_abort = new JoystickReleasedEvent(operatorStick, DriverStationConfig.JoystickButtons.DEPLOY_STACK);
		
		// Map events to routines
		to_auto.AddStartListener(auton);
		driver_stick_moved.AddAbortListener(auton);
		operator_stick_moved.AddAbortListener(auton);
		driver_stick_pressed.AddAbortListener(auton);
		operator_stick_pressed.AddAbortListener(auton);
		disabled_timeout.AddAbortListener(auton);
		
		release_stack_start.AddStartListener(releaseStack);
		release_stack_abort.AddAbortListener(releaseStack);
		
		load_tote_start.AddStartListener(load_tote);
		load_tote_abort.AddAbortListener(load_tote);
		
		load_sideways_container_start.AddStartListener(load_sideways_container);
		load_sideways_container_abort.AddAbortListener(load_sideways_container);
		
		load_upright_container_start.AddStartListener(load_upright_container);
		load_upright_container_abort.AddAbortListener(load_upright_container);
	}
	
	private void createInputProcessors() {
		inputs.add(new DrivetrainInputs(Axis.DRIVE));
		inputs.add(new DrivetrainInputs(Axis.TURN));
		inputs.add(new DrivetrainInputs(Axis.STRAFE));
//		inputs.add(new CollectorArmInputs());
//		inputs.add(new CollectorRollersInputs());
		inputs.add(new ElevatorInputs());
		inputs.add(new CarriageExtenderInputs());
		inputs.add(new CarriageHooksInputs());
	}

	public void Update()
	{
	 	ProcessAutomationTasks();
		
		ProcessInputs();
		
		for ( Event e : Event.event_vector)
		{
			e.Update();
		}
	}

	void ProcessAutomationTasks()
	{

		// Try to start queued tasks
		Iterator<Map.Entry<Automation, Event>> it = waitingTasks.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry<Automation, Event> e = (Map.Entry<Automation, Event>)it.next();
			if (!runningTasks.contains(e.getKey())) // If task isn't running
		    {
				if (e.getKey().CheckResources())
				{
					boolean ret = e.getKey().StartAutomation(e.getValue());
					if (ret)
					{
						runningTasks.add(e.getKey());
						it.remove(); //TODO: check remove/erase compatibbility
					}
				}
	        }
			
		}
		
		// Start/Abort/Continue tasks which had their events fired
		//Cant use nice nested foreach bc java is stupid
		for ( int i = 0; i< Event.event_vector.size();i++)
		{
			Event event = Event.event_vector.get(i);
			if (event.Fired())
			{
	        	// Tasks aborted by this event
	        	for (Automation a : event.GetAbortListeners())
	        	{
	        	    if (runningTasks.contains(a)) // If task is running
	        	    {
	        	    	boolean ret = a.AbortAutomation(event);
	        		    if (ret)
	        		    {
	        		    	a.DeallocateResources();
	        		        runningTasks.remove(a);
	        		    }
	        		}
	        	}
	        	
			    // Tasks started by this event
	        	for ( Automation auto : event.GetStartListeners())
	        	{
	        		if (!runningTasks.contains(auto) || auto.IsRestartable()) // If task isn't running or is restartable
	        		{
	      
	        			if (auto.CheckResources())
	        			{
							boolean ret = auto.StartAutomation(event);
							if(ret)
								runningTasks.add(auto);
	        			}
	        			else
	        			{
	        				if (auto.QueueIfBlocked())
	        					waitingTasks.put(auto, event);
	        			}
	        		}
	        	}

	        	// Tasks continued by this event
	        	for (Automation a : event.GetContinueListeners())
	        	{
	        	    if (runningTasks.contains(a))//!= runningTasks.end()) // If task is running
	        	    {
	        		    a.ContinueAutomation(event);
	        		}
	        	}
			}
		}
		
		// Update running tasks
		ListIterator<Automation> runningTaskIterator = runningTasks.listIterator();
		while(runningTaskIterator.hasNext())
		{
			Automation a = runningTaskIterator.next();
			boolean complete = a.Update();
			if(complete)
			{
				a.DeallocateResources();
				runningTaskIterator.remove();
				//runningTaskIterator.previous();
			}
			
		}
	
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
