package frc846.simulator;

public class Encoder extends Counter implements Connectable {
	
	public Encoder(int channelA, int channelB)
	{
		super(channelA, channelB);
	}

	public void update(double velocity) {
		count.setText(Integer.toString(Integer.parseInt(count.getText()) + (int)(velocity * 100)));
		rate.setText(Double.toString(velocity * 5000));
	}
}
