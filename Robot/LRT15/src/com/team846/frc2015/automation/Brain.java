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
import com.team846.frc2015.componentData.CollectorArmData;
import com.team846.frc2015.componentData.CollectorRollersData;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.oldconfig.ConfigPortMappings;
import com.team846.frc2015.oldconfig.ConfigRuntime;
import com.team846.frc2015.oldconfig.DriverStationConfig;
import com.team846.frc2015.driverstation.GameState;
import com.team846.frc2015.driverstation.LRTDriverStation;
import com.team846.frc2015.driverstation.LRTJoystick;
import com.team846.frc2015.oldconfig.RobotConfig;
import com.team846.frc2015.sensors.SensorFactory;
import edu.wpi.first.wpilibj.AnalogInput;

public class Brain {
    private static Brain instance = null;

    private final ArrayList<InputProcessor> inputs = new ArrayList<InputProcessor>();
    ArrayList<Automation> automation = new ArrayList<Automation>();

    private final LinkedList<Automation> runningTasks = new LinkedList<Automation>();
    private final HashMap<Automation, Event> waitingTasks = new HashMap<Automation, Event>();

    public static Brain Instance() {
        if (instance == null)
            instance = new Brain();
        return instance;
    }

    public static void Initialize() {
        if (instance == null)
            instance = new Brain();
    }

