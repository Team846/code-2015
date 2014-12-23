package frc846.simulator;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AnalogChannel extends JPanel implements ChangeListener, Connectable {

	private JSlider slider = new JSlider();
	private JTextField text = new JTextField("", 4);
	private boolean overLimit = false;
	
	public AnalogChannel(int channel)
	{
		super(new FlowLayout());
		slider.setMaximum(1024);
		slider.setMinimum(0);
		slider.setMajorTickSpacing(1);
		add(slider);
		text.setEditable(false);
		text.setText(String.valueOf(slider.getValue()));
		add(text);
		RobotBase.getInstance().addAnalogChannel(this, channel);
		slider.addChangeListener(this);
		setBackground(Color.RED);
		setOpaque(false);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (overLimit)
		{
			setOpaque(true);
		}
		else
		{
			setOpaque(false);
		}
	}
	
	public int getAverageValue()
	{
		return getValue();
	}
	
	public double getAverageVoltage()
	{
		return getVoltage();
	}
	
	public int getValue()
	{
		return slider.getValue();
	}
	
	public double getVoltage()
	{
		return slider.getValue() / 1024.0 * 5.0;
	}
	
	public void stateChanged(ChangeEvent arg0) {
		text.setText(String.valueOf(((JSlider)arg0.getSource()).getValue()));
	}

	public void update(double velocity) {
		int newValue = slider.getValue() + (int)(velocity * 10);
		if (newValue > 1024)
		{
			overLimit = true;
			newValue = 1024;
		}
		else if (newValue < 0)
		{
			overLimit = true;
			newValue = 0;
		}
		else
		{
			overLimit = false;
		}
		slider.setValue(newValue);
	}
}
