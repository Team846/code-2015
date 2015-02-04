package com.lynbrookrobotics.frc2015.dashboard;

public class BooleanLog extends DashboardLog<Boolean>
{
	public BooleanLog(String id, Boolean value)
	{
		super(id, value);
	}

	@Override
	public String valueJSON()
	{
		return getValue().toString();
	}
}
