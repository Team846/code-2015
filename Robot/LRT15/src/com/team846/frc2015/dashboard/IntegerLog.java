package com.team846.frc2015.dashboard;


public class IntegerLog extends DashboardLog<Integer>
{	
	public IntegerLog(String id, int value) {
		super(id, value);
	}

	@Override
	public String valueJSON()
	{
		return getValue().toString();
	}
}