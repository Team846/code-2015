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
import com.team846.frc2015.automation.inputProcessors.ContainerArmInputs;
import com.team846.frc2015.automation.inputProcessors.DrivetrainInputs;
import com.team846.frc2015.automation.inputProcessors.ElevatorInputs;
import com.team846.frc2015.automation.inputProcessors.InputProcessor;
import com.team846.frc2015.automation.inputProcessors.DrivetrainInputs.Axis;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.config.DriverStationConfig;
import com.team846.frc2015.driverstation.GameState;
import com.team846.frc2015.driverstation.LRTDriverStation;
import com.team846.frc2015.driverstation.LRTJoystick;

public class Brain 
{
	private static Brain instance = null;
	
	private final ArrayList<InputProcessor> inputs = new ArrayList<InputProcessor>();
	ArrayList<Automation> automation = new ArrayList<Automation>();
	
	private final LinkedList<Automation> runningTasks = new LinkedList<Automation>();
	private final HashMap<Automation, Event> waitingTasks = new HashMap<Automation, Event>();
	
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
		Automation auton = new Autonomous();
	
		Automation load_tote = new LoadTote();
		Automation load_sideways_container = new LoadSidewaysContainer();
		Automation load_upright_container = new LoadUprightContainer();
		Automation load_additional = new LoadAdditional();
		Automation load_stack = new LoadStack();
		Automation load_continuous = new ContinuousLoad();
		Automation human_load = new HumanLoad();
		
		Automation releaseStack = new ReleaseStack();
		
		Sequential auton_fake = new Sequential("drive");
		auton_fake.AddAutomation(new Strafe(48, 1.0, 3));
		
//		Sequential auton_fake_yellowYOLO = new Sequential("yellowTote");
//		auton_fake_yellowYOLO.AddAutomation(new Turn(0));
//		Parallel driveAndLoad = new Parallel("DriveAndLoad");
//		driveAndLoad.AddAutomation(new LoadTote(true));
//		driveAndLoad.AddAutomation(new Drive(60, 0.5, 3));
//		auton_fake_yellowYOLO.AddAutomation(driveAndLoad);
//		auton_fake_yellowYOLO.AddAutomation(new Turn(-90.0, 0.5, 3));
//		auton_fake_yellowYOLO.AddAutomation(new Drive(108, 0.5,3));
//		auton_fake_yellowYOLO.AddAutomation(new Turn(-90.0, 0.5, 3));
//		Parallel dropAndPop = new Parallel("DropMoveBack", true);
//		dropAndPop.AddAutomation(new ReleaseStack());
//		dropAndPop.AddAutomation(new Drive(-120, 0.5, 3));
//		auton_fake_yellowYOLO.AddAutomation(dropAndPop);

		
		Sequential auton_fake_three = new Sequential("ThreeTote");
		auton_fake_three.AddAutomation(new ResetDrivetrainSetpoints());
		auton_fake_three.AddAutomation(new Turn(0));

		auton_fake_three.AddAutomation(new Elevate(ElevatorData.ElevatorSetpoint.HOME_TOTE));
		auton_fake_three.AddAutomation(new Turn(21, 0.8, 3));
		auton_fake_three.AddAutomation(new Strafe(108, 0.8, 6));
		auton_fake_three.AddAutomation(new Turn(21, 0.8, 3));

		Parallel driveAndDrop = new Parallel("DriveAndDrop");
		driveAndDrop.AddAutomation(new Drive(180, 0.5, 12, true));
		driveAndDrop.AddAutomation(new Elevate(ElevatorData.ElevatorSetpoint.COLLECT_ADDITIONAL));
		auton_fake_three.AddAutomation(driveAndDrop);

		Parallel driveAndLoad = new Parallel("DriveAndLoad");
		driveAndLoad.AddAutomation(new Drive(60, 0.5, 3));
		driveAndLoad.AddAutomation(new LoadAdditional(true));
		auton_fake_three.AddAutomation(driveAndLoad);
		
		auton_fake_three.AddAutomation(new Strafe(108, 0.8, 6));
		auton_fake_three.AddAutomation(new Turn(15, 0.8, 3));
		
		driveAndDrop = new Parallel("DriveAndDrop");
		driveAndDrop.AddAutomation(new Drive(180, 0.5, 12, true));
		driveAndDrop.AddAutomation(new Elevate(ElevatorData.ElevatorSetpoint.COLLECT_ADDITIONAL));
		auton_fake_three.AddAutomation(driveAndDrop);

		driveAndLoad = new Parallel("DriveAndLoad");
		driveAndLoad.AddAutomation(new Drive(72, 0.5, 3));
		driveAndLoad.AddAutomation(new LoadAdditional(true));
		auton_fake_three.AddAutomation(driveAndLoad);
		
		auton_fake_three.AddAutomation(new Turn(70, 0.8, 3));
		auton_fake_three.AddAutomation(new Drive(-180, 1.0, 6, true));
		
