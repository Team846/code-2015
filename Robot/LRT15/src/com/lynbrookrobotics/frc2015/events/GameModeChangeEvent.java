package com.lynbrookrobotics.frc2015.events;

import com.lynbrookrobotics.frc2015.driverstation.GameState;
import com.lynbrookrobotics.frc2015.driverstation.LRTDriverStation;

import team846.robot.RobotState;

public class GameModeChangeEvent extends Event
{
	GameState from;
	GameState to;
	
	@Override
	public boolean CheckCondition()
	{
		if (from != null) {
			return RobotState.Instance().LastGameMode() == from && RobotState.Instance().GameMode() == to;
		} else {
			return RobotState.Instance().GameMode() == to;
		}
	}
}
