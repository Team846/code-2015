package com.team846.frc2015.actuators;

import java.util.ArrayList;

import com.team846.frc2015.config.ConfigRuntime;
import com.team846.frc2015.config.Configurable;
import com.team846.frc2015.logging.AsyncLogger;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SolenoidBase;

public class Pneumatics extends Actuator implements Configurable{
	
	private String configSection;

	private int pulseLength;

	private static final Compressor compressor = new Compressor(1);
	private static ArrayList<Pneumatics> pneumatic_list = new ArrayList<Pneumatics>();

	private final SolenoidBase solenoid;
	private int counter;
	private final boolean pulsed;
	private PneumaticState state;
	
	public enum PneumaticState
	{
		OFF,
		FORWARD,
		REVERSE
	}
	
	public Pneumatics(int forward, int reverse, String name) 
	{
		super(name);
		AsyncLogger.info("[Pneumatics] Created DoubleSolenoid " + name);
		solenoid = new DoubleSolenoid(forward, reverse);
		counter = 0;
		pulsed = true;
		state = PneumaticState.OFF;
		
		pneumatic_list.add(this);
		ConfigRuntime.Register(this);
	}

	public Pneumatics(int forward, int reverse, int module, String name) 
	{
		super(name);
		AsyncLogger.info("[Pneumatics] Created DoubleSolenoid " + name);
		solenoid = new DoubleSolenoid(module, forward, reverse);
		counter = 0;
		pulsed = true;
		state = PneumaticState.OFF;

		pneumatic_list.add(this);
		ConfigRuntime.Register(this);

	}

	public Pneumatics(int forward, String name) 
	{
		super(name);
		AsyncLogger.info("[Pneumatics] Created Solenoid " + name);
		solenoid = new Solenoid(forward);
		counter = 0;
		pulsed = false;
		state = PneumaticState.OFF;

		pneumatic_list.add(this);
		ConfigRuntime.Register(this);

	}

	public Pneumatics(int forward, short pcmModule, String name) 
	{
		super(name);
		AsyncLogger.info("[Pneumatics] Created Solenoid " + name);
		solenoid = new Solenoid(pcmModule, forward);
		counter = 0;
		pulsed = false;
		state = PneumaticState.OFF;

		pneumatic_list.add(this);
		ConfigRuntime.Register(this);

	}

	public void output()
	{
		if (pulsed && (solenoid instanceof DoubleSolenoid))
		{
			if (counter > 0)
			{
				counter--;
				if (state == PneumaticState.FORWARD)
				{
					((DoubleSolenoid)solenoid).set(DoubleSolenoid.Value.kForward);
				}
				else if (state == PneumaticState.REVERSE)
				{
					((DoubleSolenoid)solenoid).set(DoubleSolenoid.Value.kReverse);
				}
				else if (state == PneumaticState.OFF)
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
			if (state == PneumaticState.FORWARD)
			{
				if (solenoid instanceof Solenoid)
					((Solenoid)solenoid).set(true);
				else if (solenoid instanceof DoubleSolenoid)
					((DoubleSolenoid)solenoid).set(DoubleSolenoid.Value.kForward);
			}
			else if (state == PneumaticState.OFF)
			{
				if (solenoid instanceof Solenoid)
					((Solenoid)solenoid).set(false);
				else if (solenoid instanceof DoubleSolenoid)
					((DoubleSolenoid)solenoid).set(DoubleSolenoid.Value.kOff);
			}
			else if (state == PneumaticState.REVERSE)
			{
				if (solenoid instanceof DoubleSolenoid)
					((DoubleSolenoid)solenoid).set(DoubleSolenoid.Value.kReverse);
			}
		}
	}

	public static void createCompressor()
	{
		compressor.start();
	}

	public static void destroyCompressor()
	{
		compressor.stop();
		compressor.free();
	}

	public static void setCompressor(boolean on)
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

	void set(PneumaticState on, boolean force)
	{
		if (on != state || force)
		{
			state = on;
			if (solenoid instanceof Solenoid && state == PneumaticState.REVERSE)
			{
				state = PneumaticState.OFF;
			}
			if (pulsed)
			{
				counter = pulseLength;
			}
		}
	}
	
	public void set(PneumaticState on)
	{
		set(on, false);
	}

	public PneumaticState get()
	{
		return state;
	}

	public PneumaticState getHardwareValue()
	{
		PneumaticState current = PneumaticState.OFF;
		if (solenoid instanceof DoubleSolenoid)
		{
			if (((DoubleSolenoid)solenoid).get() == DoubleSolenoid.Value.kForward)
				current = PneumaticState.FORWARD;
			else if (((DoubleSolenoid)solenoid).get() == DoubleSolenoid.Value.kReverse)
				current = PneumaticState.REVERSE;
			else
				current = PneumaticState.OFF;
		}
		else if (solenoid instanceof Solenoid)
		{
			if (((Solenoid)solenoid).get())
				current = PneumaticState.FORWARD;
			else
				current = PneumaticState.OFF;
		}
		return current;
	}

	public void Configure()
	{
		pulseLength = GetConfig("pulseLength", 6);
	}
}
