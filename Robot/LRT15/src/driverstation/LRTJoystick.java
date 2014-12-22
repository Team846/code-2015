package driverstation;

import edu.wpi.first.wpilibj.Joystick;

public class LRTJoystick extends Joystick {
	
	int num_buttons;
	int num_axes;
	
	int port;
	
	boolean wasPressed[];
	boolean isPressed[];
	double axisPrevValue[];
	double axisValue[];
	
	public LRTJoystick(int port, int nBtns, int nAxes)
	{
		super(port);
		num_buttons = nBtns;
		num_axes = nAxes;
		//indices are 0-based for roboRIO- MV
		wasPressed = new boolean[nBtns];
        isPressed = new boolean[nBtns];
		axisPrevValue = new double[nAxes];
		axisValue = new double[nAxes]; 
		this.port = port;
        Init();
	}
	
	public boolean ButtonInBounds(int button)
	{
		if(button <= 0 || button > num_buttons)
		{
			System.out.println("[!]DebouncedJoystick: Button " + button + " out of bounds!");
			return false;
		}
		return true;
	}
	
	public boolean AxisInBounds(int axis)
	{
		if (axis <= 0 || axis > num_axes)
		{
			System.out.println("[!]DebouncedJoystick: Axis "+ axis +" out of bounds!\n");
			return false;
		}
		return true;
	}
	
	public void Init()
	{
		 for (int i = 1; i <= num_buttons; ++i)
         {
			 wasPressed[i] = isPressed[i] = false;
         }

         for (int i = 1; i <= num_axes; ++i)
         {
        	 axisPrevValue[i] = axisValue[i] = 0;
         }
	}
	
	public void Update()
	{
		for (int i = 1; i <= num_buttons; ++i)
		{
			wasPressed[i] = isPressed[i];
			isPressed[i] = getRawButton(i);
		}

		for (int i = 1; i <= num_axes; ++i)
		{
			axisPrevValue[i] = axisValue[i];
			axisValue[i] = getRawAxis(i);
		}
	}
	
	public double GetRawAxisDelta(int axis)
	{
		// return positive when stick is pushed forward
		if (!AxisInBounds(axis))
			return 0;
		return axisPrevValue[axis] - axisValue[axis];
	}
	
	public double GetLastAxis(int axis)
	{
		if (!AxisInBounds(axis))
			return 0;
		return axisPrevValue[axis];
	}
	
	public boolean IsButtonJustPressed(int button)
	{
		if (!ButtonInBounds(button))
			return false;
		return isPressed[button] && !wasPressed[button];
	}

	public boolean IsButtonJustReleased(int button)
	{
		if (!ButtonInBounds(button))
			return false;
		return !isPressed[button] && wasPressed[button];
	}

	public boolean IsButtonDown(int button)
	{
		if (!ButtonInBounds(button))
			return false;
		return isPressed[button];
	}

	public boolean WasButtonDown(int button)
	{
		if (!ButtonInBounds(button))
			return false;
		return wasPressed[button];
	}

	public boolean IsHatSwitchJustPressed(int axis, int direction)
	{
		return !WasHatSwitchDown(axis, direction) && IsHatSwitchDown(axis,
				direction);
	}

	public boolean IsHatSwitchJustReleased(int axis, int direction)
	{
		return WasHatSwitchDown(axis, direction) && !IsHatSwitchDown(axis,
				direction);
	}

	public boolean WasHatSwitchDown(int axis, int direction)
	{
		if (!AxisInBounds(axis))
			return false;
		return (axisPrevValue[axis] * direction > 0.5);
	}

	public boolean IsHatSwitchDown(int axis, int direction)
	{
		if (!AxisInBounds(axis))
			return false;
		return (axisValue[axis] * direction > 0.5);
	}

	public int GetNumButtons()
	{
		return num_buttons;
	}

	public int GetNumAxes()
	{
		return num_axes;
	}

	public int GetPort()
	{
		return port;
	}

}
