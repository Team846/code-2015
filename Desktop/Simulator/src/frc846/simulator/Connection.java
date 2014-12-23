package frc846.simulator;

public class Connection {
	private SpeedController motor;
	private Connectable sensor;
	private double mass;
	private boolean reverse;
	private double velocity = 0;
	
	public Connection(SpeedController s, Connectable c, double mass, boolean reverse)
	{
		motor = s;
		sensor = c;
		this.mass = mass;
		this.reverse = reverse;
	}
	
	public void update()
	{
		double power = motor.get();
		if (Math.abs(velocity) < Math.abs(power) || Math.signum(power) != Math.signum(velocity))
			velocity += power / mass;
		else
		{
			if (velocity > 0)
				velocity -= 0.01;
			else if (velocity < 0)
				velocity += 0.01;
		}
		if (reverse)
			sensor.update(-velocity);
		else
			sensor.update(velocity);
	}
	
	public void disable()
	{
		velocity = 0;
		sensor.update(velocity);
	}
}
