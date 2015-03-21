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
import com.team846.frc2015.config.DriverStationConfig;
import com.team846.frc2015.driverstation.GameState;
import com.team846.frc2015.driverstation.LRTDriverStation;
import com.team846.frc2015.driverstation.LRTJoystick;

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
		LRTJoystick driverStick = LRTDriverStation.Instance().GetDriverStick();
		
		createInputProcessors();

		// All automation routines		
		//NOT BEING USED
		Automation auton = new Autonomous();
	
		Automation load_tote = new LoadTote();
		Automation load_sideways_container = new LoadSidewaysContainer();
		Automation load_upright_container = new LoadUprightContainer();
		Automation load_additional = new LoadAdditional();
		Automation load_stack = new LoadStack();
		Automation human_load = new HumanLoad();
		
		Automation releaseStack = new ReleaseStack();
		
		//rip scripted autonomous
//		Automation auton_fake = new Drive(96, 0.5, 3);
		
		//EMERGENCY UNCOMMENT
		Automation auton_fake = new TimeDrive(2.5,0.5);
		
		//Automation auton_fake = new Turn(90, 0.5, 3);
		
		Sequential auton_fake_yellowYOLO = new Sequential("yellowTote");
		auton_fake_yellowYOLO.AddAutomation(new LoadTote(true));
		auton_fake_yellowYOLO.AddAutomation(new Turn(90.0,0.5));
		auton_fake_yellowYOLO.AddAutomation(new Drive(12,0.5,3));
		auton_fake_yellowYOLO.AddAutomation(new Turn(90.0,0.5));
		Parallel dropAndPop = new Parallel("DropMoveBack");
		dropAndPop.AddAutomation(new ReleaseStack());
		dropAndPop.AddAutomation(new Drive(36, 0.5));
		auton_fake_yellowYOLO.AddAutomation(dropAndPop);
		
		Sequential auton_fake_container = new Sequential("container");
		
		Parallel driveCollect = new Parallel("DriveCollect");
		auton_fake_container.AddAutomation(new LoadUprightContainer(true));
		auton_fake_container.AddAutomation(new Drive(24,0.5,3, true));

		auton_fake_container.AddAutomation(driveCollect);
		auton_fake_container.AddAutomation(new Turn(90.0,0.5));
		auton_fake_container.AddAutomation(new Drive(116,0.5,3));
		auton_fake_container.AddAutomation(new Turn(90.0,0.5));
		Parallel greenDropAndPop = new Parallel("ContainerDropMoveBack");
		greenDropAndPop.AddAutomation(new ReleaseStack());
		greenDropAndPop.AddAutomation(new Drive(36, 0.5));
		auton_fake_container.AddAutomation(greenDropAndPop );
		
		// Declare event triggers
		Event to_auto = new GameModeChangeEvent(GameState.AUTONOMOUS);
		Event driver_stick_moved = new JoystickMovedEvent(driverStick);
		Event operator_stick_moved = new JoystickMovedEvent(operatorStick);
		Event driver_stick_pressed = new JoystickPressedEvent(driverStick);
		Event operator_stick_pressed = new JoystickPressedEvent(operatorStick);
		Event disabled_timeout = new DelayedEvent(new GameModeChangeEvent(GameState.DISABLED), 100);
		
		Event driverAbort = new JoystickPressedEvent(driverStick, 8);
		
		Event driverSweep = new JoystickPressedEvent(driverStick, DriverStationConfig.JoystickButtons.DRIVER_SWEEP);
		
		Event load_tote_start = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_TOTE);
		Event load_tote_abort = new JoystickReleasedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_TOTE);
		
		Event load_upright_container_start = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_UPRIGHT_CONTAINER); 
		Event load_upright_container_abort = new JoystickReleasedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_UPRIGHT_CONTAINER);
		
		Event load_sideways_container_start = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_SIDEWAYS_CONTAINER);
		Event load_sideways_container_abort = new JoystickReleasedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_SIDEWAYS_CONTAINER);

		Event load_additional_start = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_ADDITIONAL);
		Event load_additional_abort = new JoystickReleasedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_ADDITIONAL);

		Event load_stack_start = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_STACK);
		Event load_stack_abort = new JoystickReleasedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_STACK);
		
		Event human_load_start = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.HUMAN_LOAD_START);
		Event human_load_abort = new JoystickReleasedEvent(operatorStick, DriverStationConfig.JoystickButtons.HUMAN_LOAD_START);
		
		Event load_abort_deploy = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.EXTEND_CARRIAGE);
		Event load_abort_1 = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.ELEVATE_ONE);
		Event load_abort_2 = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.ELEVATE_TWO);
		Event load_abort_3 = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.ELEVATE_THREE);
		Event load_abort_4 = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.ELEVATE_FOUR);
		Event load_abort_step = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.ELEVATE_STEP);
		
		Event release_stack_start = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.DEPLOY_STACK);
		Event release_stack_abort = new JoystickReleasedEvent(operatorStick, DriverStationConfig.JoystickButtons.DEPLOY_STACK);
		
		//SHOULD BE ENABLED LATER
