package com.lynbrookrobotics.frc2015.sensors;

import org.apache.commons.lang.NullArgumentException;

import com.lynbrookrobotics.frc2015.config.Configurable;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.StatusFrameRate;

/*!
 * Implementation of a quadrature encoder attached to a Talon SRX
 */
public class LRTCANEncoder {
	
	private CANTalon attachedEncoder = null;
	private int zeroCount; //talon doesnt have zero method, have to keep track when reset
	private double minRate = 10; // units/period of time
	
	public LRTCANEncoder(CANTalon talon)
	{
		if(talon == null)
			throw new IllegalArgumentException("[ERROR] Talon must be already constructed");
		this.attachedEncoder  = talon;
	}
	
	public LRTCANEncoder(CANTalon talon, int updatePeriod) //ms
	{
		this.attachedEncoder = talon;
		attachedEncoder.setStatusFrameRateMs(StatusFrameRate.QuadEncoder, updatePeriod);
	}
	
	public void Reset() 
	{
		zeroCount = attachedEncoder.getEncPosition();
	}
	
	/** Returns rate from attached encoder. If it is less than the minRate, returns 0.0
	 * 
	 * @return Current velocity of encoder
	 */
	public double GetRate() 
	{
		double encVel = attachedEncoder.getEncVelocity();
		if( encVel < minRate)
			return 0.0;
		return encVel;
	}

	/**
	 * Gets the currente encoder ticks from the attached encoder
	 * @return current encoder ticks
	 */
	public int Get() 
	{
		return attachedEncoder.getEncPosition() - zeroCount;
	}
}
