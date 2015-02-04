package com.lynbrookrobotics.frc2015.dashboard;

public class FloatLog extends DashboardLog<Float>
{
	public FloatLog(String id, Float value)
	{
		super(id, value);
	}

	@Override
	public String valueJSON()
	{
		return getValue().toString();
	}
}
