package frc846.simulator;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class DoubleSolenoid extends JPanel {
	
	public static class Value {

        public final int value;
        public static final int kOff_val = 0;
        public static final int kForward_val = 1;
        public static final int kReverse_val = 2;
        public static final Value kOff = new Value(kOff_val);
        public static final Value kForward = new Value(kForward_val);
        public static final Value kReverse = new Value(kReverse_val);

        private Value(int value) {
            this.value = value;
        }
    }
	private Value value = Value.kOff;
	private JTextField text = new JTextField("", 10);
	
	public DoubleSolenoid(int channelA, int channelB)
	{
		setBackground(Color.GRAY);
		setOpaque(true);
		text.setText("Off");
		text.setEditable(false);
		add(text);
		RobotBase.getInstance().addSolenoid(this, channelA, channelB);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (value == Value.kForward)
		{
			setBackground(Color.GREEN);
		}
		else if (value == Value.kReverse)
		{
			setBackground(Color.RED);
		}
		else
		{
			setBackground(Color.GRAY);
		}
	}
	
	public void set(Value v)
	{
		value = v;
		if (value == Value.kForward)
		{
			text.setText("Forward");
		}
		else if (value == Value.kReverse)
		{
			text.setText("Reverse");
		}
		else
		{
			text.setText("Off");
		}
	}
	
	public Value get()
	{
		return value;
	}
}
