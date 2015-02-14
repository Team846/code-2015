package com.team846.frc2015.dashboard;

public class StringLog extends DashboardLog<String>
{
	public StringLog(String id, String value)
	{
		super(id, value);
	}

	@Override
	public String valueJSON()
	{
		return '"' + getValue() + '"';
	}
}
