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

public class RobotBase extends JPanel {
	private static RobotBase instance;
	
	private JPanel digitalInputs = new JPanel(new GridBagLayout());
	private JPanel encoders = new JPanel(new GridBagLayout());
	private JPanel analogInputs = new JPanel(new GridBagLayout());
	public RobotBase()
	{
		super(new GridLayout(0, 3, 5, 5));
		JFrame parent = new JFrame();
		parent.add(this);
		parent.setVisible(true);
		parent.setTitle("Robot Simulator");
	    parent.setBounds( 100, 100, 640, 480 );
	    parent.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	    this.add(digitalInputs);
	    this.add(encoders);
	    this.add(analogInputs);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0.1;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.PAGE_START;
		digitalInputs.add(new JLabel("Digital Inputs"), c);
		encoders.add(new JLabel("Encoders"), c);
		analogInputs.add(new JLabel("Analog Inputs"), c);
		instance = this;
		repaint();
	}
	
	public void paintComponent(Graphics g)
	{
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

	public static RobotBase getInstance()
	{
		return instance;
	}
	
	public static void main(String[] args)
	{
		new RobotBase();
		new DigitalInput(4);
		new DigitalInput(2);
	}
}
