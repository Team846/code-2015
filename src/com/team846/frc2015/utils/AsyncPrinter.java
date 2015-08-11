package com.team846.frc2015.utils;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AsyncPrinter
{
	private static final long kSleepPeriod = 20; //ms
	private static AsyncPrinter instance = null;
	
	private static final Queue<String> toLog = new ConcurrentLinkedQueue<String>();
	private final Thread periodicLogger;
	
	public static void initialize()
	{
		if (instance == null)
			instance = new AsyncPrinter();
	}

	private AsyncPrinter() {
		periodicLogger = new PeriodicLogger();
		periodicLogger.start();
	}
	
	public static void println(String msg)
	{
		toLog.add("[LOG] " + msg);
	}
	
	public static void warn(String msg)
	{
		toLog.add("[WARNING] " + msg);
	}
	
	public static void error(String msg)
	{
		toLog.add("[ERROR] " + msg);
	}
	
	public static void info(String msg)
	{
		toLog.add("[INFO] " + msg);	
	}

	private class PeriodicLogger extends Thread
	{
		@Override
		public void run()
		{
			while (true)
			{
				//long startTime = System.nanoTime();
				for (String msg : toLog) {
					System.out.println(msg);
				}
				toLog.clear();
				try
				{
					Thread.sleep(kSleepPeriod);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
