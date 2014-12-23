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
import javax.swing.JSeparator;
import javax.swing.JSlider;

public class RobotBase {
	private static RobotBase instance;
	
	private JFrame parent = new JFrame();
	private JPanel main = new JPanel(new GridBagLayout());
	private JPanel digitalInputs = new JPanel(new GridBagLayout());
	private JPanel encoders = new JPanel(new GridBagLayout());
	private JPanel analogChannels = new JPanel(new GridBagLayout());
	private JPanel speedControllers = new JPanel(new GridBagLayout());
	private JPanel solenoids = new JPanel(new GridBagLayout());
	private JPanel digitalOutputs = new JPanel(new GridBagLayout());
	
	public RobotBase()
	{
		parent.add(main);
		parent.setVisible(true);
		parent.setTitle("Robot Simulator");
	    parent.setBounds( 100, 50, 1280, 720 );
	    parent.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.9;
		c.weighty = 0.8;
		c.gridwidth = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.PAGE_START;
		main.add(digitalInputs, c);
		c.weightx = 0.1;
		c.fill = GridBagConstraints.VERTICAL;
		c.gridheight = 2;
		main.add(new JSeparator(JSeparator.VERTICAL), c);
		c.weightx = 0.9;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridheight = 1;
		main.add(encoders, c);
		c.weightx = 0.1;
		c.fill = GridBagConstraints.VERTICAL;
		c.gridheight = 2;
		main.add(new JSeparator(JSeparator.VERTICAL), c);
		c.weightx = 0.9;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		main.add(analogChannels, c);
		c.gridy = 1;
		c.gridwidth = 5;
		c.weighty = 0.1;
		c.gridx = 0;
		main.add(new JSeparator(JSeparator.HORIZONTAL), c);
		c.gridy = 2;
		c.gridwidth = 1;
		c.weightx = 0.9;
		c.weighty = 0.8;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = GridBagConstraints.RELATIVE;
		main.add(speedControllers, c);
		c.weightx = 0.1;
		c.fill = GridBagConstraints.VERTICAL;
		main.add(new JSeparator(JSeparator.VERTICAL), c);
		c.weightx = 0.9;
		c.fill = GridBagConstraints.HORIZONTAL;
		main.add(solenoids, c);
		c.weightx = 0.1;
		c.fill = GridBagConstraints.VERTICAL;
		main.add(new JSeparator(JSeparator.VERTICAL), c);
		c.weightx = 0.9;
		c.fill = GridBagConstraints.HORIZONTAL;
		main.add(digitalOutputs, c);
		c.weightx = 1;
		c.weighty = 0.1;
		c.gridwidth = 2;
		c.gridy = GridBagConstraints.RELATIVE;
		digitalInputs.add(new JLabel("Digital Inputs"), c);
		encoders.add(new JLabel("Encoders"), c);
		analogChannels.add(new JLabel("Analog Channels"), c);
		speedControllers.add(new JLabel("Speed Controllers"), c);
		solenoids.add(new JLabel("Solenoids"), c);
		digitalOutputs.add(new JLabel("Digital Outputs"), c);
		instance = this;
		main.revalidate();
		DriverStation.initialize();
		Simulation.initialize();
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
		main.revalidate();
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
		main.revalidate();
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
		main.revalidate();
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
		main.revalidate();
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
		main.revalidate();
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
		main.revalidate();
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
		main.revalidate();
	}
	
	public static RobotBase getInstance()
	{
		return instance;
	}

	public boolean isEnabled()
	{
		return DriverStation.getInstance().isEnabled();
	}
	
	public boolean isDisabled()
	{
		return DriverStation.getInstance().isDisabled();
	}

	public boolean isOperatorControl()
	{
		return DriverStation.getInstance().isOperatorControl();
	}

	public boolean isAutonomous()
	{
		return DriverStation.getInstance().isAutonomous();
	}
	
	protected void requestNextLoop()
	{
		Simulation.getInstance().takeSemaphore();
	}
	
	public void startCompetition()
	{
		new DigitalInput(4);
		DigitalInput di = new DigitalInput(2);
		Talon t = new Talon(4);
		Talon drive = new Talon(3);
		AnalogChannel a = new AnalogChannel(3);
		new Encoder(1, 5);
		DoubleSolenoid s = new DoubleSolenoid(1, 2);
		new Joystick(2);
		Joystick j = new Joystick(1);
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
			drive.set(j.getRawAxis(1));
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			requestNextLoop();
		}
	}
	
	public static void main(String[] args)
	{
		RobotBase robot = new RobotBase();
		robot.startCompetition();
	}
}
