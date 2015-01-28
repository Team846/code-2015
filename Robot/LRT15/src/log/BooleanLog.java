package log;

public class BooleanLog extends DashboardLog<Boolean>
{
	public BooleanLog(String id, Boolean value)
	{
		super(id, value);
	}

	@Override
	String valueJSON()
	{
		return getValue().toString();
	}
}