//		// Map events to routines
//		//to_auto.AddStartListener(auton);
//		driver_stick_moved.AddAbortListener(auton);
//		operator_stick_moved.AddAbortListener(auton);
//		driver_stick_pressed.AddAbortListener(auton);
//		operator_stick_pressed.AddAbortListener(auton);
//		driverAbort.AddAbortListener(auton);
////		disabled_timeout.AddAbortListener(auton);
		
		// Map events to routines
		//SCRIPTING IS A LIE
//		to_auto.AddStartListener(auton_fake);
		driver_stick_moved.AddAbortListener(auton_fake);
		operator_stick_moved.AddAbortListener(auton_fake);
		driver_stick_pressed.AddAbortListener(auton_fake);
		operator_stick_pressed.AddAbortListener(auton_fake);
		driverAbort.AddAbortListener(auton_fake);
//		disabled_timeout.AddAbortListener(auton);
		
		release_stack_start.AddStartListener(releaseStack);
		release_stack_abort.AddAbortListener(releaseStack);
		
		load_tote_start.AddStartListener(load_tote);
		load_tote_abort.AddAbortListener(load_tote);
		load_tote_start.AddAbortListener(load_tote);
		
		load_sideways_container_start.AddStartListener(load_sideways_container);
		load_sideways_container_abort.AddAbortListener(load_sideways_container);
		load_sideways_container_start.AddAbortListener(load_sideways_container);
		
		load_upright_container_start.AddStartListener(load_upright_container);
		load_upright_container_abort.AddAbortListener(load_upright_container);
		load_upright_container_start.AddAbortListener(load_upright_container);
		
		load_additional_start.AddStartListener(load_additional);
		load_additional_start.AddAbortListener(load_additional);
		load_additional_abort.AddAbortListener(load_additional);

		load_stack_start.AddStartListener(load_stack);
		load_stack_start.AddAbortListener(load_stack);
		load_stack_abort.AddAbortListener(load_stack);
		
		load_additional_abort.AddAbortListener(load_tote);
		load_additional_abort.AddAbortListener(load_sideways_container);
		load_additional_abort.AddAbortListener(load_upright_container);
		load_additional_abort.AddAbortListener(load_stack);

		human_load_start.AddStartListener(human_load);
		human_load_abort.AddAbortListener(human_load);
		
		release_stack_start.AddAbortListener(load_tote);
		release_stack_start.AddAbortListener(load_sideways_container);
		release_stack_start.AddAbortListener(load_upright_container);
		release_stack_start.AddAbortListener(human_load);
		release_stack_start.AddAbortListener(load_additional);
		release_stack_start.AddAbortListener(load_stack);
		
		load_abort_deploy.AddAbortListener(load_tote);
		load_abort_deploy.AddAbortListener(load_sideways_container);
		load_abort_deploy.AddAbortListener(load_upright_container);
		load_abort_deploy.AddAbortListener(human_load);
		load_abort_deploy.AddAbortListener(load_additional);
		load_abort_deploy.AddAbortListener(load_stack);

		
		load_abort_1.AddAbortListener(load_tote);
		load_abort_1.AddAbortListener(load_sideways_container);
		load_abort_1.AddAbortListener(load_upright_container);
		load_abort_1.AddAbortListener(human_load);
		load_abort_1.AddAbortListener(load_additional);
		load_abort_1.AddAbortListener(load_stack);
		
		load_abort_2.AddAbortListener(load_tote);
		load_abort_2.AddAbortListener(load_sideways_container);
		load_abort_2.AddAbortListener(load_upright_container);
		load_abort_2.AddAbortListener(human_load);
		load_abort_2.AddAbortListener(load_additional);
		load_abort_2.AddAbortListener(load_stack);

		
		load_abort_3.AddAbortListener(load_tote);
		load_abort_3.AddAbortListener(load_sideways_container);
		load_abort_3.AddAbortListener(load_upright_container);
		load_abort_3.AddAbortListener(human_load);
		load_abort_3.AddAbortListener(load_additional);
		load_abort_3.AddAbortListener(load_stack);

		load_abort_4.AddAbortListener(load_tote);
		load_abort_4.AddAbortListener(load_sideways_container);
		load_abort_4.AddAbortListener(load_upright_container);
		load_abort_4.AddAbortListener(human_load);
		load_abort_4.AddAbortListener(load_additional);
		load_abort_4.AddAbortListener(load_stack);

		
		load_abort_step.AddAbortListener(load_tote);
		load_abort_step.AddAbortListener(load_sideways_container);
		load_abort_step.AddAbortListener(load_upright_container);
		load_abort_step.AddAbortListener(human_load);
		load_abort_step.AddAbortListener(load_additional);
		load_abort_step.AddAbortListener(load_stack);
		
		driverSweep.AddAbortListener(load_tote);
		driverSweep.AddAbortListener(load_additional);
		driverSweep.AddAbortListener(load_sideways_container);
		driverSweep.AddAbortListener(human_load);
		driverSweep.AddAbortListener(load_upright_container);
		driverSweep.AddAbortListener(load_stack);
	}
	
	private void createInputProcessors() {
		inputs.add(new DrivetrainInputs(Axis.DRIVE));
		inputs.add(new DrivetrainInputs(Axis.TURN));
		inputs.add(new DrivetrainInputs(Axis.STRAFE));
		inputs.add(new CollectorArmInputs());
		inputs.add(new CollectorRollersInputs());
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
						it.remove(); 
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
