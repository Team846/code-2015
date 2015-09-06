package com.team846.robot;

import com.team846.frc2015.actuators.Actuator;
import com.team846.frc2015.actuators.Pneumatics;
import com.team846.frc2015.automation.Brain;
import com.team846.frc2015.componentData.ComponentData;
import com.team846.frc2015.components.Component;
import com.team846.frc2015.logging.Logger;
import com.team846.frc2015.oldconfig.ConfigPortMappings;
import com.team846.frc2015.oldconfig.ConfigRuntime;
import com.team846.frc2015.oldconfig.RobotConfig;
import com.team846.frc2015.driverstation.GameState;
import com.team846.frc2015.driverstation.LRTDriverStation;
import com.team846.frc2015.sensors.SensorFactory;
import com.team846.frc2015.logging.AsyncLogger;
import com.team846.frc2015.dashboard.DashboardLogger;
import com.team846.frc2015.sensors.LRTGyro;


public class LRT15Robot extends LRTRobotBase
{
	private int robotStateCounter = 1;

	public static LRTGyro gyro;

	public void RobotInit()
	{
		Logger.initialize();
//		AsyncLogger.initialize();
		AsyncLogger.info("Initialized Asynchronous Logging...");

		RobotState.Initialize();
		AsyncLogger.info("Initialized RobotState...");

		LRTDriverStation.initialize();
		AsyncLogger.info("Initialized DriverStation Manager...");

		ConfigPortMappings.Instance().Load();
		AsyncLogger.info("Loaded Port Mappings...");

		ConfigRuntime.Initialize();
		AsyncLogger.info("Loaded Config Runtime...");

		ComponentData.createComponentDatas();
		AsyncLogger.info("Created ComponentDatas...");

		Component.CreateComponents();
		AsyncLogger.info("Created Components...");

		Brain.Initialize();
		AsyncLogger.info("Initialized Brain...");

		SensorFactory.initialize();
		AsyncLogger.info("Initialized Sensor Factory...");

		Pneumatics.createCompressor();
		AsyncLogger.info("Creating Compressor...");

		DashboardLogger.Initialize();
		AsyncLogger.info("Creating Funky Dashboard...");

		AsyncLogger.info("Executing main loop at " + RobotConfig.LOOP_RATE + " hz");

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
