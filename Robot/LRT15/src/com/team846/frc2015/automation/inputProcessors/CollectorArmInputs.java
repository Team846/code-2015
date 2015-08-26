package com.team846.frc2015.automation.inputProcessors;

import com.team846.frc2015.automation.ControlResource;
import com.team846.frc2015.componentData.CollectorArmData;
import com.team846.frc2015.componentData.CollectorArmData.ArmPosition;
import com.team846.frc2015.config.DriverStationConfig;
import com.team846.frc2015.driverstation.LRTDriverStation;
import com.team846.frc2015.driverstation.LRTJoystick;

public class CollectorArmInputs extends InputProcessor {

	private final CollectorArmData armData;
	private final LRTJoystick driverStick;
	public CollectorArmInputs()
	{
		RegisterResource(ControlResource.COLLECTOR_ARMS);
		armData = CollectorArmData.get();
		driverStick = LRTDriverStation.instance().getDriverStick();

	}
	@Override
	public void Update() {
		if(driverStick.isButtonDown(DriverStationConfig.JoystickButtons.COLLECT))
		{
			armData.setDesiredPosition(ArmPosition.EXTEND);
			//AsyncPrinter.info("Extend Collector");
		}

	}

}
