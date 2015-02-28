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
	private ElevatorSetpoint[] elevatorSetpoints;

	public ElevatorInputs()
	{
		elevatorData = ElevatorData.get();
		operatorStick = LRTDriverStation.Instance().GetOperatorStick();
		
		levelSelector = 0;
		elevatorSetpoints = ElevatorSetpoint.values(); //expensive call, storing array once is better for perf
		RegisterResource(ControlResource.ELEVATOR);
	}
	
	@Override
	public void Update() {
		if(operatorStick.IsButtonDown(DriverStationConfig.JoystickButtons.ELEVATOR_OVERRIDE))
		{
			elevatorData.setControlMode(ElevatorControlMode.SPEED);
			double speed = -operatorStick.getAxis(AxisType.kY);
			speed = speed < 0.05 ? 0.0 : speed;
			elevatorData.setDesiredSpeed(speed);
		}
		
		if(operatorStick.IsButtonJustPressed(DriverStationConfig.JoystickButtons.ELEVATOR_INCREMENT))
		{
			levelSelector++;
			if(levelSelector >= 5)
				levelSelector = 0;
		}
		if(operatorStick.IsButtonJustPressed(DriverStationConfig.JoystickButtons.ELEVATOR_FINALIZE))
		{
			levelSelector = 0;
			elevatorData.setControlMode(ElevatorControlMode.SETPOINT);
			elevatorData.setSetpoint(elevatorSetpoints[levelSelector]);
		}
	}

}
