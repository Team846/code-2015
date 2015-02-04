package com.lynbrookrobotics.frc2015.sensors;

import com.lynbrookrobotics.frc2015.config.Configurable;
import com.lynbrookrobotics.frc2015.config.RobotConfig;

public class DriveEncoders implements Configurable
{
	public enum Side 
	{
		LEFT,
		RIGHT
	}
	
	static DriveEncoders instance = null;

	LRTEncoder[] encoders;

	static double PULSES_PER_REVOLUTION; // Encoder pulses per wheel revolution
	static double MAX_ENCODER_RATE;
	static double MAX_TURNING_RATE;
	static double TICKS_PER_FULL_TURN;
	static double WHEEL_DIAMETER; // Inches
	static double GEAR_RATIO;
	
	DriveEncoders(int leftSourceA, int leftSourceB, int rightSourceA, int rightSourceB)
	{
		encoders = new LRTEncoder[2];
		encoders[Side.LEFT.ordinal()] = SensorFactory.GetLRTEncoder("Side.LEFTDriveEncoder", leftSourceA, leftSourceB);
		encoders[Side.RIGHT.ordinal()] = SensorFactory.GetLRTEncoder("Side.RIGHTDriveEncoder", rightSourceA, rightSourceB);

		encoders[Side.LEFT.ordinal()].setDistancePerPulse(1);
		encoders[Side.RIGHT.ordinal()].setDistancePerPulse(1);
		
		if (instance == null)
			instance = this;
	}


	public DriveEncoders Get()
	{
		return instance;
	}

	public double GetRawForwardSpeed()
	{
		return (encoders[Side.LEFT.ordinal()].GetRate() + encoders[Side.RIGHT.ordinal()].GetRate()) / 2;
	}

	public double GetNormalizedForwardSpeed()
	{
		return GetRawForwardSpeed() / MAX_ENCODER_RATE;
	}

	public double GetRobotForwardSpeed()
	{
		return GetRawForwardSpeed() / PULSES_PER_REVOLUTION * GEAR_RATIO * WHEEL_DIAMETER * Math.PI;
	}

	public double GetRawTurningSpeed()
	{
		return encoders[Side.RIGHT.ordinal()].GetRate() - encoders[Side.LEFT.ordinal()].GetRate();
	}

	public double GetNormalizedTurningSpeed()
	{
		return GetRawTurningSpeed() / MAX_TURNING_RATE;
	}

	public double GetRobotDist()
	{
		return (GetWheelDist(Side.LEFT) + GetWheelDist(Side.RIGHT)) / 2.0;
	}

	public int GetTurnTicks()
	{
		return encoders[Side.RIGHT.ordinal()].Get() - encoders[Side.LEFT.ordinal()].Get();
	}

	public double GetTurnRevolutions()
	{
		return GetTurnTicks() / TICKS_PER_FULL_TURN;
	}

	public double GetTurnAngle()
	{
		return GetTurnRevolutions() * 360.0;
	}

	public double GetWheelDist(Side side)
	{
		LRTEncoder e = encoders[side.ordinal()];
		double dist = (double) ((e.Get() * 1.0) / PULSES_PER_REVOLUTION
				* GEAR_RATIO * WHEEL_DIAMETER * Math.PI); // pulses / ( pulses / encoder revolution ) * encoder to wheel gear ratio * distance / wheel revolution = inch distance
		return dist;
	}

	public double GetNormalizedSpeed(Side side)
	{
		return encoders[side.ordinal()].GetRate() / MAX_ENCODER_RATE;
	}

	public LRTEncoder GetEncoder(Side side)
	{
		return encoders[side.ordinal()];
	}

	public static double GetMaxEncoderRate()
	{
		return MAX_ENCODER_RATE;
	}

	public double GetMaxSpeed()
	{
		return MAX_ENCODER_RATE / PULSES_PER_REVOLUTION * GEAR_RATIO * WHEEL_DIAMETER * Math.PI;
	}

	public double GetMaxTurnRate()
	{
		return MAX_TURNING_RATE / TICKS_PER_FULL_TURN * 360.0;
	}

	public double GetTurnRadius()
	{
		double faster;
		double slower;
		if (Math.abs(GetNormalizedSpeed(Side.LEFT)) > Math.abs(GetNormalizedSpeed(Side.RIGHT)))
		{
			faster = GetNormalizedSpeed(Side.LEFT);
			slower = GetNormalizedSpeed(Side.RIGHT);
		}
		else
		{
			faster = GetNormalizedSpeed(Side.RIGHT);
			slower = GetNormalizedSpeed(Side.LEFT);
		}
		if (faster == 0 && slower == 0)
			return 0;
		return RobotConfig.ROBOT_WIDTH / (1 - slower / faster) - RobotConfig.ROBOT_WIDTH / 2; // Radius to center of robot -RC
	}

	public void Configure()
	{
//		PULSES_PER_REVOLUTION = GetConfig("pulses_per_revolution", 250.0);
//		MAX_ENCODER_RATE = GetConfig("max_encoder_rate", 2214.762);
//		MAX_TURNING_RATE = GetConfig("max_turning_rate", 3782);
//		WHEEL_DIAMETER = GetConfig("wheel_diameter", 4.0); // Inches
//		GEAR_RATIO = GetConfig("gear_ratio", 1 .0);
//		TICKS_PER_FULL_TURN = GetConfig("ticks_per_full_turn", 2.0 * 26.574 * PI / (GEAR_RATIO * WHEEL_DIAMETER * PI) * PULSES_PER_REVOLUTION);
	}

	public void Log()
	{
//		LogToFile(GetNormalizedForwardSpeed(), "NormalizedDriveSpeed");
//		LogToFile(GetNormalizedTurningSpeed(), "NormalizedTurningSpeed");
//		LogToFile(GetRobotDist(), "DriveDistance");
//		LogToFile(GetTurnAngle(), "TurnAngle");
//		LogToFile(GetTurnTicks(), "TurnTicks");
//		LogToFile(GetNormalizedSpeed(Side.LEFT), "NormalizedSide.LEFTSpeed");
//		LogToFile(GetNormalizedSpeed(Side.RIGHT), "NormalizedSide.RIGHTSpeed");
	}
}


