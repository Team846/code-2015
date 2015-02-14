package com.team846.frc2015.automation.events;

import com.team846.frc2015.driverstation.GameState;
import com.team846.frc2015.driverstation.LRTDriverStation;
import com.team846.robot.RobotState;

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
