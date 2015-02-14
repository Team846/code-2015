package com.team846.frc2015.automation.inputProcessors;

import com.team846.frc2015.automation.ControlResource;
import com.team846.frc2015.componentData.CarriageExtenderData;
import com.team846.frc2015.componentData.CarriageExtenderData.ControlMode;
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
		if(operatorStick.IsButtonDown(DriverStationConfig.JoystickButtons.DEPLOY_STACK))
		{
			extenderData.setControlMode(ControlMode.POSITION);
			double pos = MathUtils.clamp(-operatorStick.getAxis(AxisType.kY),0.0,1.0);
			extenderData.setPositionSetpoint(pos);
		}

	}

}
