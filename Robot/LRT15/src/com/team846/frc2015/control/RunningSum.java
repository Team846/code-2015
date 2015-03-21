package com.team846.frc2015.control;

public class RunningSum {
	
	private double decayConstant;
	private double runningSum;
	
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
