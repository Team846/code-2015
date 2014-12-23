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
		velocity += power / mass;
		if (velocity > power || velocity < -power)
			velocity = power;
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
