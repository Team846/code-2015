package com.lynbrookrobotics.frc2015.utils;

import java.util.Map;
import java.util.Map.Entry;
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
	private Profiler() {
	}

	static private final String tab = "\t";

	private static ConcurrentHashMap<String, Pair<Long, Long>> profilingData = 
			new ConcurrentHashMap<String, Pair<Long, Long>>();

	private static void log(String msg) {
		AsyncPrinter.println(msg.toString());
	}
	
	public static void start(String methodName) {
		long startTime = System.nanoTime();

		profilingData.get(methodName).setFirst(startTime);
	}

	public static void end(String methodName) {
		long endTime = System.nanoTime();

		profilingData.get(methodName).setSecond(endTime);
	}

	public static void show(String methodName) {
		log("Profiler output:");

		long endTime = profilingData.get(methodName).getSecond();
		long startTime = profilingData.get(methodName).getFirst();
		
		long totalTime = (endTime - startTime / 1000000);
		
		log(tab + "Method name: " + methodName);
		log(tab + "Time taken: " + totalTime + "ms");
	}

	public static void show() {
		log("Profiler output:");
		for (Entry<String, Pair<Long, Long>> e : profilingData.entrySet()) {
			long endTime = e.getValue().getSecond();
			long startTime = e.getValue().getFirst();
			
			long totalTime = (endTime - startTime / 1000000);
			
			log(tab + "Method name: " + e.getKey());
			log(tab + "Time taken: " + totalTime + " ms");
		}
	}

	public static void clear() {
		profilingData.clear();
	}
}
