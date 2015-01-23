package log;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AsyncPrinter
{
	private static long kSleepPeriod = 100;
	private static AsyncPrinter instance = null;

	public static AsyncPrinter getInstance()
	{
		if (instance == null)
			instance = new AsyncPrinter();

		return instance;
	}
	
	private Queue<String> toLog;
	private Thread periodicLogger;

	private AsyncPrinter() {
		toLog = new ConcurrentLinkedQueue<String>();
		periodicLogger = new PeriodicLogger();
		periodicLogger.start();
	}
	
	public void println(String msg)
	{
		toLog.add(msg);
	}

	private class PeriodicLogger extends Thread
	{
		@Override
		public void run()
		{
			while (true)
			{
				long startTime = System.nanoTime();
				for (String msg : toLog) {
					System.out.println(msg);
				}
				
				System.out.println("Log flush took: " + (System.nanoTime() - startTime));
				
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
