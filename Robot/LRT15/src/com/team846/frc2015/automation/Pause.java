package com.team846.frc2015.automation;

import edu.wpi.first.wpilibj.Timer;

public class Pause extends Automation {

	boolean timingCycles;
	double time;
	int cycles;
	Timer timer;
	int currentCycles;
	
	public Pause(double time)
	{
		super("Pause");
		this.time = time;
		timingCycles = false;
	}

	public Pause(int cycles)
	{
		super("Pause");
		this.cycles = cycles;
		timingCycles = true;
	}

	public void AllocateResources()
	{
		
	}

	protected boolean Start()
	{
		if (timingCycles)
		{
			currentCycles = 0;
		}
		else
		{
			timer.reset();
			timer.start();
		}
		return true;
	}

	protected boolean Run()
	{
		if (timingCycles)
			currentCycles++;
		return timingCycles ? currentCycles >= cycles : timer.get() > time;
	}

	protected boolean Abort()
	{
		if (timingCycles)
		{
			currentCycles = 0;
		}
		else
		{
			timer.stop();
		}
		return true;
	}

}
