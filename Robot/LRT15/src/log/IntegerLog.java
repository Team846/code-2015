package log;

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
