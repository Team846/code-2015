package com.team846.frc2015.sensors;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.StatusFrameRate;

/**
 * Implementation of a quadrature encoder attached to a Talon SRX
 */
public class LRTCANEncoder
{
	private CANTalon attachedEncoder = null;
	private final double minRate = 10; // units/period of time
	private double prevRate = 0;
	private boolean reverse;

	/** Returns a state indicating whether the CANEncoder
	 * is reversing encoder output
	 * @return is reversing sensor output
	 */
	public boolean isReverse() {
		return reverse;
	}

	/** Sets a flag to reverse encoder output
	 * @param reverse flip sign of encoder output
	 */
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

	private LRTCANEncoder(CANTalon talon, int updatePeriod, boolean reverse) //ms
	{
		if(talon == null)
			throw new IllegalArgumentException("[ERROR] Talon must be already constructed");
		this.reverse = reverse;
		attachedEncoder = talon;
		attachedEncoder.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		attachedEncoder.setStatusFrameRateMs(StatusFrameRate.QuadEncoder, updatePeriod);
	}

	/**  Returns a reference to a Talon SRX
	 * @return reference to attached Talon SRX
	 */
	public CANTalon getTalon()
	{
		return attachedEncoder;
	}

	/** Returns rate from attached encoder. If it is less than the minRate, returns 0.0
	 *
	 * @return Current velocity of encoder
	 */
	public double getRate()
	{
		double encVel = attachedEncoder.getSpeed();

//		double encVel = attachedEncoder.getEncVelocity();

//		if( Math.abs(encVel) < minRate || encVel == prevRate) //if exactly the same
//		{
//			prevRate = encVel;
//			return 0.0;
//		}
//		prevRate = encVel;

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
		int count = attachedEncoder.getEncPosition();
		return reverse ? -count: count;
	}
}
