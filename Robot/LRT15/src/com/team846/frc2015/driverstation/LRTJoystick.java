package com.team846.frc2015.driverstation;

import com.team846.frc2015.utils.AsyncPrinter;

import edu.wpi.first.wpilibj.Joystick;

public class LRTJoystick extends Joystick {
	
	private final int num_buttons;
	private final int num_axes;
	
	private final int port;
	
	private final boolean[] wasPressed;
	private final boolean[] isPressed;
	private final double[] axisPrevValue;
	private final double[] axisValue;
	
	public LRTJoystick(int port, int nBtns, int nAxes)
	{
		super(port);
		num_buttons = nBtns;
		num_axes = nAxes;
		//axis indices are 0-based - MV
		wasPressed = new boolean[nBtns + 1];
        isPressed = new boolean[nBtns + 1];
		axisPrevValue = new double[nAxes];
		axisValue = new double[nAxes]; 
		this.port = port;
        init();
	}
	
	private boolean buttonInBounds(int button)
	{
		if(button < 1 || button > num_buttons)
		{
			AsyncPrinter.warn("DebouncedJoystick: Button " + button + " out of bounds!");
			return false;
		}
		return true;
	}
	
	private boolean axisInBounds(int axis)
	{
		if (axis < 0 || axis > num_axes - 1)
		{
			AsyncPrinter.warn("DebouncedJoystick: Axis "+ axis +" out of bounds!\n");
			return false;
		}
		return true;
	}
	
	private void init()
	{
		 for (int i = 1; i < num_buttons + 1; ++i)
         {
			 wasPressed[i] = isPressed[i] = false;
         }

         for (int i = 0; i < num_axes; ++i)
         {
        	 axisPrevValue[i] = axisValue[i] = 0;
         }
	}
	
	public void update()
	{
		for (int i = 1; i < num_buttons + 1; ++i)
		{
			wasPressed[i] = isPressed[i];
			isPressed[i] = getRawButton(i);
		}

		for (int i = 0; i < num_axes ; ++i)
		{
			axisPrevValue[i] = axisValue[i];
			axisValue[i] = getRawAxis(i);
		}
	}
	
	public double getRawAxisDelta(int axis)
	{
		// return positive when stick is pushed forward
		if (!axisInBounds(axis))
			return 0;
		return axisPrevValue[axis] - axisValue[axis];
	}
	
	public double getLastAxis(int axis)
	{
		if (!axisInBounds(axis))
			return 0;
		return axisPrevValue[axis];
	}
	
	public boolean isButtonJustPressed(int button)
	{
		if (!buttonInBounds(button))
			return false;
		return isPressed[button] && !wasPressed[button];
	}

	public boolean isButtonJustReleased(int button)
	{
		if (!buttonInBounds(button))
			return false;
		return !isPressed[button] && wasPressed[button];
	}

	public boolean isButtonDown(int button)
	{
		if (!buttonInBounds(button))
			return false;
		return isPressed[button];
	}

	public boolean wasButtonDown(int button)
	{
		if (!buttonInBounds(button))
			return false;
		return wasPressed[button];
	}

	public boolean isHatSwitchJustPressed(int axis, int direction)
	{
		return !wasHatSwitchDown(axis, direction) && isHatSwitchDown(axis,
				direction);
	}

	public boolean isHatSwitchJustReleased(int axis, int direction)
	{
		return wasHatSwitchDown(axis, direction) && !isHatSwitchDown(axis,
				direction);
	}

	boolean wasHatSwitchDown(int axis, int direction)
	{
		if (!axisInBounds(axis))
			return false;
		return (axisPrevValue[axis] * direction > 0.5);
	}

	boolean isHatSwitchDown(int axis, int direction)
	{
		if (!axisInBounds(axis))
			return false;
		return (axisValue[axis] * direction > 0.5);
	}

	public int getNumButtons()
	{
		return num_buttons;
	}

	public int getNumAxes()
	{
		return num_axes;
	}

	public int getPort()
	{
		return port;
	}

}
