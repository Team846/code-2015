package com.team846.frc2015.automation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

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

		//create input processor
		inputs.add(new DrivetrainInputs(Axis.DRIVE));
		inputs.add(new DrivetrainInputs(Axis.TURN));
		inputs.add(new DrivetrainInputs(Axis.STRAFE));
		inputs.add(new CollectorArmInputs());
		inputs.add(new CollectorRollersInputs());
		inputs.add(new ElevatorInputs());
		inputs.add(new CarriageExtenderInputs());
		inputs.add(new CarriageHooksInputs());
		
		Automation auton = new Autonomous();
		
		Automation collect_tote = new Collect();
		Automation collect_upright_container = new Collect(true, true);	
		Automation load_tote = new LoadTote();
		Automation load_container = new LoadContainer();
		Automation load_upright_container = new LoadUprightContainer();
		
		Automation elevate_1 = new Elevate(1);
		Automation elevate_2 = new Elevate(2);
		Automation elevate_3 = new Elevate(3);
		Automation elevate_4 = new Elevate(4);
		
		Automation extendCarriage = new ExtendCarriage();
		Automation releaseStack = new ReleaseStack();
		
		Event to_auto = new GameModeChangeEvent(GameState.AUTONOMOUS);
		Event driver_stick_moved = new JoystickMovedEvent(driverStick);
		Event operator_stick_moved = new JoystickMovedEvent(operatorStick);
		Event driver_stick_pressed = new JoystickPressedEvent(driverStick);
		Event operator_stick_pressed = new JoystickPressedEvent(operatorStick);
		Event disabled_timeout = new DelayedEvent(new GameModeChangeEvent(GameState.DISABLED), 100);
		
		Event collect_tote_start = new JoystickPressedEvent(driverStick, DriverStationConfig.JoystickButtons.COLLECT_TOTE);
		Event collect_tote_abort = new JoystickReleasedEvent(driverStick, DriverStationConfig.JoystickButtons.COLLECT_TOTE);
		
		Event collect_upright_container_start = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.COLLECT_UPRIGHT_CONTAINER);
		Event collect_upright_container_abort = new JoystickReleasedEvent(operatorStick, DriverStationConfig.JoystickButtons.COLLECT_UPRIGHT_CONTAINER);
		
		Event load_tote_start = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_TOTE);
		Event load_tote_abort = new JoystickReleasedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_TOTE);
		
		Event load_upright_container_start = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_UPRIGHT_CONTAINER); 
		Event load_upright_container_abort = new JoystickReleasedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_UPRIGHT_CONTAINER);
		
		Event load_container_start = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_CONTAINER);
		Event load_container_abort = new JoystickReleasedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_CONTAINER);
		
		Event elevate_one_start = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.ELEVATE_ONE);
		Event elevate_one_abort = new JoystickReleasedEvent(operatorStick, DriverStationConfig.JoystickButtons.ELEVATE_ONE);
		
		Event elevate_two_start = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.ELEVATE_TWO);
		Event elevate_two_abort = new JoystickReleasedEvent(operatorStick, DriverStationConfig.JoystickButtons.ELEVATE_TWO);
		
		Event elevate_three_start = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.ELEVATE_THREE);
		Event elevate_three_abort = new JoystickReleasedEvent(operatorStick, DriverStationConfig.JoystickButtons.ELEVATE_THREE);
		
		Event elevate_four_start = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.ELEVATE_FOUR);
		Event elevate_four_abort = new JoystickReleasedEvent(operatorStick, DriverStationConfig.JoystickButtons.ELEVATE_FOUR);
		
		Event extend_carriage_start = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.EXTEND_CARRIAGE);
		Event extend_carriage_abort = new JoystickReleasedEvent(operatorStick, DriverStationConfig.JoystickButtons.EXTEND_CARRIAGE);
		
		Event release_stack_start = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.DEPLOY_STACK);
		Event release_stack_abort = new JoystickReleasedEvent(operatorStick, DriverStationConfig.JoystickButtons.DEPLOY_STACK);
		
		//map events to tasks
		to_auto.AddStartListener(auton);
		driver_stick_moved.AddAbortListener(auton);
		operator_stick_moved.AddAbortListener(auton);
		driver_stick_pressed.AddAbortListener(auton);
		operator_stick_pressed.AddAbortListener(auton);
		disabled_timeout.AddAbortListener(auton);
		
		collect_tote_start.AddStartListener(collect_tote);
		collect_tote_abort.AddAbortListener(collect_tote);
		
		collect_upright_container_start.AddStartListener(collect_upright_container);
		collect_upright_container_abort.AddAbortListener(collect_upright_container);

		extend_carriage_start.AddStartListener(extendCarriage);
		extend_carriage_abort.AddAbortListener(extendCarriage);
		
		release_stack_start.AddStartListener(releaseStack);
		release_stack_abort.AddAbortListener(releaseStack);
		
		
		load_tote_start.AddStartListener(load_tote);
		load_tote_abort.AddAbortListener(load_tote);
		
		load_container_start.AddStartListener(load_container);
		load_container_abort.AddAbortListener(load_container);
		
		load_upright_container_start.AddStartListener(load_upright_container);
		load_upright_container_abort.AddAbortListener(load_upright_container);
		
		elevate_one_start.AddStartListener(elevate_1);
		elevate_one_abort.AddAbortListener(elevate_1);
		
		elevate_two_start.AddStartListener(elevate_2);
		elevate_two_abort.AddAbortListener(elevate_2);
		
		elevate_three_start.AddStartListener(elevate_3);
		elevate_three_abort.AddAbortListener(elevate_3);
		
		elevate_four_start.AddStartListener(elevate_4);
		elevate_four_abort.AddAbortListener(elevate_4);
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
