package com.team846.frc2015.utils;

import com.team846.frc2015.logging.AsyncLogger;

import java.util.Map.Entry;
import java.util.HashMap;

class Profiler 
{
	private Profiler() {}

	private static final HashMap<String, Pair<Long, Long>> profilingData =
			new HashMap<String, Pair<Long, Long>>();

	private static void log(String msg)
	{
		AsyncLogger.info(msg);
	}
	
	public static void start(String methodName)
	{
		long startTime = System.nanoTime();

		profilingData.get(methodName).setFirst(startTime);
	}

	public static void end(String methodName)
	{
		long endTime = System.nanoTime();

		profilingData.get(methodName).setSecond(endTime);
	}

	public static void show(String methodName)
	{
		log("Profiler output:");

		long endTime = profilingData.get(methodName).getSecond();
		long startTime = profilingData.get(methodName).getFirst();
		
		long totalTime = (endTime - startTime / 1000000);
		
		log("\tMethod name: " + methodName);
		log("\tTime taken: " + totalTime + "ms");
		
		clear();
	}

	public static void show()
	{
		log("Profiler output:");
		for (Entry<String, Pair<Long, Long>> e : profilingData.entrySet())
		{
			long endTime = e.getValue().getSecond();
			long startTime = e.getValue().getFirst();
			
			long totalTime = (endTime - startTime / 1000000);
			
			log("\tMethod name: " + e.getKey());
			log("\tTime taken: " + totalTime + " ms");
		}
		clear();
	}

	private static void clear()
	{
		profilingData.clear();
	}
}
