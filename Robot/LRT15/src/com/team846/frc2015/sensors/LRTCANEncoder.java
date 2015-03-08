package com.team846.frc2015.sensors;

import org.apache.commons.lang.NullArgumentException;

import com.team846.frc2015.config.Configurable;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.StatusFrameRate;

/**
 * Implementation of a quadrature encoder attached to a Talon SRX
 */
public class LRTCANEncoder
{
	private CANTalon attachedEncoder = null;
	private int zeroCount; //talon doesnt have zero method, have to keep track when reset
	private double minRate = 10; // units/period of time
	private double prevRate = 0;
	
	public LRTCANEncoder(CANTalon talon)
	{
		this(talon, 20);
	}
	
	public LRTCANEncoder(CANTalon talon, int updatePeriod) //ms
	{
		if(talon == null)
			throw new IllegalArgumentException("[ERROR] Talon must be already constructed");
		attachedEncoder  = talon;
		attachedEncoder.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		attachedEncoder.setStatusFrameRateMs(StatusFrameRate.QuadEncoder, updatePeriod);
		reset();
	}
	
	public void reset() 
	{
		zeroCount = attachedEncoder.getEncPosition();
	}
	
	/** Returns rate from attached encoder. If it is less than the minRate, returns 0.0
	 * 
	 * @return Current velocity of encoder
	 */
	public double getRate() 
	{
		double encVel = attachedEncoder.getEncVelocity();
		if( encVel < minRate || encVel == prevRate)
		{
			prevRate = encVel;
			return 0.0;
		}
		prevRate = encVel;
		
		return encVel;
	}

	/**
	 * Gets the currente encoder ticks from the attached encoder
	 * @return current encoder ticks
	 */
	public int get() 
	{
		return attachedEncoder.getEncPosition() - zeroCount;
	}
}
