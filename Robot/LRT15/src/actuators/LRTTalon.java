package actuators;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Talon;

public class LRTTalon extends LRTSpeedController {
	
	public static ArrayList<LRTTalon> talon_list = new ArrayList<LRTTalon>();
	
	private double m_pwm;
	DigitalOutput m_brake_jumper;
	LRTSpeedController.NeutralMode m_neutral;
	
	private Talon talon;

	LRTTalon(int channel, String name, int jumperChannel)
		//LRTSpeedController("LRTTalon" + name),
		//m_brake_jumper(jumperChannel != 0 ? new DigitalOutput(jumperChannel) : NULL)
	{
		super("LRTTalon"+name);
		talon = new Talon(channel);
		m_brake_jumper = (jumperChannel != 0 ? new DigitalOutput(jumperChannel) : null);
		m_pwm = 0.0;
		m_neutral = LRTSpeedController.NeutralMode.kNeutralMode_Coast;
		talon_list.add(this);
		
		System.out.println("Constructed LRTTalon" + name+" on channel " + channel);
	}

	public void SetDutyCycle(double speed)
	{
		m_pwm = speed;
	}

	public double GetDutyCycle()
	{
		return m_pwm;
	}

	public double GetHardwareValue()
	{
		return talon.get();
	}

	public void Set( float speed)
	{
		System.out.println("[WARNING] Calling Set() in LRTTalon: "+ GetName() +" use SetDutyCycle() instead");
		SetDutyCycle(speed);
	}

	public double Get()
	{
		return m_pwm;
	}

	public void Disable()
	{
		m_pwm = Talon.kPwmDisabled;
	}

	public void PIDWrite( float output) 
	{
		SetDutyCycle(output);
	}

	public void ConfigNeutralMode(LRTSpeedController.NeutralMode mode)
	{
		m_neutral = mode;
	}

	LRTSpeedController.NeutralMode GetNeutralMode()
	{
		return m_neutral;
	}

	public void Update()
	{
		talon.set(m_pwm);
		if (m_brake_jumper != null)
		{
			if(m_neutral == LRTSpeedController.NeutralMode.kNeutralMode_Coast)
				m_brake_jumper.set(true);
			if(m_neutral == LRTSpeedController.NeutralMode.kNeutralMode_Brake)
				m_brake_jumper.set(false);
		}
	}

}
