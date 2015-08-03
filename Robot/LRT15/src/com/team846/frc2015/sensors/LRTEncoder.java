package com.team846.frc2015.sensors;

import edu.wpi.first.wpilibj.Encoder;

public class LRTEncoder extends Encoder {

	private final double trim = 1.0;

	public LRTEncoder(int aChannel, int bChannel) 
	{
		super(aChannel, bChannel, false, EncodingType.k4X);
	}

	public void Start() 
	{
		super.reset();
		super.setMinRate(10);
	}

	public double GetRate() 
	{
		double rate = super.getRate();

		if (super.getStopped() || Double.isNaN(rate)) //catches NaN
			return 0.0;

		return rate;
	}

	public int Get() 
	{
		return (int) (super.get() * trim);
	}

}
