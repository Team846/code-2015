package com.team846.frc2015.control;

import com.team846.frc2015.utils.Pair;

public class Pivot
{
	double R;
	
	public Pivot(double r){
		this.R = r;
	}
	
	public void update(double angularChange)
	{
		double W = angularVelocity(angularChange);
		double strafe = strafeVelocity(W);
	}

	private double findRadii(Pair<Double, Double> pnt)
	{
		double mag = 0;
		mag = Math.sqrt(Math.pow(pnt.getFirst(), 2) + Math.pow(pnt.getSecond(), 2));
		return mag;
	}
	
	private double angularVelocity(double angleChange){
		double w = 0;
		w = angleChange/0.02;
		
		return w;
	}
	
	private double strafeVelocity(double W){
		double Vt = W * R;
		
		return Vt;
	}
}
