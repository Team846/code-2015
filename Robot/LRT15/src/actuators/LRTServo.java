package actuators;

import edu.wpi.first.wpilibj.Servo;

public class LRTServo extends Actuator
{
	public enum ControlMode
	{
		kValue, kMicroseconds, kAngle
	}
	private Servo servo;
	public ControlMode m_controlMode;
    double m_value; 
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
			m_controlMode = ControlMode.kValue;
			m_value = 0.0;
			enabled = false;
			previous_value = 999.0;
			
			
	    System.out.println("Created LRTServo "+name+" on channel "+ channel);
	}


public void Output()
{
	if (enabled)
	{
		switch(m_controlMode)
		{
		case kValue:
	        servo.set(m_value);
			break;
		case kMicroseconds:
	    	float val = (float)(m_value - MIN_VAL) / (MAX_VAL - MIN_VAL);
	        servo.set(val);
			break;
		case kAngle:
	        servo.setAngle(m_value);
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
	m_controlMode = ControlMode.kValue;
	m_value = value;
}

public void SetMicroseconds(int ms) 
{
	m_controlMode = ControlMode.kMicroseconds;
	m_value = Math.max(MIN_VAL, Math.min(MAX_VAL,ms));
}

public void SetAngle(float angle)
{
	m_controlMode = ControlMode.kAngle;
	m_value = angle;
}

public void SetControlMode(ControlMode mode)
{
	m_controlMode = mode;
}

double Get()
{
	return m_value;
}

double GetHardwareValue()
{
	return servo.get();
}

public ControlMode GetControlMode()
{
	return m_controlMode;
}

//public void Log()
//{
//	LogToFile(&m_controlMode, "public ControlMode");
//	LogToFile(&m_value, "Value");
//}
//
//public void Send()
//{
//	SendToNetwork(m_value, "S_" + string(GetName()), "ActuatorData");
//}

	

}
