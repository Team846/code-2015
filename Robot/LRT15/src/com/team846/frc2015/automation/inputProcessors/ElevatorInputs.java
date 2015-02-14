package com.team846.frc2015.automation.inputProcessors;

import com.ni.vision.NIVision.VerticalTextAlignment;
import com.team846.frc2015.automation.ControlResource;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.componentData.ElevatorData.ControlMode;
import com.team846.frc2015.config.DriverStationConfig;
import com.team846.frc2015.driverstation.LRTDriverStation;
import com.team846.frc2015.driverstation.LRTJoystick;

import edu.wpi.first.wpilibj.Joystick.AxisType;

public class ElevatorInputs extends InputProcessor {

	private ElevatorData elevatorData;
	private LRTJoystick operatorStick;

	public ElevatorInputs()
	{
		elevatorData = ElevatorData.get();
		operatorStick = LRTDriverStation.Instance().GetOperatorStick();
		
		RegisterResource(ControlResource.ELEVATOR);
		
	}
	
	@Override
	public void Update() {
		if(operatorStick.IsButtonDown(DriverStationConfig.JoystickButtons.ELEVATOR_OVERRIDE))
		{
			elevatorData.setControlMode(ControlMode.SPEED);
			elevatorData.setSpeed(-operatorStick.getAxis(AxisType.kY));
		}
		
	}

}
