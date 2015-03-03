package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.CarriageHooksData;
import com.team846.frc2015.componentData.CollectorArmData;
import com.team846.frc2015.componentData.CollectorArmData.ArmPosition;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.componentData.CarriageHooksData.HookState;
import com.team846.frc2015.componentData.ElevatorData.ElevatorControlMode;
import com.team846.frc2015.componentData.ElevatorData.ElevatorSetpoint;
import com.team846.frc2015.config.DriverStationConfig;
import com.team846.frc2015.driverstation.LRTDriverStation;
import com.team846.frc2015.driverstation.LRTJoystick;

public class LoadItem extends Automation {

	private ElevatorData elevatorData;
	private CarriageHooksData hooksData;
	private CollectorArmData armData;
	private ElevatorSetpoint collect;
	private ElevatorSetpoint grab;
	private ElevatorSetpoint home;
	private LRTJoystick driverStick;
	private int waitCycles;
	private int waitTicks;
	
	enum State
	{
		COLLECT,
		GRAB,
		WAIT,
		HOME
	}
	protected State state;

	public LoadItem(String name, ElevatorSetpoint collectSetpoint, ElevatorSetpoint grabSetpoint, ElevatorSetpoint homeSetpoint) {
		this(name, collectSetpoint, grabSetpoint, homeSetpoint, 20);
	}
	
	
	public LoadItem(String name, ElevatorSetpoint collectSetpoint, ElevatorSetpoint grabSetpoint, ElevatorSetpoint homeSetpoint, int waitCycles) {
		super(name);
		state = State.COLLECT;
		elevatorData = ElevatorData.get();
		hooksData = CarriageHooksData.get();
		armData = CollectorArmData.get();
		driverStick = LRTDriverStation.Instance().GetDriverStick();
		collect = collectSetpoint;
		grab = grabSetpoint;
		home = homeSetpoint;
		this.waitCycles = waitCycles;
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
		waitTicks = 0;
		return true;
	}

	@Override
	protected boolean Abort() {
		elevatorData.setControlMode(ElevatorControlMode.VELOCITY);
		elevatorData.setDesiredSpeed(0.0);
		hooksData.setBackHooksDesiredState(HookState.DOWN);
		hooksData.setFrontHooksDesiredState(HookState.DOWN);
		return true;
	}

	@Override
	protected boolean Run() {
		armData.setDesiredPosition(ArmPosition.STOWED);
		
		switch(state)
		{
			case COLLECT:
			{
				hooksData.setBackHooksDesiredState(HookState.DOWN);
				hooksData.setFrontHooksDesiredState(HookState.DOWN);
				
				elevatorData.setControlMode(ElevatorControlMode.SETPOINT);
				elevatorData.setSetpoint(collect);
				if (driverStick.IsButtonJustPressed(DriverStationConfig.JoystickButtons.COLLECT))
					state = State.GRAB;
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
				if (waitTicks++ > waitCycles)
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
		
		return false;
	}
}