		Parallel dropAndDrive = new Parallel("DropAndDrive", true);
		dropAndDrive.AddAutomation(new ReleaseStack());
		dropAndDrive.AddAutomation(new Drive(-156, 0.8, 3));
		auton_fake_three.AddAutomation(dropAndDrive);
		
//		Parallel driveAndLoad = new Parallel("DriveAndLoad");
//		Sequential loadAndElevate = new Sequential("LoadAndElevate");
//		loadAndElevate.AddAutomation(new LoadTote(true));
//		loadAndElevate.AddAutomation(new Drive(-36, 0.8, 3));
////		loadAndElevate.AddAutomation(new Elevate(ElevatorData.ElevatorSetpoint.TOTE_3));
////		driveAndLoad.AddAutomation(new Drive(60, 0.5, 3));
//		driveAndLoad.AddAutomation(loadAndElevate);
//		auton_fake_three.AddAutomation(driveAndLoad);
//
//		auton_fake_three.AddAutomation(new Turn(-40, 0.8, 3));
//		
//		Parallel driveAndSweep = new Parallel("DriveAndSweep", true);
//		Sequential sweepDrive = new Sequential("SweepDrive");
//		sweepDrive.AddAutomation(new Drive(132, 0.8, 3, true));
////		sweepDrive.AddAutomation(new Drive(24, 0.5, 3, true));
////		sweepDrive.AddAutomation(new Drive(72, 0.5, 3, true));
//		driveAndSweep.AddAutomation(sweepDrive);
////		driveAndSweep.AddAutomation(new Sweep(Sweep.Direction.LEFT));
//		auton_fake_three.AddAutomation(driveAndSweep);
//
//		auton_fake_three.AddAutomation(new Turn(140, 0.8, 3));
//
//		Parallel driveAndDrop = new Parallel("DriveAndDrop");
//		driveAndDrop.AddAutomation(new Drive(144, 0.8, 6, true));
//		driveAndDrop.AddAutomation(new Elevate(ElevatorData.ElevatorSetpoint.COLLECT_ADDITIONAL));
//		auton_fake_three.AddAutomation(driveAndDrop);
//		
//		auton_fake_three.AddAutomation(new Turn(-60, 0.8, 3));
//		
//		driveAndLoad = new Parallel("DriveAndLoad");
//		loadAndElevate = new Sequential("LoadAndElevate");
//		loadAndElevate.AddAutomation(new LoadAdditional(true));
//		loadAndElevate.AddAutomation(new Drive(-36, 0.8, 3));
////		loadAndElevate.AddAutomation(new Elevate(ElevatorData.ElevatorSetpoint.TOTE_3));
//		driveAndLoad.AddAutomation(new Drive(60, 0.8, 3));
//		driveAndLoad.AddAutomation(loadAndElevate);
//		auton_fake_three.AddAutomation(driveAndLoad);
//
//		auton_fake_three.AddAutomation(new Turn(-40, 0.8, 3));
//		
//		driveAndSweep = new Parallel("DriveAndSweep", true);
//		sweepDrive = new Sequential("SweepDrive");
//		sweepDrive.AddAutomation(new Drive(132, 0.8, 3, true));
////		sweepDrive.AddAutomation(new Drive(24, 0.5, 3, true));
////		sweepDrive.AddAutomation(new Drive(72, 0.5, 3, true));
//		driveAndSweep.AddAutomation(sweepDrive);
////		driveAndSweep.AddAutomation(new Sweep(Sweep.Direction.LEFT));
//		auton_fake_three.AddAutomation(driveAndSweep);
//
//		auton_fake_three.AddAutomation(new Turn(140, 0.8, 3));
//		
//		driveAndDrop = new Parallel("DriveAndDrop");
//		driveAndDrop.AddAutomation(new Drive(144, 0.8, 6, true));
//		driveAndDrop.AddAutomation(new Elevate(ElevatorData.ElevatorSetpoint.COLLECT_ADDITIONAL));
//		auton_fake_three.AddAutomation(driveAndDrop);
//		
//		auton_fake_three.AddAutomation(new Turn(-60, 0.8, 3));
//		
//		driveAndLoad = new Parallel("DriveAndLoad");
//		Automation load = new LoadAdditional(true);
//		driveAndLoad.AddAutomation(new Drive(72, 0.8, 3));
//		driveAndLoad.AddAutomation(load);
//		auton_fake_three.AddAutomation(driveAndLoad);
//		
////		auton_fake_three.AddAutomation(new Strafe(1.5, 1.0));
//		auton_fake_three.AddAutomation(new ResetDrivetrainSetpoints());
//		
//		Parallel dropAndDrive = new Parallel("DropAndDrive", true);
//		dropAndDrive.AddAutomation(new ReleaseStack());
//		dropAndDrive.AddAutomation(new Drive(-120, 0.8, 3));
//		auton_fake_three.AddAutomation(dropAndDrive);
		
		
		
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
		
		Event driverSweep = new JoystickPressedEvent(driverStick, DriverStationConfig.JoystickButtons.DRIVER_SWEEP_LEFT);
		
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
		
