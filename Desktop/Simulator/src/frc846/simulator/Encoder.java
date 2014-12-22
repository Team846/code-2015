package frc846.simulator;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Encoder extends JPanel {

	private JTextField count = new JTextField("", 4);
	private JTextField rate = new JTextField("", 4);
	
	public Encoder(int channelA, int channelB)
	{
		super(new FlowLayout());
		add(new JLabel("Count"));
		count.setText("0");
		add(count);
		add(new JLabel("Rate"));
		rate.setText("0");
		add(rate);
		RobotBase.getInstance().addEncoder(this, channelA, channelB);
	}
	
	public int get()
	{
		return Integer.getInteger(count.getText());
	}
	
	public double getRate()
	{
		return Double.parseDouble((rate.getText()));
	}
	
	public boolean getStopped()
	{
		return Double.parseDouble((rate.getText())) == 0;
	}
	
	public void start()
	{
		
	}
	
	public void setMaxPeriod()
	{
		
	}
	
	public void setDistancePerPulse()
	{
		
	}
}
