package com.lynbrookrobotics.frc2015.utils;

import java.util.Map;
import java.util.function.Consumer;
import java.util.concurrent.ConcurrentHashMap;

import com.lynbrookrobotics.frc2015.log.AsyncPrinter;

/*
 * USAGE
 * 
 * import Profiler;
 * 
 * public class someClass {
 * 		public static void someMethod(int i, int j) {
 * 			// sometask
 * 			
 * 			System.out.println(i + j); // 3
 * 		}
 * 
 *		public static void someVoidMethod() {
 * 			// sometask
 * 		}
 * 		
 * 		public static void main(String[] args) {
 * 			Profiler.time(Void -> someClass.someMethod(1, 2), "id1"); Lambda syntax
 *			Profiler.time(Void -> someClass.someVoidMethod(), "id2");
 *			
 *			Profiler.show("id1"); Show individual profiled method
 *			Profiler.show(); Show all profiled methods
 *			Profiler.clear(); Clear entries in profiler
 * 		}
 * }
 */

public class Profiler {
	private Profiler()
	{}

	static private final String tab = "\t";

	private static ConcurrentHashMap<String, Long> profilingData = new ConcurrentHashMap<String, Long>();
	private static ConcurrentHashMap<String, Long> startTimes = new ConcurrentHashMap<String, Long>();
	
	private static void log(String msg) {
		AsyncPrinter.println(msg.toString());
	}

	public static void time(Consumer<Object> aFunction, String methodName) {
		long startTime = System.nanoTime();
		aFunction.accept(null);
		long endTime = System.nanoTime();

		profilingData.put(methodName, (endTime - startTime) / 1000000);
	}
	
	public static void start(String methodName) {
		long startTime = System.nanoTime();
		
		startTimes.put(methodName, startTime);
	}
	
	public static void end(String methodName) {
		long endTime = System.nanoTime();
		
		profilingData.put(methodName, (endTime - startTimes.get(methodName)) / 1000000);
		startTimes.put(methodName, null);
	}

	public static void show(String methodName) {
		log("Profiler output:");

		log(tab + "Method name: " + methodName);
		log(tab + "Time taken: " + profilingData.get(methodName) + "ms");
	}
	
	public static void show()
	{
		log("Profiler output:");
		for(Map.Entry<String, Long> e: profilingData.entrySet())
		{
			log(tab + "Method name: " + e.getKey());
			log(tab + "Time taken: " + e.getValue() + " ms");
		}
	}
	
	public static void clear()
	{
		profilingData.clear();
	}
}
