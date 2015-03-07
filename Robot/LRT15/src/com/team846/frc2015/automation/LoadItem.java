package com.team846.frc2015.automation;

import com.team846.frc2015.automation.events.JoystickReleasedEvent;
import com.team846.frc2015.componentData.CarriageHooksData;
import com.team846.frc2015.componentData.CollectorArmData;
import com.team846.frc2015.componentData.CollectorArmData.ArmPosition;
import com.team846.frc2015.componentData.CollectorRollersData.Direction;
import com.team846.frc2015.componentData.CollectorRollersData;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.componentData.CarriageHooksData.HookState;
import com.team846.frc2015.componentData.ElevatorData.ElevatorControlMode;
import com.team846.frc2015.componentData.ElevatorData.ElevatorSetpoint;
import com.team846.frc2015.config.ConfigPortMappings;
import com.team846.frc2015.config.ConfigRuntime;
import com.team846.frc2015.config.Configurable;
import com.team846.frc2015.config.DriverStationConfig;
import com.team846.frc2015.driverstation.LRTDriverStation;
import com.team846.frc2015.driverstation.LRTJoystick;
import com.team846.frc2015.sensors.SensorFactory;

import edu.wpi.first.wpilibj.AnalogInput;

public abstract class LoadItem extends Automation{

	private ElevatorData elevatorData;
	private CarriageHooksData hooksData;
	private CollectorArmData armData;
	private CollectorRollersData rollersData;
	
	private ElevatorSetpoint collect;
	private ElevatorSetpoint grab;
	private ElevatorSetpoint home;
	
	private LRTJoystick driverStick;
	private int requiredWaitCycles;
	private int waitTicks;
	
	protected boolean hasItem = false;
	
	AnalogInput sensor;
	
	protected int analogThreshold = 0;
	
	enum State
	{
		COLLECT,
		GRAB,
		WAIT,
		HOME
	}
	protected State state;
	private boolean auto;

	public LoadItem(String name, ElevatorSetpoint collectSetpoint, ElevatorSetpoint grabSetpoint, ElevatorSetpoint homeSetpoint, boolean auto) {
		this(name, collectSetpoint, grabSetpoint, homeSetpoint, 20, auto);
	}
	
	public LoadItem(String name, ElevatorSetpoint collectSetpoint, ElevatorSetpoint grabSetpoint, ElevatorSetpoint homeSetpoint, int waitCycles, boolean auto) {
		super(name);
		state = State.COLLECT;
		this.auto = auto; 
		elevatorData = ElevatorData.get();
		hooksData = CarriageHooksData.get();
		armData = CollectorArmData.get();
		rollersData = CollectorRollersData.get();
		driverStick = LRTDriverStation.Instance().GetDriverStick();
		
		sensor = SensorFactory.GetAnalogInput(ConfigPortMappings.Instance().get("Analog/COLLECTOR_PROXIMITY"));
		collect = collectSetpoint;
		grab = grabSetpoint;
		home = homeSetpoint;
		
		this.requiredWaitCycles = waitCycles;
		waitTicks = 0;
	}

	@Override
	public void AllocateResources() {
		AllocateResource(ControlResource.COLLECTOR_ARMS);
		AllocateResource(ControlResource.COLLECTOR_ROLLERS);
		AllocateResource(ControlResource.CARRIAGE_HOOKS);
		AllocateResource(ControlResource.ELEVATOR);
	}

	@Override
	protected boolean Start() {
		state = State.COLLECT;
		hasItem = false;
		waitTicks = 0;
		return true;
	}

	@Override
	protected boolean Abort() {
		elevatorData.setControlMode(ElevatorControlMode.VELOCITY);
		elevatorData.setDesiredSpeed(0.0);
		
		armData.setDesiredPosition(ArmPosition.STOWED);
		rollersData.setRunning(false);
		
		hooksData.setBackHooksDesiredState(HookState.DOWN);
		hooksData.setFrontHooksDesiredState(HookState.DOWN);
		return true;
	}

	@Override
	protected boolean Run() {
		armData.setDesiredPosition(ArmPosition.STOWED);
		rollersData.setRunning(false);
		
		switch(state)
		{
			case COLLECT:
			{
				hooksData.setBackHooksDesiredState(HookState.DOWN);
				hooksData.setFrontHooksDesiredState(HookState.DOWN);
				
				
				
				elevatorData.setControlMode(ElevatorControlMode.SETPOINT);
				elevatorData.setSetpoint(collect);

				if ((driverStick.IsButtonDown(DriverStationConfig.JoystickButtons.COLLECT)
						/*&& hasItem*/) || auto)
				{
					
					armData.setDesiredPosition(ArmPosition.EXTEND);
					rollersData.setRunning(true);
					rollersData.setDirection(Direction.INTAKE);
					rollersData.setSpeed(1.0);
					System.out.println(sensor.getAverageValue());
					if(sensor.getAverageValue() > analogThreshold)
					{
						
						hasItem = true;
						state = State.GRAB;
					}
				}
					
				break;
			}
			case GRAB:
			{
				hooksData.setBackHooksDesiredState(HookState.UP);
				hooksData.setFrontHooksDesiredState(HookState.UP);
				elevatorData.setControlMode(ElevatorControlMode.SETPOINT);
				elevatorData.setSetpoint(grab);
				if (elevatorData.isAtSetpoint(grab))
				{
					hooksData.setBackHooksDesiredState(HookState.DOWN);
					hooksData.setFrontHooksDesiredState(HookState.DOWN);
					state = State.WAIT;
				}
				break;
			}
			case WAIT:
			{
				if (waitTicks++ > requiredWaitCycles)
				{
					state = State.HOME;
				}
				break;
			}
			case HOME:
			{
				hooksData.setBackHooksDesiredState(HookState.DOWN);
				hooksData.setFrontHooksDesiredState(HookState.DOWN);
				elevatorData.setControlMode(ElevatorControlMode.SETPOINT);
				elevatorData.setSetpoint(home);
				break;
			}
		}
		System.out.println(state);
		if(auto && elevatorData.isAtSetpoint(home))
			return true;
		
		return false;
	}
	
	protected void setAnalogThreshold(int a)
	{
		analogThreshold = a;
	}
}
