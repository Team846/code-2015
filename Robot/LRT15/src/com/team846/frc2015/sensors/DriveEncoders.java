package com.team846.frc2015.sensors;

import com.team846.frc2015.config.ConfigRuntime;
import com.team846.frc2015.config.Configurable;
import com.team846.frc2015.config.RobotConfig;

import edu.wpi.first.wpilibj.CANTalon;

public class DriveEncoders implements Configurable
{
	public enum Side 
	{
		LEFT_FRONT,
		RIGHT_FRONT,
		LEFT_BACK,
		RIGHT_BACK
	}
	
	private static DriveEncoders instance = null;

	private final LRTCANEncoder[] encoders;

	private static double PULSES_PER_REVOLUTION; // Encoder pulses per wheel revolution
	private static double MAX_ENCODER_RATE; //TODO: Calibrate after running robot
	private static double MAX_TURNING_RATE;  //TODO: Calibrate after running robot
	private static double TICKS_PER_FULL_TURN;
	private static double WHEEL_DIAMETER; // //TODO: Calibrate after running robot
	private static double GEAR_RATIO;  //TODO: Talk to drivetrain team
	private static double MAX_STRAFING_SPEED;
	
	public DriveEncoders(CANTalon frontLeft, CANTalon frontRight, CANTalon backLeft, CANTalon backRight)
	{
		encoders = new LRTCANEncoder[4];
		
		encoders[Side.LEFT_FRONT.ordinal()] = new LRTCANEncoder(frontLeft, true);
		encoders[Side.RIGHT_FRONT.ordinal()] = new LRTCANEncoder(frontRight, false);
		encoders[Side.LEFT_BACK.ordinal()] = new LRTCANEncoder(backLeft, true);
		encoders[Side.RIGHT_BACK.ordinal()] = new LRTCANEncoder(backRight, false);
		
		//TODO: reverse not working, check later
//		//NOTE: Reverse left encoders
//		encoders[Side.LEFT_BACK.ordinal()].getTalon().reverseSensor(true);// = new LRTCANEncoder(backLeft);
//		encoders[Side.LEFT_FRONT.ordinal()].getTalon().reverseSensor(true);// = new LRTCANEncoder(backLeft);

		
		if (instance == null)
			instance = this;
		ConfigRuntime.Register(this);
	}


	public static DriveEncoders Get()
	{
		return instance;
	}

	double GetRawForwardSpeed()
	{
		double leftSpeed = (encoders[Side.LEFT_FRONT.ordinal()].getRate()
				+ encoders[Side.LEFT_BACK.ordinal()].getRate()) / 2;
		
		double rightSpeed =  (encoders[Side.RIGHT_FRONT.ordinal()].getRate()
				+ encoders[Side.RIGHT_BACK.ordinal()].getRate()) / 2;
		
		return (leftSpeed + rightSpeed) / 2;
	}

	public double GetNormalizedForwardSpeed()
	{
		return GetRawForwardSpeed() / MAX_ENCODER_RATE;
	}

	public double GetRobotForwardSpeed()
	{
		return GetRawForwardSpeed() / PULSES_PER_REVOLUTION * GEAR_RATIO * WHEEL_DIAMETER * Math.PI;
	}

	double GetRawTurningSpeed()
	{
		double rightVel = (encoders[Side.RIGHT_FRONT.ordinal()].getRate() + encoders[Side.RIGHT_BACK.ordinal()].getRate()) /2;
		double leftVel =  (encoders[Side.LEFT_FRONT.ordinal()].getRate() + encoders[Side.LEFT_BACK.ordinal()].getRate()) /2;
		return rightVel - leftVel;
	}
	
	double GetRawStrafingSpeed()
	{
		return (encoders[Side.LEFT_FRONT.ordinal()].getRate() + encoders[Side.RIGHT_BACK.ordinal()].getRate()) /2;
	}
	
	public double GetNormalizedStrafingSpeed()
	{
		return GetRawStrafingSpeed() / MAX_STRAFING_SPEED;
	}

