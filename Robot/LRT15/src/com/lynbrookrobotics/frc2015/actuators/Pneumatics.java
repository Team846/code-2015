package com.lynbrookrobotics.frc2015.actuators;

import java.util.ArrayList;

import com.lynbrookrobotics.frc2015.config.ConfigRuntime;
import com.lynbrookrobotics.frc2015.config.Configurable;
import com.lynbrookrobotics.frc2015.log.AsyncPrinter;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SolenoidBase;

public class Pneumatics extends Actuator implements Configurable{
	private String configSection;

	private int pulse_length;

	private static Compressor compressor = new Compressor(0);
	public static ArrayList<Pneumatics> pneumatic_list = new ArrayList<Pneumatics>();

	private SolenoidBase solenoid;
	private int counter;
	private boolean pulsed;
	private State state;
	
	public enum State
	{
		OFF,
		FORWARD,
		REVERSE
	}
	
	public Pneumatics(int forward, int reverse, String name) 
//		Actuator(name),
//		Configurable("Pneumatics"),
//		Loggable("Pneumatics" + name),
//		configSection("Pneumatics")
	{
		super(name);
		AsyncPrinter.println("Created DoubleSolenoid " + name);
		solenoid = new DoubleSolenoid(forward, reverse);
		counter = 0;
		pulsed = true;
		state = State.OFF;
		
		pneumatic_list.add(this);
		ConfigRuntime.Register(this);
	}

	public Pneumatics(int forward, int reverse, int module, String name) 
//		Actuator(name),
//		Configurable("Pneumatics"),
//		Loggable("Pneumatics" + name),
//		configSection("Pneumatics")
	{
		super(name);
		AsyncPrinter.println("Created DoubleSolenoid " + name);
		solenoid = new DoubleSolenoid(module, forward, reverse);
		counter = 0;
		pulsed = true;
		state = State.OFF;

		pneumatic_list.add(this);
		ConfigRuntime.Register(this);

	}

	public Pneumatics(int forward, String name) 
//		Actuator(name),;
//		Configurable("Pneumatics"),
//		Loggable("Pneumatics" + name),
//		configSection("Pneumatics")
	{
		super(name);
		AsyncPrinter.println("Created Solenoid " + name);
		solenoid = new Solenoid(forward);
		counter = 0;
		pulsed = false;
		state = State.OFF;

		pneumatic_list.add(this);
		ConfigRuntime.Register(this);

	}

	public Pneumatics(int forward, short pcmModule, String name) 
//		Actuator(name),
//		Configurable("Pneumatics"),
//		Loggable("Pneumatics" + name),
//		configSection("Pneumatics")
	{
		super(name);
		AsyncPrinter.println("Created Solenoid " +  name);
		solenoid = new Solenoid(pcmModule, forward);
		counter = 0;
		pulsed = false;
		state = State.OFF;

		pneumatic_list.add(this);
		ConfigRuntime.Register(this);

	}

	public void Output()
	{
		if (pulsed && (solenoid instanceof DoubleSolenoid))
		{
			if (counter > 0)
			{
				counter--;
				if (state == State.FORWARD)
				{
					((DoubleSolenoid)solenoid).set(DoubleSolenoid.Value.kForward);
				}
				else if (state == State.REVERSE)
				{
					((DoubleSolenoid)solenoid).set(DoubleSolenoid.Value.kReverse);
				}
				else if (state == State.OFF)
				{
					((DoubleSolenoid)solenoid).set(DoubleSolenoid.Value.kOff);
				}
			}
			else
			{
				counter = 0;
				((DoubleSolenoid)solenoid).set(DoubleSolenoid.Value.kOff);
			}
		}
		else
		{
			if (state == State.FORWARD)
			{
				if (solenoid instanceof Solenoid)
					((Solenoid)solenoid).set(true);
				else if (solenoid instanceof DoubleSolenoid)
					((DoubleSolenoid)solenoid).set(DoubleSolenoid.Value.kForward);
			}
			else if (state == State.OFF)
			{
				if (solenoid instanceof Solenoid)
					((Solenoid)solenoid).set(false);
				else if (solenoid instanceof DoubleSolenoid)
					((DoubleSolenoid)solenoid).set(DoubleSolenoid.Value.kOff);
			}
			else if (state == State.REVERSE)
			{
				if (solenoid instanceof DoubleSolenoid)
					((DoubleSolenoid)solenoid).set(DoubleSolenoid.Value.kReverse);
			}
		}
	}

	public static void CreateCompressor()
	{
		compressor.start();
	}

	public static void DestroyCompressor()
	{
		compressor.stop();
		compressor.free();
	}

	public static void SetCompressor(boolean on)
	{
		if (on)
		{
			compressor.start();
		}
		else
		{
			compressor.stop();
		}
	}

	public void Set(State on, boolean force)
	{
		if (on != state || force)
		{
			state = on;
			if (solenoid instanceof Solenoid && state == State.REVERSE)
			{
				state = State.OFF;
			}
			if (pulsed)
			{
				counter = pulse_length;
			}
		}
	}
	
	public void Set(State on)
	{
		Set(on, false);
	}

	public State Get()
	{
		return state;
	}

	public State GetHardwareValue()
	{
		State current = State.OFF;
		if (solenoid instanceof DoubleSolenoid)
		{
			if (((DoubleSolenoid)solenoid).get() == DoubleSolenoid.Value.kForward)
				current = State.FORWARD;
			else if (((DoubleSolenoid)solenoid).get() == DoubleSolenoid.Value.kReverse)
				current = State.REVERSE;
			else
				current = State.OFF;
		}
		else if (solenoid instanceof Solenoid)
		{
			if (((Solenoid)solenoid).get())
				current = State.FORWARD;
			else
				current = State.OFF;
		}
		return current;
	}

	public void Configure()
	{
		pulse_length = (int)GetConfig("pulseLength", 25);
	}
//
//	void Log()
//	{
//		LogToFile(&state, "State");
//	}
//
//	void Send()
//	{
//		SendToNetwork(state == State.FORWARD ? true : false, "P_" + String(GetName()), "ActuatorData");
//	}

}
