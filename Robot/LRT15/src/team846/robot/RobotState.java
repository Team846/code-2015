package team846.robot;

import driverstation.GameState;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

public class RobotState {
	
	private static RobotState instance = null;
	
	private GameState gameMode;
	private GameState lastGameMode;
	private Timer matchTimer;
	private Timer totalTimer;
	private double lastTime;
	private double currentTime;
	private DriverStation ds;
	private boolean fms;
	
	public static void Initialize()
	{
		if (instance == null)
			instance = new RobotState();
	}

	public static RobotState Instance()
	{
		if (instance == null)
			instance = new RobotState();
		return instance;
	}
	
	private RobotState()
	{
		totalTimer.start();
		gameMode = GameState.DISABLED;
		lastTime = 0.0;
		ds = DriverStation.getInstance();
		fms = ds.isFMSAttached();
	}


	public GameState GameMode()
	{
		return gameMode;
	}

	public GameState LastGameMode()
	{
		return lastGameMode;
	}

	public double MatchTime()
	{
		return matchTimer.get();
	}

	public double TotalTime()
	{
		return totalTimer.get();
	}

	public boolean FMSAttached()
	{
		return fms;
	}

	public double LastCycleTime()
	{
		return currentTime - lastTime;
	}

	public void Update()
	{
		lastGameMode = gameMode;
		if (ds.isDisabled())
		{
			gameMode = GameState.DISABLED;
			matchTimer.stop();
			matchTimer.reset();
		}
		else if (ds.isAutonomous())
		{
			gameMode = GameState.AUTONOMOUS;
			matchTimer.start();
		}
		else if (ds.isOperatorControl())
		{
			gameMode = GameState.TELEOPERATED;
			matchTimer.start();
		}
		fms = ds.isFMSAttached();
		lastTime = currentTime;
		currentTime = Timer.getFPGATimestamp();
	}
	

}
