package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.CarriageExtenderData;
import com.team846.frc2015.componentData.CarriageHooksData;
import com.team846.frc2015.componentData.CollectorArmData;
import com.team846.frc2015.componentData.CollectorArmData.ArmPosition;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.componentData.CarriageHooksData.HookState;
import com.team846.frc2015.componentData.ElevatorData.ElevatorControlMode;
import com.team846.frc2015.componentData.ElevatorData.ElevatorSetpoint;
import com.team846.frc2015.config.ConfigRuntime;
import com.team846.frc2015.config.Configurable;

public class ReleaseStack extends Automation implements Configurable {

	private ElevatorData elevatorData;
	private int startingPosition;
	private int dropHeight;
	private CarriageHooksData hooksData;
	private CarriageExtenderData extenderData;
	private CollectorArmData collectorArmData;
	private boolean elevatorToHome;

	public ReleaseStack() {
		super("ReleaseStack", RoutineOption.REQUIRES_ABORT_CYCLES);
		
		elevatorData = ElevatorData.get();
		hooksData = CarriageHooksData.get();
		extenderData = CarriageExtenderData.get();
		collectorArmData = CollectorArmData.get();
		
		dropHeight = 0;
		elevatorToHome = false;
		
		ConfigRuntime.Register(this);
	}

	@Override
	public void AllocateResources() {
		AllocateResource(ControlResource.ELEVATOR);
		AllocateResource(ControlResource.CARRIAGE_EXTENDER);
		AllocateResource(ControlResource.CARRIAGE_HOOKS);
		AllocateResource(ControlResource.COLLECTOR_ARMS);
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
		collectorArmData.setDesiredPosition(ArmPosition.STOWED);
		elevatorData.setControlMode(ElevatorControlMode.POSITION);
		elevatorData.setDesiredPosition((startingPosition + dropHeight)); // down is positive
		if(elevatorData.isAtPosition(startingPosition + dropHeight) || elevatorToHome)
		{
			elevatorToHome = true;
			hooksData.setBackHooksDesiredState(HookState.UP);
			hooksData.setFrontHooksDesiredState(HookState.UP);
			
			extenderData.setControlMode(CarriageExtenderData.CarriageControlMode.POSITION);
			extenderData.setPositionSetpoint(0.0);
			
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
		}
		return false;
	}

	@Override
	public void Configure() {
		dropHeight = GetConfig("dropHeight", 20); //TODO:Change default once pot comes in
	}

}
