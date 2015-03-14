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
	private boolean reverse;
	
	public boolean isReverse() {
		return reverse;
	}

	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}

	public LRTCANEncoder(CANTalon talon)
	{
		this(talon, 20, false);
	}
	
	public LRTCANEncoder(CANTalon talon, boolean reverse)
	{
		this(talon, 20, reverse);
	}
	
	public LRTCANEncoder(CANTalon talon, int updatePeriod, boolean reverse) //ms
	{
		if(talon == null)
			throw new IllegalArgumentException("[ERROR] Talon must be already constructed");
		this.reverse = reverse;
		attachedEncoder = talon;
		attachedEncoder.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		attachedEncoder.setStatusFrameRateMs(StatusFrameRate.QuadEncoder, updatePeriod);
		reset();
	}
	
	public CANTalon getTalon()
	{
		return attachedEncoder;
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
		if( Math.abs(encVel) < minRate || encVel == prevRate)
		{
			prevRate = encVel;
			return 0.0;
		}
		prevRate = encVel;
		
		if(reverse)
			encVel = -encVel;
		return encVel;
	}

	/**
	 * Gets the current encoder ticks from the attached encoder
	 * @return current encoder ticks
	 */
	public int get() 
	{
		int count = attachedEncoder.getEncPosition() - zeroCount;
		if(reverse)
			count = -count;
		return count;
	}
}
