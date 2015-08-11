package com.team846.robot;

import com.team846.frc2015.actuators.Actuator;
import com.team846.frc2015.actuators.Pneumatics;
import com.team846.frc2015.automation.Brain;
import com.team846.frc2015.componentData.ComponentData;
import com.team846.frc2015.components.Component;
import com.team846.frc2015.config.ConfigPortMappings;
import com.team846.frc2015.config.ConfigRuntime;
import com.team846.frc2015.config.RobotConfig;
import com.team846.frc2015.driverstation.GameState;
import com.team846.frc2015.driverstation.LRTDriverStation;
import com.team846.frc2015.sensors.SensorFactory;
import com.team846.frc2015.utils.AsyncPrinter;


public class LRT15Robot extends LRTRobotBase
{
	private int robotStateCounter = 1;

	public static gyro;

	public void RobotInit()
	{
		AsyncPrinter.initialize();
		AsyncPrinter.info("Initialized Asynchronous Logging...");

		RobotState.Initialize();
		AsyncPrinter.info("Initialized RobotState...");

		LRTDriverStation.initialize();
		AsyncPrinter.info("Initialized DriverStation Manager...");

		ConfigPortMappings.Instance().Load();
		AsyncPrinter.info("Loaded Port Mappings...");

		ConfigRuntime.Initialize();
		AsyncPrinter.info("Loaded Config Runtime...");

		ComponentData.createComponentDatas();
		AsyncPrinter.info("Created ComponentDatas...");

		Component.CreateComponents();
		AsyncPrinter.info("Created Components...");

		Brain.Initialize();
		AsyncPrinter.info("Initialized Brain...");

		SensorFactory.initialize();
		AsyncPrinter.info("Initialized Sensor Factory...");

		Pneumatics.createCompressor();
		AsyncPrinter.info("Creating Compressor...");

		AsyncPrinter.info("Executing main loop at " + RobotConfig.LOOP_RATE + " hz");

		gyro = LRTGyro.getInstance();
	}

	public void Tick() {

		if (robotStateCounter % 2 == 0) {
			RobotState.Instance().Update();
		}
		robotStateCounter++;

		LRTDriverStation.update();

		Brain.Instance().Update();

		Component.UpdateAll();

		Actuator.outputAll();

		gyro.update();

		if(RobotState.Instance().GameMode() == GameState.DISABLED)
			ConfigRuntime.Instance().CheckForFileUpdates();

		ComponentData.ResetAllCommands();
	}
}