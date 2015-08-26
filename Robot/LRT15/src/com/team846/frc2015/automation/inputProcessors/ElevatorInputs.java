package com.team846.frc2015.automation.inputProcessors;

import com.team846.frc2015.automation.ControlResource;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.componentData.ElevatorData.ElevatorControlMode;
import com.team846.frc2015.componentData.ElevatorData.ElevatorSetpoint;
import com.team846.frc2015.config.DriverStationConfig;
import com.team846.frc2015.driverstation.LRTDriverStation;
import com.team846.frc2015.driverstation.LRTJoystick;

import edu.wpi.first.wpilibj.Joystick.AxisType;

public class ElevatorInputs extends InputProcessor {

	private final ElevatorData elevatorData;
	private final LRTJoystick operatorStick;
	
	public ElevatorInputs()
	{
		elevatorData = ElevatorData.get();
		operatorStick = LRTDriverStation.instance().getOperatorStick();
		
		RegisterResource(ControlResource.ELEVATOR);
	}
	
	@Override
	public void Update() {
//		elevatorData.setControlMode(ElevatorControlMode.SETPOINT);
//		elevatorData.setSetpoint(ElevatorSetpoint.HOME_TOTE);
		
		if(operatorStick.isButtonDown(DriverStationConfig.JoystickButtons.ELEVATOR_OVERRIDE))
		{
			elevatorData.setControlMode(ElevatorControlMode.VELOCITY);
			double speed = -operatorStick.getAxis(AxisType.kY);
			elevatorData.setDesiredSpeed(speed);
		}
		if(operatorStick.isButtonDown(DriverStationConfig.JoystickButtons.ELEVATE_ONE))
		{
			elevatorData.setControlMode(ElevatorControlMode.SETPOINT);
			elevatorData.setSetpoint(ElevatorSetpoint.TOTE_1);
		}
		else if(operatorStick.isButtonDown(DriverStationConfig.JoystickButtons.ELEVATE_TWO))
		{
			elevatorData.setControlMode(ElevatorControlMode.SETPOINT);
			elevatorData.setSetpoint(ElevatorSetpoint.TOTE_2);
		}
		else if(operatorStick.isButtonDown(DriverStationConfig.JoystickButtons.ELEVATE_THREE))
		{
			elevatorData.setControlMode(ElevatorControlMode.SETPOINT);
			elevatorData.setSetpoint(ElevatorSetpoint.TOTE_3);
		}
		else if(operatorStick.isButtonDown(DriverStationConfig.JoystickButtons.ELEVATE_FOUR))
		{
			elevatorData.setControlMode(ElevatorControlMode.SETPOINT);
			elevatorData.setSetpoint(ElevatorSetpoint.TOTE_4);
		}
		else if(operatorStick.isButtonDown(DriverStationConfig.JoystickButtons.ELEVATE_STEP))
		{
			elevatorData.setControlMode(ElevatorControlMode.SETPOINT);
			elevatorData.setSetpoint(ElevatorSetpoint.STEP);
		}
	}

}