    private Brain() {
        LRTJoystick operatorStick = LRTDriverStation.instance().getOperatorStick();
        LRTJoystick driverStick = LRTDriverStation.instance().getDriverStick();

        createInputProcessors();

//        double delay = 0.0;
        double driveSpeed = 0.25;// untested 0.3;
//        double turnSpeed = 0.25;
//
//        double[] turnAngles = {-80.0, 100.0, -70.0, 70.0, -30.0};
//        double[] driveTicks = {5000.0, 8500.0, 6500.0, 8500.0};

        // BEGIN AUTONOMOUS ROUTINE
        Sequential auton = new Sequential(
                "auto",
                new Elevate(3),
                new Parallel(
                        "sweep1",
                        new Drive(12000, driveSpeed),
                        new Sweep(Sweep.Direction.LEFT, 30) // 1.5 second
                ),
                new LoadAdditional(true, ElevatorData.ElevatorSetpoint.TOTE_3),
                new Parallel(
                        "sweep2",
                        new Drive(13000, driveSpeed),
                        new Sweep(Sweep.Direction.LEFT, 40) // 2 seconds
                ),
                new Automation() {
                    private final CollectorArmData armData = CollectorArmData.get();
                    private final CollectorRollersData rollersData = CollectorRollersData.get();
                    AnalogInput sensor = SensorFactory.getAnalogInput(ConfigPortMappings.Instance().get("Analog/COLLECTOR_PROXIMITY"));

                    @Override
                    protected void AllocateResources() {}

                    @Override
                    protected boolean Start() { return true; }

                    @Override
                    protected boolean Abort() {
                        return true;
                    }

                    @Override
                    protected boolean Run() {
                        armData.setDesiredPosition(CollectorArmData.ArmPosition.EXTEND);
                        rollersData.setDirection(CollectorRollersData.Direction.INTAKE);
                        rollersData.setRunning(true);
                        rollersData.setSpeed(1.0);

                        return sensor.getAverageValue() > ConfigRuntime.Instance().Get("LoadTote", "analog_tote_value", 1600);
                    }
                },
                new Parallel(
                    "strafedrop",
                    new Strafe(200000, driveSpeed + 0.5 /* untested 1.0 */, 5.0) {
                        private final CollectorArmData armData = CollectorArmData.get();
                        private final CollectorRollersData rollersData = CollectorRollersData.get();

                        @Override
                        protected boolean Run() {
                            armData.setDesiredPosition(CollectorArmData.ArmPosition.EXTEND);
                            rollersData.setDirection(CollectorRollersData.Direction.INTAKE);
                            rollersData.setRunning(true);
                            rollersData.setSpeed(1.0);
                            return super.Run();
                        }
                    },
                    new Elevate(ElevatorData.ElevatorSetpoint.TOTE_1)
                ),
                new Parallel(
                    "releasedrive",
                    new ReleaseStack(),
                    new Drive(-8000, driveSpeed)
                )
        );

//        auton.AddAutomation(new LoadTote(true, ElevatorData.ElevatorSetpoint.TOTE_3));

//        auton.AddAutomation(new Elevate(3));
        // END AUTONOMOUS ROUTINE


//        auton.AddAutomation(new Turn(turnAngles[0], turnSpeed)); // degrees
//        auton.AddAutomation(new Pause(delay));
//
//        auton.AddAutomation(new Pause(delay));
//        auton.AddAutomation(new Turn(turnAngles[1], turnSpeed)); // degrees
//        auton.AddAutomation(new Pause(delay));
//
//        auton.AddAutomation(new Drive(driveTicks[1], driveSpeed)); // encoder ticks
//        auton.AddAutomation(new Pause(delay));
//        auton.AddAutomation(new LoadTote(true));

//        auton.AddAutomation(new Turn(turnAngles[2], turnSpeed)); // degrees
//        auton.AddAutomation(new Pause(delay));
//        auton.AddAutomation(new Drive(driveTicks[2], driveSpeed)); // encoder ticks
//        auton.AddAutomation(new Pause(delay));
//        auton.AddAutomation(new Turn(turnAngles[3], turnSpeed)); // degrees
//        auton.AddAutomation(new Pause(delay));
//        auton.AddAutomation(new Drive(driveTicks[3], driveSpeed)); // encoder ticks
//        auton.AddAutomation(new Pause(delay));
//        auton.AddAutomation(new Turn(turnAngles[4], turnSpeed)); // degrees
//        auton.AddAutomation(new Pause(delay));
//        auton.AddAutomation(new Strafe(120000, driveSpeed, 5.0)); // degrees

        Automation load_tote = new LoadTote();
        Automation load_sideways_container = new LoadSidewaysContainer();
        Automation load_upright_container = new LoadUprightContainer();
        Automation load_additional = new LoadAdditional();
        Automation load_stack = new LoadStack();
        Automation load_continuous = new ContinuousLoad();
        Automation human_load = new HumanLoad();
        Automation releaseStack = new ReleaseStack();

        // Declare event triggers
        Event to_auto = new GameModeChangeEvent(GameState.AUTONOMOUS);
        Event driver_stick_moved = new JoystickMovedEvent(driverStick);
        Event operator_stick_moved = new JoystickMovedEvent(operatorStick);
        Event driver_stick_pressed = new JoystickPressedEvent(driverStick);
        Event operator_stick_pressed = new JoystickPressedEvent(operatorStick);
        Event disabled_timeout = new DelayedEvent(new GameModeChangeEvent(GameState.DISABLED), 100);

        Event driverSweep = new JoystickPressedEvent(driverStick, DriverStationConfig.JoystickButtons.DRIVER_SWEEP_LEFT);

//        Event load_tote_start = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_TOTE);
//        Event load_tote_abort = new JoystickReleasedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_TOTE);

        Event load_upright_container_start = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_UPRIGHT_CONTAINER);
        Event load_upright_container_abort = new JoystickReleasedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_UPRIGHT_CONTAINER);

        Event load_sideways_container_start = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_SIDEWAYS_CONTAINER);
        Event load_sideways_container_abort = new JoystickReleasedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_SIDEWAYS_CONTAINER);

        Event load_additional_start = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_ADDITIONAL);
        Event load_additional_abort = new JoystickReleasedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_ADDITIONAL);

        Event load_stack_start = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_STACK);
        Event load_stack_abort = new JoystickReleasedEvent(operatorStick, DriverStationConfig.JoystickButtons.LOAD_STACK);

        // Event human_load_start = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.HUMAN_LOAD_START);
        // Event human_load_abort = new JoystickReleasedEvent(operatorStick, DriverStationConfig.JoystickButtons.HUMAN_LOAD_START);

        Event load_abort_deploy = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.EXTEND_CARRIAGE);
        Event load_abort_1 = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.ELEVATE_ONE);
        Event load_abort_2 = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.ELEVATE_TWO);
        Event load_abort_3 = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.ELEVATE_THREE);
        Event load_abort_4 = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.ELEVATE_FOUR);
        Event load_abort_step = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.ELEVATE_STEP);

        Event release_stack_start = new JoystickPressedEvent(operatorStick, DriverStationConfig.JoystickButtons.DEPLOY_STACK);
        Event release_stack_abort = new JoystickReleasedEvent(operatorStick, DriverStationConfig.JoystickButtons.DEPLOY_STACK);

        // Map events to routines
        to_auto.addStartListener(auton);
        driver_stick_moved.addAbortListener(auton);
        operator_stick_moved.addAbortListener(auton);
        driver_stick_pressed.addAbortListener(auton);
        operator_stick_pressed.addAbortListener(auton);
