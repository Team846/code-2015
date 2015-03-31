package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.CarriageExtenderData;
import com.team846.frc2015.componentData.CarriageHooksData;
import com.team846.frc2015.componentData.CollectorArmData;
import com.team846.frc2015.componentData.CollectorArmData.ArmPosition;
import com.team846.frc2015.componentData.CollectorRollersData;
import com.team846.frc2015.componentData.CollectorRollersData.Direction;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.componentData.CarriageHooksData.HookState;
import com.team846.frc2015.componentData.ElevatorData.ElevatorControlMode;
import com.team846.frc2015.componentData.ElevatorData.ElevatorSetpoint;
import com.team846.frc2015.config.ConfigRuntime;
import com.team846.frc2015.config.Configurable;

public class ReleaseStack extends Automation implements Configurable {

	private final ElevatorData elevatorData;
	private int startingPosition;
	private int dropHeight;
	private final CarriageHooksData hooksData;
	private final CarriageExtenderData extenderData;
	private final CollectorArmData collectorArmData;
	private final CollectorRollersData collectorRollersData;
	private boolean elevatorToHome;
	private boolean spit;

	public ReleaseStack(boolean spit) {
		super("ReleaseStack", RoutineOption.REQUIRES_ABORT_CYCLES);
		
		elevatorData = ElevatorData.get();
		hooksData = CarriageHooksData.get();
		extenderData = CarriageExtenderData.get();
		collectorArmData = CollectorArmData.get();
		collectorRollersData = CollectorRollersData.get();
		
		dropHeight = 0;
		elevatorToHome = false;
		
		this.spit = spit;
		
		ConfigRuntime.Register(this);
	}
	
	public ReleaseStack()
	{
		this(false);
	}

	@Override
	public void AllocateResources() {
		AllocateResource(ControlResource.ELEVATOR);
		AllocateResource(ControlResource.CARRIAGE_EXTENDER);
		AllocateResource(ControlResource.CARRIAGE_HOOKS);
		AllocateResource(ControlResource.COLLECTOR_ARMS);
		AllocateResource(ControlResource.COLLECTOR_ROLLERS);
	}

	@Override
	protected boolean Start() {
		startingPosition = elevatorData.getCurrentPosition();
		elevatorToHome = false;
		return true;
	}

	@Override
	protected boolean Abort() {
		return true;
	}

	@Override
	protected boolean Run() {
		if (!spit)
			collectorArmData.setDesiredPosition(ArmPosition.STOWED);
		else
			collectorArmData.setDesiredPosition(ArmPosition.EXTEND);
		
		elevatorData.setControlMode(ElevatorControlMode.POSITION);
		elevatorData.setDesiredPosition((startingPosition + dropHeight)); // down is positive
		if(elevatorData.isAtPosition(startingPosition + dropHeight) || elevatorToHome)
		{
			elevatorToHome = true;
			hooksData.setBackHooksDesiredState(HookState.UP);
			hooksData.setFrontHooksDesiredState(HookState.UP);
			
			extenderData.setControlMode(CarriageExtenderData.CarriageControlMode.POSITION);
			extenderData.setPositionSetpoint(0.0);
			
			if (spit)
			{
				collectorRollersData.setDirection(Direction.REVERSE);
				collectorRollersData.setRunning(true);
				collectorRollersData.setSpeed(1.0);
			}
		}
		if (Aborting())
		{
			elevatorData.setControlMode(ElevatorControlMode.SETPOINT);
			elevatorData.setSetpoint(ElevatorSetpoint.HOME_TOTE);
			hooksData.setBackHooksDesiredState(HookState.DOWN);
			hooksData.setFrontHooksDesiredState(HookState.DOWN);
			if (elevatorData.isAtSetpoint(ElevatorSetpoint.HOME_TOTE))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public void Configure() {
		dropHeight = GetConfig("dropHeight", 400);
	}

}
