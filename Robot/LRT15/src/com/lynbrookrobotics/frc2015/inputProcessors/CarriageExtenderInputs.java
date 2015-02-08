package com.lynbrookrobotics.frc2015.inputProcessors;

import com.lynbrookrobotics.frc2015.componentData.CarriageExtenderData;
import com.lynbrookrobotics.frc2015.componentData.CarriageExtenderData.ControlMode;
import com.lynbrookrobotics.frc2015.config.DriverStationConfig;
import com.lynbrookrobotics.frc2015.driverstation.LRTDriverStation;
import com.lynbrookrobotics.frc2015.driverstation.LRTJoystick;

import edu.wpi.first.wpilibj.Joystick.AxisType;

public class CarriageExtenderInputs extends InputProcessor {
	
	private CarriageExtenderData extenderData;
	private LRTJoystick operatorStick;
	

	public CarriageExtenderInputs() {
		extenderData = CarriageExtenderData.get();
		operatorStick = LRTDriverStation.Instance().GetOperatorStick();
	}

	@Override
	public void Update() {
		if(operatorStick.IsButtonDown(DriverStationConfig.JoystickButtons.DEPLOY_STACK))
		{
			extenderData.setControlMode(ControlMode.POSITION);
			double pos = -operatorStick.getAxis(AxisType.kY);
			extenderData.setDesiredPositionSetpoint(pos);
		}

	}

}
