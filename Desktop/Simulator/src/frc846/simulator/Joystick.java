package frc846.simulator;

import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;

public class Joystick extends JPanel {
	
	private static final int numAxes = 3;
	private static final int numButtons = 12;
	
	private JSlider[] axes = new JSlider[numAxes];
	private JToggleButton[] buttons = new JToggleButton[numButtons];
	
	public Joystick(int port)
	{
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		for (int i = 0; i < numAxes; i++)
		{
			axes[i] = new JSlider();
			axes[i].setMinimum(-32768);
			axes[i].setMaximum(32768);
			axes[i].setMajorTickSpacing(32768);
			axes[i].setPaintTicks(true);
			axes[i].setValue(0);
			add(axes[i]);
		}
		JPanel buttons = new JPanel(new GridLayout(2, 6));
		for (int i = 1; i <= numButtons; i++)
		{
			this.buttons[i - 1] = new JToggleButton();
			this.buttons[i - 1].setText(String.valueOf(i));
			buttons.add(this.buttons[i - 1]);
		}
		add(buttons);
		DriverStation.getInstance().addJoystick(this, port);
	}
	
	double getRawAxis(int axis)
	{
		if (axis < 1 || axis > 3)
			return 0.0;
		return axes[axis - 1].getValue() / 32768.0;
	}

	boolean getRawButton(int button)
	{
		if (button < 1 || button > 12)
			return false;
		return buttons[button - 1].isSelected();
	}
}
