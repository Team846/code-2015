package team846.robot;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import com.lynbrookrobotics.frc2015.config.RobotConfig;

import edu.wpi.first.wpilibj.RobotBase;

public abstract class LRTRobotBase extends RobotBase
{
	protected Timer loopSynchronizer;
	protected TimerTask loopGrabber = new TimerTask() {
		
		@Override
		public void run() {
			
			loopSem.release();
		}
	};
	public Semaphore loopSem;
	
	public LRTRobotBase()
	{
		loopSem = new Semaphore(1); //binary semaphore
		loopSynchronizer = new Timer();
		loopSynchronizer.scheduleAtFixedRate(loopGrabber, 0, RobotConfig.LOOP_PERIOD);
	}
	
	public abstract void Tick();
	
	@Override
	public void startCompetition()
	{
		RobotInit(); //Game specific Robot Initialization
		
		Main();
	}

	public abstract void RobotInit();
	
	private void Main()
	{
		while(true)
		{
			loopSem.acquireUninterruptibly(); //Blocks until semaphore is available
			long prevTime = System.currentTimeMillis();
			Tick();
			long delta = System.currentTimeMillis() - prevTime;
			if(delta > RobotConfig.LOOP_PERIOD)
				System.out.println("[WARNING] Loop time exceeded " + RobotConfig.LOOP_PERIOD + "ms");
		}
	}

}
