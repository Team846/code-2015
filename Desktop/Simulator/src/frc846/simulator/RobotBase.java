package frc846.simulator;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class RobotBase extends JPanel {
	private static RobotBase instance;
	
	private JPanel digitalInputs = new JPanel(new GridBagLayout());
	private JPanel encoders = new JPanel(new GridBagLayout());
	private JPanel analogChannels = new JPanel(new GridBagLayout());
	private JPanel speedControllers = new JPanel(new GridBagLayout());
	private JPanel solenoids = new JPanel(new GridBagLayout());
	private JPanel digitalOutputs = new JPanel(new GridBagLayout());
	
	public RobotBase()
	{
		super(new GridLayout(0, 3, 10, 10));
		JFrame parent = new JFrame();
		parent.add(this);
		parent.setVisible(true);
		parent.setTitle("Robot Simulator");
	    parent.setBounds( 100, 100, 1280, 720 );
	    parent.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	    this.add(digitalInputs);
	    this.add(encoders);
	    this.add(analogChannels);
	    this.add(speedControllers);
	    this.add(solenoids);
	    this.add(digitalOutputs);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0.1;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.PAGE_START;
		digitalInputs.add(new JLabel("Digital Inputs"), c);
		encoders.add(new JLabel("Encoders"), c);
		analogChannels.add(new JLabel("Analog Channels"), c);
		speedControllers.add(new JLabel("Speed Controllers"), c);
		solenoids.add(new JLabel("Solenoids"), c);
		digitalOutputs.add(new JLabel("Digital Outputs"), c);
		instance = this;
		repaint();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
	}
	
	public void addDigitalInput(JComboBox<String> digitalInput, int channel)
	{
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = channel;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START;
		c.weighty = 0.9;
		c.weightx = 0.2;
		digitalInputs.add(new JLabel(String.valueOf(channel)), c);
		c.gridx = 1;
		c.weightx = 0.8;
		digitalInputs.add(digitalInput, c);
		repaint();
	}

	public void addEncoder(JPanel encoder, int channel)
	{
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = channel;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START;
		c.weighty = 0.9;
		c.weightx = 0.2;
		encoders.add(new JLabel(String.valueOf(channel)), c);
		c.gridx = 1;
		c.weightx = 0.8;
		encoders.add(encoder, c);
		repaint();
	}
	
	public void addEncoder(JPanel encoder, int channelA, int channelB)
	{
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = channelA;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START;
		c.weighty = 0.9;
		c.weightx = 0.2;
		encoders.add(new JLabel(String.valueOf(channelA) + ", " + String.valueOf(channelB)), c);
		c.gridx = 1;
		c.weightx = 0.8;
		encoders.add(encoder, c);
		repaint();
	}

	public void addAnalogChannel(JPanel analogChannel, int channel)
	{
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = channel;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START;
		c.weighty = 0.9;
		c.weightx = 0.1;
		analogChannels.add(new JLabel(String.valueOf(channel)), c);
		c.gridx = 1;
		c.weightx = 0.9;
		analogChannels.add(analogChannel, c);
		repaint();
	}
	
	public void addSpeedController(JPanel speedController, int channel)
	{
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = channel;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START;
		c.weighty = 0.9;
		c.weightx = 0.1;
		speedControllers.add(new JLabel(String.valueOf(channel)), c);
		c.gridx = 1;
		c.weightx = 0.9;
		speedControllers.add(speedController, c);
		repaint();
	}

	public void addSolenoid(JPanel solenoid, int channel)
	{
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = channel;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START;
		c.weighty = 0.9;
		c.weightx = 0.1;
		solenoids.add(new JLabel(String.valueOf(channel)), c);
		c.gridx = 1;
		c.weightx = 0.9;
		solenoids.add(solenoid, c);
		repaint();
	}

	public void addSolenoid(JPanel solenoid, int channelA, int channelB)
	{
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = channelA;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START;
		c.weighty = 0.9;
		c.weightx = 0.1;
		solenoids.add(new JLabel(String.valueOf(channelA) + ", " + String.valueOf(channelB)), c);
		c.gridx = 1;
		c.weightx = 0.9;
		solenoids.add(solenoid, c);
		repaint();
	}
	
	public static RobotBase getInstance()
	{
		return instance;
	}
	
	public static void main(String[] args)
	{
		new RobotBase();
		new DigitalInput(4);
		DigitalInput di = new DigitalInput(2);
		Talon t = new Talon(4);
		new Talon(3);
		AnalogChannel a = new AnalogChannel(3);
		new Encoder(1, 5);
		DoubleSolenoid s = new DoubleSolenoid(1, 2);
		while(true)
		{
			if (di.get())
			{
				s.set(DoubleSolenoid.Value.kForward);
			}
			else
			{
				s.set(DoubleSolenoid.Value.kReverse);
			}
			t.set(a.getVoltage() / 5);
		}
	}
}
