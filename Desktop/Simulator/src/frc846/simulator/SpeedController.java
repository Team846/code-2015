package frc846.simulator;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

public class SpeedController extends JPanel {
	private double value = 0;
	private JSlider slider = new JSlider();
	private JTextField text = new JTextField("", 4);
	
	public SpeedController(int channel)
	{
		super(new FlowLayout());
		slider.setEnabled(false);
		slider.setMaximum(100);
		slider.setMinimum(-100);
		slider.setPaintTicks(true);
		slider.setMajorTickSpacing(100);
		slider.setValue((int)(value * 100));
		add(slider);
		text.setEditable(false);
		text.setText(String.valueOf(value));
		add(text);
		setOpaque(true);
		RobotBase.getInstance().addSpeedController(this, channel);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (value > 0)
		{
			setBackground(Color.GREEN);
		}
		else if (value < 0)
		{
			setBackground(Color.RED);
		}
		else
		{
			setBackground(Color.GRAY);
		}
	}
	
	public double get()
	{
		return value;
	}
	
	public void set(double v)
	{
		value = Math.max(Math.min(v, 1.0), -1.0);
		slider.setValue((int)(v * 100));
		text.setText(String.valueOf(v));
		repaint();
	}
}
