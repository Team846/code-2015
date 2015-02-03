package dashboard;

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
