package frc846.simulator;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class DigitalOutput extends JPanel {

	private boolean value = false;
	private JTextField text = new JTextField("", 10);
	
	public DigitalOutput(int channel)
	{
		setBackground(Color.GRAY);
		setOpaque(true);
		text.setText(String.valueOf(value));
		add(text);
		RobotBase.getInstance().addDigitalOutput(this, channel);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (value)
		{
			setBackground(Color.GREEN);
		}
		else
		{
			setBackground(Color.GRAY);
		}
	}
	
	public void set(boolean on)
	{
		value = on;
		text.setText(String.valueOf(on ? "On" : "Off"));
		repaint();
	}
}