//        disabled_timeout.addAbortListener(auton);

        release_stack_start.addStartListener(releaseStack);
        release_stack_abort.addAbortListener(releaseStack);

//        load_tote_start.addStartListener(load_tote);
//        load_tote_abort.addAbortListener(load_tote);
//        load_tote_start.addAbortListener(load_tote);

        load_sideways_container_start.addStartListener(load_sideways_container);
        load_sideways_container_abort.addAbortListener(load_sideways_container);
        load_sideways_container_start.addAbortListener(load_sideways_container);

        load_upright_container_start.addStartListener(load_upright_container);
        load_upright_container_abort.addAbortListener(load_upright_container);
        load_upright_container_start.addAbortListener(load_upright_container);

        load_additional_start.addStartListener(load_additional);
        load_additional_start.addAbortListener(load_additional);
        load_additional_abort.addAbortListener(load_additional);

        load_stack_start.addStartListener(load_stack);
        load_stack_start.addAbortListener(load_stack);
        load_stack_abort.addAbortListener(load_stack);

        // human_load_start.addStartListener(load_continuous);
        // human_load_abort.addAbortListener(load_continuous);
        // human_load_start.addAbortListener(load_continuous);

        load_additional_start.addAbortListener(load_tote);
        load_additional_start.addAbortListener(load_sideways_container);
        load_additional_start.addAbortListener(load_upright_container);
        load_additional_start.addAbortListener(load_stack);
        load_additional_start.addAbortListener(load_continuous);

        // human_load_start.addAbortListener(load_tote);
        // human_load_start.addAbortListener(load_sideways_container);
        // human_load_start.addAbortListener(load_upright_container);
        // human_load_start.addAbortListener(load_stack);
        // human_load_start.addAbortListener(load_additional);

        release_stack_start.addAbortListener(load_tote);
        release_stack_start.addAbortListener(load_sideways_container);
        release_stack_start.addAbortListener(load_upright_container);
        release_stack_start.addAbortListener(human_load);
        release_stack_start.addAbortListener(load_additional);
        release_stack_start.addAbortListener(load_stack);
        release_stack_start.addAbortListener(load_continuous);

        load_abort_deploy.addAbortListener(load_tote);
        load_abort_deploy.addAbortListener(load_sideways_container);
        load_abort_deploy.addAbortListener(load_upright_container);
        load_abort_deploy.addAbortListener(human_load);
        load_abort_deploy.addAbortListener(load_additional);
        load_abort_deploy.addAbortListener(load_stack);
        load_abort_deploy.addAbortListener(load_continuous);

        load_abort_1.addAbortListener(load_tote);
        load_abort_1.addAbortListener(load_sideways_container);
        load_abort_1.addAbortListener(load_upright_container);
        load_abort_1.addAbortListener(human_load);
        load_abort_1.addAbortListener(load_additional);
        load_abort_1.addAbortListener(load_stack);
        load_abort_1.addAbortListener(load_continuous);

        load_abort_2.addAbortListener(load_tote);
        load_abort_2.addAbortListener(load_sideways_container);
        load_abort_2.addAbortListener(load_upright_container);
        load_abort_2.addAbortListener(human_load);
        load_abort_2.addAbortListener(load_additional);
        load_abort_2.addAbortListener(load_stack);
        load_abort_2.addAbortListener(load_continuous);

        load_abort_3.addAbortListener(load_tote);
        load_abort_3.addAbortListener(load_sideways_container);
        load_abort_3.addAbortListener(load_upright_container);
        load_abort_3.addAbortListener(human_load);
        load_abort_3.addAbortListener(load_additional);
        load_abort_3.addAbortListener(load_stack);
        load_abort_3.addAbortListener(load_continuous);

        load_abort_4.addAbortListener(load_tote);
        load_abort_4.addAbortListener(load_sideways_container);
        load_abort_4.addAbortListener(load_upright_container);
        load_abort_4.addAbortListener(human_load);
        load_abort_4.addAbortListener(load_additional);
        load_abort_4.addAbortListener(load_stack);
        load_abort_4.addAbortListener(load_continuous);

        load_abort_step.addAbortListener(load_tote);
        load_abort_step.addAbortListener(load_sideways_container);
        load_abort_step.addAbortListener(load_upright_container);
        load_abort_step.addAbortListener(human_load);
        load_abort_step.addAbortListener(load_additional);
        load_abort_step.addAbortListener(load_stack);
        load_abort_step.addAbortListener(load_continuous);

        driverSweep.addAbortListener(load_tote);
        driverSweep.addAbortListener(load_additional);
        driverSweep.addAbortListener(load_sideways_container);
        driverSweep.addAbortListener(human_load);
        driverSweep.addAbortListener(load_upright_container);
        driverSweep.addAbortListener(load_stack);
        driverSweep.addAbortListener(load_continuous);
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

    public void Update() {
        ProcessAutomationTasks();

        ProcessInputs();

        for (Event e : Event.event_vector) {
            e.update();
        }
    }

    void ProcessAutomationTasks() {

        // Try to start queued tasks
        Iterator<Map.Entry<Automation, Event>> it = waitingTasks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Automation, Event> e = it.next();
            if (!runningTasks.contains(e.getKey())) // If task isn't running
            {
                if (e.getKey().CheckResources()) {
                    boolean ret = e.getKey().StartAutomation(e.getValue());
                    if (ret) {
                        runningTasks.add(e.getKey());
                        it.remove();
                    }
                }
            }

        }

        // Start/Abort/Continue tasks which had their events fired
        //Cant use nice nested foreach bc java is stupid
        for (int i = 0; i < Event.event_vector.size(); i++) {
            Event event = Event.event_vector.get(i);
            if (event.fired()) {
                // Tasks aborted by this event
                for (Automation a : event.getAbortListeners()) {
                    if (runningTasks.contains(a)) // If task is running
                    {
                        boolean ret = a.AbortAutomation(event);
                        if (ret) {
                            a.DeallocateResources();
                            runningTasks.remove(a);
                        }
                    }
                }

                // Tasks started by this event
                for (Automation auto : event.getStartListeners()) {
                    if (!runningTasks.contains(auto) || auto.IsRestartable()) // If task isn't running or is restartable
                    {

                        if (auto.CheckResources()) {
                            boolean ret = auto.StartAutomation(event);
                            if (ret)
                                runningTasks.add(auto);
                        } else {
                            if (auto.QueueIfBlocked())
                                waitingTasks.put(auto, event);
                        }
                    }
                }

                // Tasks continued by this event
                for (Automation a : event.getContinueListeners()) {
                    if (runningTasks.contains(a))//!= runningTasks.end()) // If task is running
                    {
                        a.ContinueAutomation(event);
                    }
                }
            }
        }

        // update running tasks
        ListIterator<Automation> runningTaskIterator = runningTasks.listIterator();
        while (runningTaskIterator.hasNext()) {
            Automation a = runningTaskIterator.next();
            boolean complete = a.Update();
            if (complete) {
                a.DeallocateResources();
                runningTaskIterator.remove();
                //runningTaskIterator.previous();
            }

        }

    }

    void ProcessInputs() {
        for (InputProcessor i : inputs) {
            if (i.CheckResources()) {
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
