package team846.robot;

import com.lynbrookrobotics.frc2015.actuators.Actuator;
import com.lynbrookrobotics.frc2015.actuators.Pneumatics;
import com.lynbrookrobotics.frc2015.automation.Brain;
import com.lynbrookrobotics.frc2015.componentData.ComponentData;
import com.lynbrookrobotics.frc2015.components.Component;
import com.lynbrookrobotics.frc2015.config.ConfigPortMappings;
import com.lynbrookrobotics.frc2015.config.ConfigRuntime;
import com.lynbrookrobotics.frc2015.config.RobotConfig;
import com.lynbrookrobotics.frc2015.dashboard.DashboardLogger;
import com.lynbrookrobotics.frc2015.driverstation.GameState;
import com.lynbrookrobotics.frc2015.driverstation.LRTDriverStation;
import com.lynbrookrobotics.frc2015.log.AsyncPrinter;
import com.lynbrookrobotics.frc2015.sensors.SensorFactory;

//import dashboard.DashboardLogger;
import com.lynbrookrobotics.frc2015.utils.Profiler;

public class LRT15Robot extends LRTRobotBase
{

	public void RobotInit() 
	{
		AsyncPrinter.Initialize();
		AsyncPrinter.info("Initialized Asynchronous Logging...");
		
		RobotState.Initialize();
		AsyncPrinter.info("Initialized RobotState...+");

		LRTDriverStation.Initialize();
		AsyncPrinter.info("Initialized DriverStation Manager...");
		
		ConfigPortMappings.Instance().Load();
		AsyncPrinter.info("Loaded Port Mappings...");
		
		ConfigRuntime.Initialize();
		AsyncPrinter.info("Loaded Config Runtime...");
		
		ComponentData.createComponentDatas();
		AsyncPrinter.info("Created ComponentDatas...");
		
		Component.CreateComponents();
		AsyncPrinter.info("Created Components...");
		
		AsyncPrinter.info("Executing main loop at " + RobotConfig.LOOP_RATE + " hz");
//
		Brain.Initialize();
		AsyncPrinter.info("Initialized Brain...");
//
//		Pneumatics.CreateCompressor();
//
		SensorFactory.Initialize();
		AsyncPrinter.info("Initialized Sensor Factory...");
	}

	public void Tick() {
		Profiler.time(Void->RobotState.Instance().Update(), "RobotState.Update");

		Profiler.time(Void->LRTDriverStation.Update(), "LRTDriverStation.Update");

		Profiler.time(Void->Brain.Instance().Update(), "Brain.Update");

		Profiler.time(Void->Component.UpdateAll(), "Component.UpdateAll");
//
//		Profiler.time(Void->Actuator.OutputAll(), "Actuator.OutputAll");
//
//		Profiler.time(Void->DashboardLogger.getInstance().tick(), "DashboardLogger.tick");
//
		if(RobotState.Instance().GameMode() == GameState.DISABLED)
		{
			Profiler.time(Void->ConfigRuntime.Instance().CheckForFileUpdates(), "ConfigRuntime.CheckForFileUpdates");
		}
		
		ComponentData.ResetAllCommands();

	//	Profiler.time(Void->DashboardLogger.getInstance().tick(), "DashboardLogger.tick");

	}
}
