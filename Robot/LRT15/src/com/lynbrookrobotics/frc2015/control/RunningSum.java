package com.lynbrookrobotics.frc2015.control;

public class RunningSum {
	
	double decayConstant;
	double runningSum;
	
	RunningSum(double decayConstant)
	{
		this.decayConstant = decayConstant;
		runningSum = 0;
	}
	
	public double UpdateSum(double x)
	{
		runningSum *= decayConstant;
		runningSum += x;
		return runningSum * (1 - decayConstant);
	}
	
	public void Clear()
	{
		runningSum = 0;
	}
	
	public void setDecayConstant(double decayConstant)
	{
		this.decayConstant = decayConstant;
	}

}
