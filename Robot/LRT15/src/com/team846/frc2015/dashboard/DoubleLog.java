package com.team846.frc2015.dashboard;

public class DoubleLog extends DashboardLog<Double>
{
	public DoubleLog(String id, Double value)
	{
		super(id, value);
	}

	@Override
	public String valueJSON()
	{
		return getValue().toString();
	}
}
