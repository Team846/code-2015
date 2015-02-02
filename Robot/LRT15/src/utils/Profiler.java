package utils;

import java.util.function.Consumer;
import java.lang.Void;

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
 * 			Profiler.time(Void -> someClass.someMethod(1, 2));
 *			Profiler.time(Void -> someClass.someVoidMethod());
 * 		}
 * }
 */

public class Profiler {
	static long time(Consumer<Object> aFunction) {
		long startTime = System.nanoTime();
		aFunction.accept(null);
		long endTime = System.nanoTime();

		long durationInMs = (endTime - startTime) / 1000000;

		return durationInMs;
	}
}
