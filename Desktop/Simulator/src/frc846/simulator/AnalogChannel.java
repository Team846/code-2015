package frc846.simulator;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AnalogChannel extends JPanel implements ChangeListener{

	private JSlider slider = new JSlider();
	private JTextField text = new JTextField("", 4);
	
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
	
	@Override
	public void stateChanged(ChangeEvent arg0) {
		text.setText(String.valueOf(((JSlider)arg0.getSource()).getValue()));
	}
}
