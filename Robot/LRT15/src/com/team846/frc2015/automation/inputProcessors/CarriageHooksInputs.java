package com.team846.frc2015.automation.inputProcessors;

import com.team846.frc2015.automation.ControlResource;
import com.team846.frc2015.componentData.CarriageHooksData;
import com.team846.frc2015.componentData.CarriageHooksData.HookState;
import com.team846.frc2015.config.DriverStationConfig;
import com.team846.frc2015.driverstation.LRTDriverStation;
import com.team846.frc2015.driverstation.LRTJoystick;

public class CarriageHooksInputs extends InputProcessor{
	
	
	private CarriageHooksData hooksData;
	private LRTJoystick operatorStick;

	public CarriageHooksInputs() {

		hooksData = CarriageHooksData.get();
		operatorStick = LRTDriverStation.Instance().GetOperatorStick();
		RegisterResource(ControlResource.CARRIAGE_HOOKS);
	}

	@Override
	public void Update() {
		if(operatorStick.IsButtonDown(DriverStationConfig.JoystickButtons.HOOKS_OVERRIDE))
		{
			hooksData.setFrontHooksDesiredState(HookState.UP);
			hooksData.setBackHooksDesiredState(HookState.UP);
		}
		
	}

}
