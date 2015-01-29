package actuators;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Talon;

public class LRTTalon extends LRTSpeedController {
	
	public static ArrayList<LRTTalon> talon_list = new ArrayList<LRTTalon>();
	
	private double pwm;
	DigitalOutput brake_jumper;
	LRTSpeedController.NeutralMode neutral;
	
	private Talon talon;

	public LRTTalon(int channel, String name, int jumperChannel)
		//LRTSpeedController("LRTTalon" + name),
		//brake_jumper(jumperChannel != 0 ? new DigitalOutput(jumperChannel) : NULL)
	{
		
		super("LRTTalon"+name);
		talon = new Talon(channel);
		brake_jumper = (jumperChannel != 0 ? new DigitalOutput(jumperChannel) : null);
		pwm = 0.0;
		neutral = LRTSpeedController.NeutralMode.kNeutralMode_Coast;
		talon_list.add(this);
		
		System.out.println("Constructed LRTTalon" + name+" on channel " + channel);
	}

	public void SetDutyCycle(double speed)
	{
		pwm = speed;
	}

	public double GetDutyCycle()
	{
		return pwm;
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
		return pwm;
	}

	public void Disable()
	{
		pwm = Talon.kPwmDisabled;
	}

	public void PIDWrite( float output) 
	{
		SetDutyCycle(output);
	}

	public void ConfigNeutralMode(LRTSpeedController.NeutralMode mode)
	{
		neutral = mode;
	}

	LRTSpeedController.NeutralMode GetNeutralMode()
	{
		return neutral;
	}

	public void Update()
	{
		talon.set(pwm);
		if (brake_jumper != null)
		{
			if(neutral == LRTSpeedController.NeutralMode.kNeutralMode_Coast)
				brake_jumper.set(true);
			if(neutral == LRTSpeedController.NeutralMode.kNeutralMode_Brake)
				brake_jumper.set(false);
		}
	}

}
