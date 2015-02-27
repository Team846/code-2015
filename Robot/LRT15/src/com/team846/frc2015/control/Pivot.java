package com.team846.frc2015.control;

import com.team846.frc2015.utils.Pair;

public class Pivot
{
	Pair<Double, Double> point;
	double dpt;
	double magnitude;

	public double update()
	{
		magnitude = findRadii(point);
		double angle = dpt / magnitude;
		return angle;
	}

	private double findRadii(Pair<Double, Double> pnt)
	{
		double mag = 0;
		mag = Math.sqrt(Math.pow(pnt.getFirst(), 2)
				+ Math.pow(pnt.getSecond(), 2));
		return mag;
	}

	public double getDpt()
	{
		return dpt;
	}

	public void setDpt(double dpt)
	{
		this.dpt = dpt;
	}

	public Pair<Double, Double> getPoint()
	{
		return point;
	}

	public void setPoint(Pair<Double, Double> point)
	{
		this.point = point;
	}

}
