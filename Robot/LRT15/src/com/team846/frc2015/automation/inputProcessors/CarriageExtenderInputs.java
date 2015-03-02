package com.team846.frc2015.automation.inputProcessors;

import com.team846.frc2015.automation.ControlResource;
import com.team846.frc2015.componentData.CarriageExtenderData;
import com.team846.frc2015.componentData.CarriageExtenderData.CarriageControlMode;
import com.team846.frc2015.config.DriverStationConfig;
import com.team846.frc2015.driverstation.LRTDriverStation;
import com.team846.frc2015.driverstation.LRTJoystick;
import com.team846.frc2015.utils.MathUtils;

import edu.wpi.first.wpilibj.Joystick.AxisType;

public class CarriageExtenderInputs extends InputProcessor {
	
	private CarriageExtenderData extenderData;
	private LRTJoystick operatorStick;

	public CarriageExtenderInputs() {
		extenderData = CarriageExtenderData.get();
		operatorStick = LRTDriverStation.Instance().GetOperatorStick();
		RegisterResource(ControlResource.CARRIAGE_EXTENDER);
	}

	@Override
	public void Update() {

		extenderData.setControlMode(CarriageControlMode.POSITION);
		extenderData.setPositionSetpoint(0.0);
		
		if(operatorStick.IsButtonDown(DriverStationConfig.JoystickButtons.EXTEND_CARRIAGE))
		{
			extenderData.setControlMode(CarriageControlMode.POSITION);
			double pos = MathUtils.clamp(-operatorStick.getAxis(AxisType.kY),0.0,1.0);
			extenderData.setPositionSetpoint(pos);
		}
		else if(operatorStick.IsButtonDown(DriverStationConfig.JoystickButtons.CARRIAGE_OVERRIDE))
		{
			extenderData.setControlMode(CarriageControlMode.VELOCITY);
			double speed = MathUtils.clamp(-operatorStick.getAxis(AxisType.kY),-1.0,1.0);
			extenderData.setSpeed(speed);
		}
	}

}

