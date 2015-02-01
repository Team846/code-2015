package componentData;

public class CarriageExtenderData extends ComponentData 
{
	private ControlMode control;
	private Setpoint setpoint;
	
	private double maxSpeed;
	private double speed;
	
	public enum ControlMode
	{
		AUTOMATED,
		MANUAL
	}
	
	public enum Setpoint
	{
		RETRACT, 
		EXTEND
	}
	public CarriageExtenderData() {
		super("CarriageExtenderData");
		ResetCommands();
	}
	
	public static CarriageExtenderData get()
	{
		return (CarriageExtenderData)ComponentData.GetComponentData("CarriageExtenderData");
	}
	
	public void setMaxSpeed(double speed)
	{
		maxSpeed = speed;
	}
	
	public double getMaxSpeed()
	{
		return maxSpeed;
	}
	
	public void setCarriageSpeed(double speed)
	{
		this.speed = speed * maxSpeed;
	}
	
	public double getCarriageSpeed()
	{
		return speed;
	}
	
	public ControlMode getControlMode()
	{
		return control;
	}
	
	public void setControlMode(ControlMode controlMode)
	{
		control = controlMode;
	}
	
	public void setSetpoint(Setpoint set)
	{
		setpoint = set;
	}
	
	public Setpoint getSetpoint()
	{
		return setpoint;
	}

	@Override
	protected void ResetCommands() 
	{
		maxSpeed = 1.0;
		speed = 0.0;
		control = ControlMode.MANUAL;
		setpoint = Setpoint.RETRACT;
	}

}
