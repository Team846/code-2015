package com.lynbrookrobotics.frc2015.sensors;

import com.lynbrookrobotics.frc2015.config.Configurable;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.StatusFrameRate;

/*!
 * Implementation of a sensor attached to a Talon SRX
 */
public class LRTCANEncoder {
	
	private CANTalon attachedEncoder = null;
	
	public LRTCANEncoder(CANTalon talon)
	{
		this.attachedEncoder  = talon;
		
	}
	
	/*!
	 * Saves a reference to a constructed CANTalon,and changes the update rate
	 */
	public LRTCANEncoder(CANTalon talon, int updatePeriod) //ms
	{
		this.attachedEncoder = talon;
		attachedEncoder.setStatusFrameRateMs(StatusFrameRate.QuadEncoder, updatePeriod);
	}
}
