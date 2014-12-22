package frc846.simulator;

import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JTextField;

public class DigitalInput extends JComboBox<String> {

private static String[] choices = {"0", "1"};

public DigitalInput(int channel)
{
	super(choices);
	RobotBase.getInstance().addDigitalInput(this, channel);
}

public boolean get()
{
	String selected = (String)getSelectedItem();
	if (selected == "1")
		return true;
	return false;
}
}