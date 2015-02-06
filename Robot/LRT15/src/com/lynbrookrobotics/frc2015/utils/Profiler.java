package com.lynbrookrobotics.frc2015.utils;

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
 * 			Profiler.time(Void -> someClass.someMethod(1, 2), "id1");
 *			Profiler.time(Void -> someClass.someVoidMethod(), "id2");
 *			
 *			Profiler.show("id1");
 *			Profiler.show("id2");
 * 		}
 * }
 */

public class Profiler {
	private Profiler() {
	};

	static private final String tab = "\t";

	private static ConcurrentHashMap<String, Float> threadData = new ConcurrentHashMap<String, Float>(); // eternal
																											// shame
	private static void log(String msg) {
		AsyncPrinter.println(msg.toString());
	}

	public static void time(Consumer<Object> aFunction, String methodName) {
		long startTime = System.nanoTime();
		aFunction.accept(null);
		long endTime = System.nanoTime();

		threadData.put(methodName, (float) (endTime - startTime) / 1000000);
	}

	public static void show(String methodName) {
		log("Profiler output:");

		log(tab + "Method name: " + methodName);
		log(tab + "Time taken: " + threadData.get(methodName) + "ms");
	}
}
