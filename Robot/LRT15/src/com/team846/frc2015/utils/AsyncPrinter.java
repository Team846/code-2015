package com.team846.frc2015.utils;

import com.team846.frc2015.logging.Logger;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * change	 queue to take objects that represent a value to be log
 * different types for different types of log messages (info, warning, error)
 * adjust existing logging methods (println, etc) to add a log object to the queue
 *
 * create interface loggable
 * change periodic logger to use the logger class
 */

public class AsyncPrinter
{
	private static abstract class Loggable {
		String message;
		abstract public void log();

		public Loggable(String message) {
			this.message = message;
		}
	}

	static class InfoLog extends Loggable {
		public InfoLog(String message) {
			super(message);
		}

		@Override
		public void log() {
			Logger.getInstance().info(message);
		}
	}

	static class DebugLog extends Loggable {
		public DebugLog(String message) {
			super(message);
		}

		@Override
		public void log() {
			Logger.getInstance().debug(message);
		}
	}

	static class ErrorLog extends Loggable {
		public ErrorLog(String message) {
			super(message);
		}

		@Override
		public void log() {
			Logger.getInstance().error(message);
		}
	}

	static class WarningLog extends Loggable {
		public WarningLog(String message) {
			super(message);
		}

		@Override
		public void log() {
			Logger.getInstance().warning(message);
		}
	}
	private static final long kSleepPeriod = 20; //ms
	private static AsyncPrinter instance = null;
	
	private static final Queue<Loggable> toLog = new ConcurrentLinkedQueue<Loggable>();
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
	
	public static void debug(String msg)
	{
		toLog.add(new DebugLog(msg));
	}
	
	public static void warn(String msg)
	{
		toLog.add(new WarningLog(msg));
	}
	
	public static void error(String msg)
	{
		toLog.add(new ErrorLog(msg));
	}
	
	public static void info(String msg)
	{
		toLog.add(new InfoLog(msg));
	}


	private class PeriodicLogger extends Thread
	{
		@Override
		public void run()
		{
			while (true)
			{
				//long startTime = System.nanoTime();
				for (Loggable msg : toLog) {
					msg.log();
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
