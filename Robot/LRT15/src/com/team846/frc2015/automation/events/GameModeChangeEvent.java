package com.team846.frc2015.automation.events;

import com.team846.frc2015.driverstation.GameState;
import com.team846.robot.RobotState;

public class GameModeChangeEvent extends Event {
    private GameState fromMode = null;
    private GameState toMode = null;
    private final boolean FROM;

    public GameModeChangeEvent(GameState toMode) {
        this.toMode = toMode;
        FROM = false;
    }

    public GameModeChangeEvent(GameState toMode, GameState fromMode) {
        this.toMode = toMode;
        this.fromMode = fromMode;
        FROM = false;
    }

    @Override
    public boolean checkCondition() {
        if (fromMode != null) {
            return RobotState.Instance().LastGameMode() == fromMode && RobotState.Instance().GameMode() == toMode;
        } else {
            return RobotState.Instance().GameMode() == toMode;
        }
    }
}
