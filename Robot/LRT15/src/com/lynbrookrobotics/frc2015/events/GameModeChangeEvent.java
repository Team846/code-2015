package com.lynbrookrobotics.frc2015.events;

import com.lynbrookrobotics.frc2015.driverstation.GameState;
import com.lynbrookrobotics.frc2015.driverstation.LRTDriverStation;

import team846.robot.RobotState;

public class GameModeChangeEvent extends Event
{
	GameState fromMode;
	GameState toMode;
	boolean from;
	
	public GameModeChangeEvent(GameState toMode)
	{
		this.toMode  = toMode;
		from = false;
	}
	
	public GameModeChangeEvent(GameState toMode, GameState fromMode)
	{
		this.toMode = toMode;
		this.fromMode = fromMode;
		from = false;
	}
	
	@Override
	public boolean CheckCondition()
	{
		if (fromMode != null) {
			return RobotState.Instance().LastGameMode() == fromMode && RobotState.Instance().GameMode() == toMode;
		} else {
			return RobotState.Instance().GameMode() == toMode;
		}
	}
}
