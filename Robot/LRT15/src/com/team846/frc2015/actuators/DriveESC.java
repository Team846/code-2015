package com.team846.frc2015.actuators;

import com.team846.frc2015.utils.MathUtils;

import edu.wpi.first.wpilibj.CANTalon;

public class DriveESC
{
	private class BrakeAndDutyCycle
	{
		public double dutyCycle;
		public double braking;

		public BrakeAndDutyCycle()
		{
			dutyCycle = 0;
			braking = 0;
		}
	}

	CANTalon controller1;
	
	double dutyCycle;
	
	int cycle_count;
	
	double forwardCurrentLimit; // % stall current for acceleration
	double reverseCurrentLimit; // % stall current for reversing direction

	int shouldBrakeThisCycle;
	
//	DriveESC(LRTSpeedController esc, LRTEncoder encoder, String name) 
//		//Loggable(name),
//	{
//		controller1 = esc;
//		controller1.ConfigNeutralMode(LRTSpeedController.NeutralMode.kNeutralMode_Coast);
//		
//		dutyCycle = 0.0;
//		cycle_count = 0;
//		shouldBrakeThisCycle = 0;
//		
//		forwardCurrentLimit = 50.0 / 100.0;
//		reverseCurrentLimit = 50.0 / 100.0;
//	}
	
	public DriveESC(CANTalon esc1) 
	{
		controller1 = esc1;
		
		controller1.enableBrakeMode(false);

		dutyCycle = 0.0;
		cycle_count = 0;
		shouldBrakeThisCycle = 0;
		
		forwardCurrentLimit = 50.0 / 100.0;
		reverseCurrentLimit = 50.0 / 100.0;
	}
	
	BrakeAndDutyCycle CalculateBrakeAndDutyCycle(double dutyCycle, double speed)
	{
		BrakeAndDutyCycle command = new BrakeAndDutyCycle();

		command.dutyCycle = 0.0;

		if (speed < 0)
		{
			command = CalculateBrakeAndDutyCycle(-dutyCycle, -speed);
			command.dutyCycle = -command.dutyCycle;
			return command;
		}

		// Speed >= 0 at this point
		if (dutyCycle >= speed) // Going faster
		{
			command.dutyCycle = dutyCycle;
			command.braking = 0.0;
		}
		else // Slowing down
		{
			double error = dutyCycle - speed; // error always <= 0

			if (dutyCycle >= 0) // Braking is based on speed alone; reverse power unnecessary
			{
				command.dutyCycle = 0.0; // Must set 0 to brake

				if (speed > -error + 0.05)
					command.braking = -error / speed; // Speed always > 0
				else
					command.braking = 1.0;
			}
			else // Input < 0, braking with reverse power
			{
				command.braking = 0.0; // not braking
				command.dutyCycle = error / (1.0 + speed); // DutyCycle <= 0 because error <= 0
			}
		}

		return command;
	}

	public void SetDutyCycle(double dutyCycle)
	{
		//TODO: evaluate need for dithered braking/current limiting w/ mecanum
//		double speed = encoder.GetRate()
//					/ DriveEncoders.GetMaxEncoderRate();
//		
//		speed = MathUtils.clamp(speed, -1.0, 1.0);
//		
//		dutyCycle = DitheredBraking(dutyCycle, speed);
//		dutyCycle = CurrentLimit(dutyCycle, speed);
//		this.dutyCycle = dutyCycle;
		
		controller1.set(dutyCycle);
	}

	double DitheredBraking(double dutyCycle, double speed)
	{
		dutyCycle = MathUtils.clamp(dutyCycle, -1.0, 1.0);
		BrakeAndDutyCycle command = CalculateBrakeAndDutyCycle(dutyCycle, speed);
		command.dutyCycle = MathUtils.clamp(command.dutyCycle, -1.0, 1.0);

		// Default to coast
		controller1.enableBrakeMode(false);
		
		if (Math.abs(command.dutyCycle) < 1E-4) //brake only when duty cycle = 0
		{
			dutyCycle = 0.0;

			// cycle count ranges from 0 to 8
			if (++cycle_count >= 8)
				cycle_count = 0;

			/*
			 * Each integer, corresponding to value, is a bitfield of 8 cycles
			 * Pattern N has N bits out of 8 set to true.
			 * 0: 0000 0000 = 0x00
			 * 1: 0000 0001 = 0x01
			 * 2: 0001 0001 = 0x11
			 * 3: 0010 0101 = 0x25
			 * 4: 0101 0101 = 0x55
			 * 5: 1101 0101 = 0xD5
			 * 6: 1110 1110 = 0xEE
			 * 7: 1111 1110 = 0xFE
			 * 8: 1111 1111 = 0xFF
			 */
			int ditherPattern[] =
			{ 0x00, 0x01, 0x11, 0x25, 0x55, 0xD5, 0xEE, 0xFE, 0xFF };

			int brake_level = (int) (Math.abs(command.braking) * 8);
			shouldBrakeThisCycle = ditherPattern[brake_level] & (1 << cycle_count);

			if (shouldBrakeThisCycle == 1)
			{
				controller1.enableBrakeMode(false);
			}
		}

		return command.dutyCycle;
	}

	public double CurrentLimit(double dutyCycle, double speed)
	{
		if (speed < 0)
		{
			return -CurrentLimit(-dutyCycle, -speed);
		}
		// At this point speed >= 0
		if (dutyCycle > speed) // Current limit accelerating
		{
			dutyCycle = Math.min(dutyCycle, speed + forwardCurrentLimit);
		}
		else if (dutyCycle < 0) // Current limit reversing direction
		{
			double limitedDutyCycle = -reverseCurrentLimit / (1.0 + speed); // speed >= 0 so dutyCycle < -currentLimit
			dutyCycle = Math.max(dutyCycle, limitedDutyCycle); // Both are negative
		}
		
		return dutyCycle;
	}

	public void SetForwardCurrentLimit(float limit)
	{
		forwardCurrentLimit = limit;
	}

	public void SetReverseCurrentLimit(float limit)
	{
		reverseCurrentLimit = limit;
	}

	public void Disable()
	{
		controller1.set(0.0);
	}

	void ResetCache()
	{
//		if(dynamic_cast<AsyncCANJaguar*>(controller1) )
//			dynamic_cast<AsyncCANJaguar*>(controller1).ResetCache();
//		if(dynamic_cast<AsyncCANJaguar*>(controller2))
//			dynamic_cast<AsyncCANJaguar*>(controller2).ResetCache();
	}

//	void Log()
//	{
//		LogToFile(&dutyCycle, "DutyCycle");
//		LogToFile(&shouldBrakeThisCycle, "Brake");
//	}

}
