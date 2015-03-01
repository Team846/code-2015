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

	private ElevatorData elevatorData;
	private LRTJoystick operatorStick;
	
	private int levelSelector;
	private ElevatorSetpoint[] elevatorSetpoints = {ElevatorSetpoint.TOTE_1, ElevatorSetpoint.TOTE_2, ElevatorSetpoint.TOTE_3, ElevatorSetpoint.TOTE_4};

	public ElevatorInputs()
	{
		elevatorData = ElevatorData.get();
		operatorStick = LRTDriverStation.Instance().GetOperatorStick();
		
		levelSelector = 0;
		
		RegisterResource(ControlResource.ELEVATOR);
	}
	
	@Override
	public void Update() {
		if(operatorStick.IsButtonDown(DriverStationConfig.JoystickButtons.ELEVATOR_OVERRIDE))
		{
			elevatorData.setControlMode(ElevatorControlMode.SPEED);
			double speed = -operatorStick.getAxis(AxisType.kY);
			//speed = speed < 0.05 ? 0.0 : speed;
			elevatorData.setDesiredSpeed(speed);
		}
		if(operatorStick.IsButtonDown(DriverStationConfig.JoystickButtons.ELEVATE_ONE))
		{
			elevatorData.setControlMode(ElevatorControlMode.SETPOINT);
			elevatorData.setSetpoint(ElevatorSetpoint.TOTE_1);
		}
		else if(operatorStick.IsButtonDown(DriverStationConfig.JoystickButtons.ELEVATE_TWO))
		{
			elevatorData.setControlMode(ElevatorControlMode.SETPOINT);
			elevatorData.setSetpoint(ElevatorSetpoint.TOTE_2);
		}
		else if(operatorStick.IsButtonDown(DriverStationConfig.JoystickButtons.ELEVATE_THREE))
		{
			elevatorData.setControlMode(ElevatorControlMode.SETPOINT);
			elevatorData.setSetpoint(ElevatorSetpoint.TOTE_3);
		}
		else if(operatorStick.IsButtonDown(DriverStationConfig.JoystickButtons.ELEVATE_FOUR))
		{
			elevatorData.setControlMode(ElevatorControlMode.SETPOINT);
			elevatorData.setSetpoint(ElevatorSetpoint.TOTE_4);
		}
//		if(operatorStick.IsButtonJustPressed(DriverStationConfig.JoystickButtons.ELEVATOR_INCREMENT))
//		{
//			levelSelector++;
//			if(levelSelector >= 5)
//				levelSelector = 0;
//		}
//		if(operatorStick.IsButtonJustPressed(DriverStationConfig.JoystickButtons.ELEVATOR_FINALIZE))
//		{
//			levelSelector = 0;
//			elevatorData.setControlMode(ElevatorControlMode.SETPOINT);
//			elevatorData.setSetpoint(elevatorSetpoints[levelSelector]);
//		}
	}

}
