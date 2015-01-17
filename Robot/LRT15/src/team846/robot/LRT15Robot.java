package team846.robot;

import sensors.SensorFactory;
import actuators.Actuator;
import actuators.Pneumatics;
import automation.Brain;
import components.Component;
import components.ComponentData;
import driverstation.GameState;
import driverstation.LRTDriverStation;

public class LRT15Robot extends LRTRobotBase 
{
	
	public void RobotInit() {
		RobotState.Initialize();
		
		LRTDriverStation.Initialize();
		//ConfigPortMappings.Instance().Load();
		
		ComponentData.Initialize();
		
		Component.CreateComponents();
		
		Brain.Initialize();
		
		Pneumatics.CreateCompressor();
		
		SensorFactory.Initialize();
		
	}

	public void Tick() {
		RobotState.Instance().Update();
		
		LRTDriverStation.Update();
		
		Brain.Instance().Update();
		
		Component.UpdateAll();
		
		Actuator.OutputAll();
		
		if(RobotState.Instance().GameMode() == GameState.DISABLED)
		{
			//ConfigRuntime.Instance().CheckForFileUpdates();
		}
		
	}
}
