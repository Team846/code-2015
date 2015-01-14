package team846.robot;

import sensors.SensorFactory;
import actuators.Actuator;
import actuators.Pneumatics;
import automation.Brain;
import components.Component;
import components.ComponentData;
import driverstation.GameState;
import driverstation.LRTDriverStation;
import edu.wpi.first.wpilibj.RobotBase;

public class LRT15Robot extends RobotBase 
{
	

	@Override
	public void startCompetition() {
		RobotInit();
		Main();
		
	}

	private void Main() {
		while(true)
		{
			Tick();
		}
		
	}
	
	private void RobotInit() {
		RobotState.Initialize();
		
		LRTDriverStation.Initialize();
		//ConfigPortMappings.Instance().Load();
		
		ComponentData.Initialize();
		
		Component.CreateComponents();
		
		Brain.Initialize();
		
		Pneumatics.CreateCompressor();
		
		SensorFactory.Initialize();
		
	}

	private void Tick() {
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