		// Map events to routines
//		to_auto.AddStartListener(auton);
//		driver_stick_moved.AddAbortListener(auton);
//		operator_stick_moved.AddAbortListener(auton);
//		driver_stick_pressed.AddAbortListener(auton);
//		operator_stick_pressed.AddAbortListener(auton);
//		disabled_timeout.AddAbortListener(auton);
		
		// Map events to routines
		to_auto.AddStartListener(auton_fake_three);
		driver_stick_moved.AddAbortListener(auton_fake_three);
		operator_stick_moved.AddAbortListener(auton_fake_three);
		driver_stick_pressed.AddAbortListener(auton_fake_three);
		operator_stick_pressed.AddAbortListener(auton_fake_three);
//		disabled_timeout.AddAbortListener(auton_fake_three);
		
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

		human_load_start.AddStartListener(load_continuous);
		human_load_abort.AddAbortListener(load_continuous);
		human_load_start.AddAbortListener(load_continuous);
		
		load_additional_start.AddAbortListener(load_tote);
		load_additional_start.AddAbortListener(load_sideways_container);
		load_additional_start.AddAbortListener(load_upright_container);
		load_additional_start.AddAbortListener(load_stack);
		load_additional_start.AddAbortListener(load_continuous);
		
		human_load_start.AddAbortListener(load_tote);
		human_load_start.AddAbortListener(load_sideways_container);
		human_load_start.AddAbortListener(load_upright_container);
		human_load_start.AddAbortListener(load_stack);
		human_load_start.AddAbortListener(load_additional);
		
		release_stack_start.AddAbortListener(load_tote);
		release_stack_start.AddAbortListener(load_sideways_container);
		release_stack_start.AddAbortListener(load_upright_container);
		release_stack_start.AddAbortListener(human_load);
		release_stack_start.AddAbortListener(load_additional);
		release_stack_start.AddAbortListener(load_stack);
		release_stack_start.AddAbortListener(load_continuous);
		
		load_abort_deploy.AddAbortListener(load_tote);
		load_abort_deploy.AddAbortListener(load_sideways_container);
		load_abort_deploy.AddAbortListener(load_upright_container);
		load_abort_deploy.AddAbortListener(human_load);
		load_abort_deploy.AddAbortListener(load_additional);
		load_abort_deploy.AddAbortListener(load_stack);
		load_abort_deploy.AddAbortListener(load_continuous);
		
		load_abort_1.AddAbortListener(load_tote);
		load_abort_1.AddAbortListener(load_sideways_container);
		load_abort_1.AddAbortListener(load_upright_container);
		load_abort_1.AddAbortListener(human_load);
		load_abort_1.AddAbortListener(load_additional);
		load_abort_1.AddAbortListener(load_stack);
		load_abort_1.AddAbortListener(load_continuous);
		
		load_abort_2.AddAbortListener(load_tote);
		load_abort_2.AddAbortListener(load_sideways_container);
		load_abort_2.AddAbortListener(load_upright_container);
		load_abort_2.AddAbortListener(human_load);
		load_abort_2.AddAbortListener(load_additional);
		load_abort_2.AddAbortListener(load_stack);
		load_abort_2.AddAbortListener(load_continuous);
		
		load_abort_3.AddAbortListener(load_tote);
		load_abort_3.AddAbortListener(load_sideways_container);
		load_abort_3.AddAbortListener(load_upright_container);
		load_abort_3.AddAbortListener(human_load);
		load_abort_3.AddAbortListener(load_additional);
		load_abort_3.AddAbortListener(load_stack);
		load_abort_3.AddAbortListener(load_continuous);

		load_abort_4.AddAbortListener(load_tote);
		load_abort_4.AddAbortListener(load_sideways_container);
		load_abort_4.AddAbortListener(load_upright_container);
		load_abort_4.AddAbortListener(human_load);
		load_abort_4.AddAbortListener(load_additional);
		load_abort_4.AddAbortListener(load_stack);
		load_abort_4.AddAbortListener(load_continuous);
		
		load_abort_step.AddAbortListener(load_tote);
		load_abort_step.AddAbortListener(load_sideways_container);
		load_abort_step.AddAbortListener(load_upright_container);
		load_abort_step.AddAbortListener(human_load);
		load_abort_step.AddAbortListener(load_additional);
		load_abort_step.AddAbortListener(load_stack);
		load_abort_step.AddAbortListener(load_continuous);
		
		driverSweep.AddAbortListener(load_tote);
		driverSweep.AddAbortListener(load_additional);
		driverSweep.AddAbortListener(load_sideways_container);
		driverSweep.AddAbortListener(human_load);
		driverSweep.AddAbortListener(load_upright_container);
		driverSweep.AddAbortListener(load_stack);
		driverSweep.AddAbortListener(load_continuous);
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
		inputs.add(new ContainerArmInputs());
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
			Map.Entry<Automation, Event> e = it.next();
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
