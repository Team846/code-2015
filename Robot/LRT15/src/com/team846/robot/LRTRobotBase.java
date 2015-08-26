package com.team846.robot;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import com.team846.frc2015.config.RobotConfig;

import edu.wpi.first.wpilibj.RobotBase;

abstract class LRTRobotBase extends RobotBase
{
	private final Timer loopSynchronizer;
	private final TimerTask loopGrabber = new TimerTask() {
		@Override
		public void run() {
			
			loopSem.release();
		}
	};
	private final Semaphore loopSem;
	
	LRTRobotBase()
	{
		loopSem = new Semaphore(1); //binary semaphore
		loopSynchronizer = new Timer();
		loopSynchronizer.scheduleAtFixedRate(loopGrabber, 0, RobotConfig.LOOP_PERIOD);
	}
	
	protected abstract void Tick();
	
	@Override
	public void startCompetition()
	{
		RobotInit(); //Game specific Robot Initialization
		
		Main();
	}

	protected abstract void RobotInit();
	
	private void Main()
	{
		while(true)
		{
			loopSem.acquireUninterruptibly(); //Blocks until semaphore is available
			long prevTime = System.currentTimeMillis();
			Tick();
//			long delta = System.currentTimeMillis() - prevTime;
//			if(delta > RobotConfig.LOOP_PERIOD)
//			{
//				System.out.println("[WARNING] Loop time of " + RobotConfig.LOOP_PERIOD + "ms exceeded by "
//			+ (delta - RobotConfig.LOOP_PERIOD) + "ms");
//				Profiler.show();
//				Profiler.clear();
//			}
		}
	}

}
