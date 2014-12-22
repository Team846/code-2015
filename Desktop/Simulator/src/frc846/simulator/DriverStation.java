package frc846.simulator;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;

public class DriverStation {
	private static DriverStation instance = null;
	
	private JPanel main = new JPanel(new GridBagLayout());
	private JTextField battery = new JTextField("13.0", 4);
	private JRadioButton enable = new JRadioButton();
	private JRadioButton disable = new JRadioButton();
	private JRadioButton teleoperated = new JRadioButton();
	private JRadioButton autonomous = new JRadioButton();
	
	public static void initialize()
	{
		instance = new DriverStation();
	}
	
	public static DriverStation getInstance()
	{
		return instance;
	}
	
	public DriverStation()
	{
		JFrame parent = new JFrame();
		parent.add(main);
		parent.setVisible(true);
		parent.setTitle("Driver Station");
	    parent.setBounds( 100, 770, 1280, 240 );
	    parent.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	    ButtonGroup enabled = new ButtonGroup();
	    enabled.add(enable);
	    enabled.add(disable);
	    disable.setSelected(true);
	    ButtonGroup mode = new ButtonGroup();
	    mode.add(teleoperated);
	    mode.add(autonomous);
	    teleoperated.setSelected(true);
	    GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.2;
		c.weighty = 0.1;
		c.gridx = 0;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.PAGE_START;
		c.gridx = 0;
		main.add(new JLabel("Battery Voltage:"), c);
		c.gridx = 1;
		main.add(battery, c);
		c.gridx = 0;
		main.add(enable, c);
		main.add(disable, c);
		c.gridx = 1;
		main.add(new JLabel("Enable"), c);
		main.add(new JLabel("Disable"), c);
		c.gridx = 0;
	    c.gridwidth = 2;
	    main.add(new JSeparator(JSeparator.HORIZONTAL), c);
	    c.gridwidth = 1;
	    main.add(teleoperated, c);
	    main.add(autonomous, c);
		c.gridx = 1;
		main.add(new JLabel("Teleoperated"), c);
		main.add(new JLabel("Autonomous"), c);
		main.revalidate();
	}
	
	public void addJoystick(JPanel joystick, int port)
	{
	    GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.VERTICAL;
	    c.gridy = 0;
		c.gridx = port * 2;
		c.gridheight = 6;
		c.weightx = 0.1;
		main.add(new JSeparator(JSeparator.VERTICAL), c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = port * 2 + 1;
	    c.gridy = 0;
		c.weightx = 0.9;
		c.weighty = 0.1;
		main.add(new JLabel("Joystick " + String.valueOf(port)), c);
	    c.gridy = 1;
		c.weighty = 0.9;
		main.add(joystick, c);
		main.revalidate();
	}
	
	public boolean isEnabled()
	{
		return enable.isSelected();
	}
	
	public boolean isDisabled()
	{
		return enable.isSelected();
	}

	public boolean isOperatorControl()
	{
		return teleoperated.isSelected();
	}

	public boolean isAutonomous()
	{
		return autonomous.isSelected();
	}
	
	public double getBatteryVoltage()
	{
		return Double.parseDouble(battery.getText());
	}
	
	public boolean isFMSAttached()
	{
		return false;
	}
}
