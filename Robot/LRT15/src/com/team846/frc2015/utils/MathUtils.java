package com.team846.frc2015.utils;

public class MathUtils
{
	public static <T extends Comparable<T>> T clamp(T val, T min, T max) {
	    if (val.compareTo(min) < 0) return min;
	    else if(val.compareTo(max) > 0) return max;
	    else return val;
	}

	public static <T extends Number> int Sign(T number) {
		if(number.doubleValue() > 0)
			return 1;
		else if(number.doubleValue() < 0)
			return -1;
		else
		return 0;
	}
}
