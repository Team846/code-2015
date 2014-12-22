package driverstation;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.Timer;

public class RobotState {
	static RobotState instance = null;
	
	RobotBase robot;
	GameState gameMode;
	GameState lastGameMode;
	Timer matchTimer;
	Timer totalTimer;
	double lastTime;
	double currentTime;
	boolean fms;
	
	public void Initialize()
	{
		if(instance == null)
			instance = new RobotState();
		 //robot = new RobotBa
		
	}
	
	public static RobotState Instance()
	{
		if(instance == null)
			instance = new RobotState();
		
		return instance;
	}
	
	private RobotState()
	{
		totalTimer.start();
		gameMode = GameState.DISABLED;
		lastTime = 0.0;
		fms = DriverStation.getInstance().isFMSAttached();
	}
	
	GameState GameMode()
	{
		return gameMode;
	}

	GameState LastGameMode()
	{
		return lastGameMode;
	}

	double MatchTime()
	{
		return matchTimer.get();
	}

	double TotalTime()
	{
		return totalTimer.get();
	}

	boolean FMSAttached()
	{
		return fms;
	}

	double LastCycleTime()
	{
		return currentTime - lastTime;
	}

	void Update()
	{
		
		lastGameMode = gameMode;
		if (true)//RobotBase base
		{
			
			gameMode = GameState.DISABLED;
			matchTimer.stop();
			matchTimer.reset();
		}
		else if (true/*RobotBase.getInstance().IsAutonomous()*/)
		{
			gameMode = GameState.AUTONOMOUS;
			matchTimer.start();
		}
		else if (true/*RobotBase.getInstance().IsOperatorControl()*/)
		{
			gameMode = GameState.TELEOPERATED;
			matchTimer.start();
		}
		fms = DriverStation.getInstance().isFMSAttached();
		lastTime = currentTime;
		currentTime = Timer.getFPGATimestamp();
		
	}
}
