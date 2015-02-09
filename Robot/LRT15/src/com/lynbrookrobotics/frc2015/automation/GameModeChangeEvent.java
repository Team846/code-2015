package com.lynbrookrobotics.frc2015.automation;

import team846.robot.RobotState;

import com.lynbrookrobotics.frc2015.driverstation.GameState;
import com.lynbrookrobotics.frc2015.events.Event;

public class GameModeChangeEvent extends Event {

	GameState toMode;
	GameState fromMode;
	boolean from;
	
	 public GameModeChangeEvent(GameState toMode) {
		this.toMode = toMode;
		from = false;
	}
	 
	 public GameModeChangeEvent(GameState toMode, GameState fromMode) {
			this.toMode = toMode;
			this.fromMode = fromMode;
			from = true;
		}
	
	
	@Override
	public boolean CheckCondition() {
		// TODO Auto-generated method stub
		return RobotState.Instance().GameMode() == toMode;
	}
	
	public boolean Fired()
	{
		if(super.Fired())
		{
			if(from && RobotState.Instance().LastGameMode() != fromMode)
				return false;
			
			return true;
		}
		return false;
	}

}
