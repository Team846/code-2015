package com.lynbrookrobotics.frc2015.inputProcessors;

import com.lynbrookrobotics.frc2015.automation.ControlResource;
import com.lynbrookrobotics.frc2015.componentData.ElevatorData.ControlMode;
import com.lynbrookrobotics.frc2015.componentData.ElevatorData;
import com.lynbrookrobotics.frc2015.config.DriverStationConfig;
import com.lynbrookrobotics.frc2015.driverstation.LRTDriverStation;
import com.lynbrookrobotics.frc2015.driverstation.LRTJoystick;
import com.ni.vision.NIVision.VerticalTextAlignment;

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
