package actuators;

import edu.wpi.first.wpilibj.Servo;

public class LRTServo extends Actuator
{
	public enum ControlMode
	{
		kValue, kMicroseconds, kAngle
	}
	private Servo servo;
	public ControlMode controlMode;
    double value; 
    boolean enabled;
    double previous_value;
    
	static final int MIN_VAL = 727;
	static final int MAX_VAL = 2252;

	public LRTServo(String name) {
		super(name);
	}
	
	LRTServo(int channel, String name)
	{
			super(name);
			servo = new Servo(channel);
			controlMode = ControlMode.kValue;
			value = 0.0;
			enabled = false;
			previous_value = 999.0;
			
			
	    System.out.println("Created LRTServo "+name+" on channel "+ channel);
	}


public void Output()
{
	if (enabled)
	{
		switch(controlMode)
		{
		case kValue:
	        servo.set(value);
			break;
		case kMicroseconds:
	    	float val = (float)(value - MIN_VAL) / (MAX_VAL - MIN_VAL);
	        servo.set(val);
			break;
		case kAngle:
	        servo.setAngle(value);
			break;
		}
	}
	else
       ;// servo.SetOffline(); TODO:Find Safety
}

public void SetEnabled(boolean enabled)
{
    this.enabled = enabled;
}

boolean IsEnabled()
{
    return enabled;
}

public void Set(float value)
{
	controlMode = ControlMode.kValue;
	value = value;
}

public void SetMicroseconds(int ms) 
{
	controlMode = ControlMode.kMicroseconds;
	value = Math.max(MIN_VAL, Math.min(MAX_VAL,ms));
}

public void SetAngle(float angle)
{
	controlMode = ControlMode.kAngle;
	value = angle;
}

public void SetControlMode(ControlMode mode)
{
	controlMode = mode;
}

double Get()
{
	return value;
}

double GetHardwareValue()
{
	return servo.get();
}

public ControlMode GetControlMode()
{
	return controlMode;
}

//public void Log()
//{
//	LogToFile(&controlMode, "public ControlMode");
//	LogToFile(&value, "Value");
//}
//
//public void Send()
//{
//	SendToNetwork(value, "S_" + string(GetName()), "ActuatorData");
//}

	

}
