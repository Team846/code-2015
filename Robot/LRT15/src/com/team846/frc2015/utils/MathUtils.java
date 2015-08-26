package com.team846.frc2015.utils;

public class MathUtils
{
	public static <T extends Comparable<T>> T clamp(T val, T min, T max) {
	    if (val.compareTo(min) < 0) return min;
	    else if(val.compareTo(max) > 0) return max;
	    else return val;
	}

	public static <T extends Number> int sign(T number) {
		if(number.doubleValue() > 0)
			return 1;
		else if(number.doubleValue() < 0)
			return -1;
		else
		return 0;
	}
	
	//TODO: fix to work with arbitrary number types
	public static double rescale(double d, double min0, double max0, double min1, double max1)
	{
		if (max0 == min0)
			return min1;
		d = MathUtils.clamp(d, min0, max0);
		return (d - min0) * (max1 - min1) / (max0 - min0) + min1;
	}
}