	public double GetNormalizedTurningSpeed()
	{
		return GetRawTurningSpeed() / MAX_TURNING_RATE;
	}

	public double GetRobotDist()
	{
		double rightDist = (GetWheelDist(Side.RIGHT_FRONT) + GetWheelDist(Side.RIGHT_BACK)) / 2.0;
		double leftDist =  (GetWheelDist(Side.LEFT_FRONT) + GetWheelDist(Side.LEFT_BACK)) / 2.0;
		
		return (rightDist + leftDist) / 2;
	}

	int GetTurnTicks()
	{
		int rightTicks = (encoders[Side.RIGHT_FRONT.ordinal()].get()
				+ encoders[Side.RIGHT_BACK.ordinal()].get()) /2;
		int leftTicks =  (encoders[Side.LEFT_FRONT.ordinal()].get()
				+ encoders[Side.LEFT_BACK.ordinal()].get()) /2;
		return rightTicks - leftTicks; 
	}

	double GetTurnRevolutions()
	{
		return GetTurnTicks() / TICKS_PER_FULL_TURN;
	}
	
	public void Reset()
	{
		for(LRTCANEncoder enc : encoders)
		{
			enc.reset();
		}
	}

	public double GetTurnAngle()
	{
		return GetTurnRevolutions() * 360.0;
	}

	double GetWheelDist(Side side)
	{
		LRTCANEncoder e = encoders[side.ordinal()];
		double dist = ((e.get()) / PULSES_PER_REVOLUTION
				* GEAR_RATIO * WHEEL_DIAMETER * Math.PI); // pulses / ( pulses / encoder revolution ) * encoder to wheel gear ratio * distance / wheel revolution = inch distance
		return dist;
	}

	double GetNormalizedSpeed(Side side)
	{
		return encoders[side.ordinal()].getRate() / MAX_ENCODER_RATE;
	}

	public LRTCANEncoder GetEncoder(Side side)
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
		if (Math.abs((GetNormalizedSpeed(Side.LEFT_BACK) +GetNormalizedSpeed(Side.LEFT_FRONT)) / 2) > 
			Math.abs( (GetNormalizedSpeed(Side.RIGHT_BACK) +GetNormalizedSpeed(Side.RIGHT_FRONT)) / 2))
		{
			faster = (GetNormalizedSpeed(Side.RIGHT_BACK) +GetNormalizedSpeed(Side.RIGHT_FRONT)) / 2;
			slower = (GetNormalizedSpeed(Side.LEFT_BACK) +GetNormalizedSpeed(Side.LEFT_FRONT)) / 2;
		}
		else
		{
			faster = (GetNormalizedSpeed(Side.RIGHT_BACK) +GetNormalizedSpeed(Side.RIGHT_FRONT)) / 2;
			slower = (GetNormalizedSpeed(Side.LEFT_BACK) +GetNormalizedSpeed(Side.LEFT_FRONT)) / 2;
		}
		if (faster == 0 && slower == 0)
			return 0;
		return RobotConfig.ROBOT_WIDTH / (1 - slower / faster) - RobotConfig.ROBOT_WIDTH / 2; // Radius to center of robot -RC
	}

	public void Configure()
	{
		PULSES_PER_REVOLUTION = GetConfig("pulses_per_revolution", 50.0);
		MAX_ENCODER_RATE = GetConfig("max_encoder_rate", 2214.762);
		MAX_TURNING_RATE = GetConfig("max_turning_rate", 3782);
		MAX_STRAFING_SPEED = GetConfig("max_strafing_speed", 29); //TODO: MUST TEST
		WHEEL_DIAMETER = GetConfig("wheel_diameter", 6.0); // Inches
		GEAR_RATIO = GetConfig("gear_ratio", 4.0);
		TICKS_PER_FULL_TURN = GetConfig("ticks_per_full_turn", 2.0 * 26.574 * Math.PI / (GEAR_RATIO * WHEEL_DIAMETER * Math.PI) * PULSES_PER_REVOLUTION);
	}
}


