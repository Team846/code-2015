package team846.robot;

import sensors.SensorFactory;

import actuators.Actuator;
import actuators.Pneumatics;
import automation.Brain;
import componentData.ComponentData;
import components.Component;
import config.ConfigPortMappings;
import config.ConfigRuntime;
import dashboard.DashboardLogger;
//import dashboard.DashboardLogger;
import driverstation.GameState;
import driverstation.LRTDriverStation;
import utils.Profiler;

public class LRT15Robot extends LRTRobotBase
{

	public void RobotInit() 
	{
		RobotState.Initialize();

		LRTDriverStation.Initialize();
		
		ConfigPortMappings.Instance().Load();
		ConfigRuntime.Initialize();

		ComponentData.createComponentDatas();
		Component.CreateComponents();
//
//		Brain.Initialize();
//
//		Pneumatics.CreateCompressor();
//
//		SensorFactory.Initialize();
	}

	public void Tick() {
		Profiler.time(Void->RobotState.Instance().Update(), "RobotState.Update");

		Profiler.time(Void->LRTDriverStation.Update(), "LRTDriverStation.Update");
//
//		Profiler.time(Void->Brain.Instance().Update(), "Brain.Update");
//
//		Profiler.time(Void->Component.UpdateAll(), "Component.UpdateAll");
//
//		Profiler.time(Void->Actuator.OutputAll(), "Actuator.OutputAll");
//
//		Profiler.time(Void->DashboardLogger.getInstance().tick(), "DashboardLogger.tick");
//
		if(RobotState.Instance().GameMode() == GameState.DISABLED)
		{
			Profiler.time(Void->ConfigRuntime.Instance().CheckForFileUpdates(), "ConfigRuntime.CheckForFileUpdates");
		}

	//	Profiler.time(Void->DashboardLogger.getInstance().tick(), "DashboardLogger.tick");

	}
}
