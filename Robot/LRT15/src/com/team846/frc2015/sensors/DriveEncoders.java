package com.team846.frc2015.sensors;

import com.team846.frc2015.config.Configurable;
import com.team846.frc2015.config.RobotConfig;

import edu.wpi.first.wpilibj.CANTalon;

public class DriveEncoders implements Configurable
{
	public enum Side 
	{
		FRONT_LEFT,
		FRONT_RIGHT,
		BACK_LEFT,
		BACK_RIGHT
	}
	
	private static DriveEncoders instance = null;

	CANTalon[] encoders;
	
	int[] initialValues;

	static double PULSES_PER_REVOLUTION; // Encoder pulses per wheel revolution
	static double MAX_ENCODER_RATE; //TODO: Calibrate after running robot
	static double MAX_TURNING_RATE;  //TODO: Calibrate after running robot
	static double TICKS_PER_FULL_TURN;
	static double WHEEL_DIAMETER; // //TODO: Calibrate after running robot
	static double GEAR_RATIO;  //TODO: Talk to drivetrain team
	static double MAX_STRAFING_SPEED;
	
	public DriveEncoders(CANTalon frontLeft, CANTalon frontRight, CANTalon backLeft, CANTalon backRight)
	{
		encoders = new CANTalon[4];
		initialValues = new int[4];
		
		encoders[Side.FRONT_LEFT.ordinal()] = frontLeft;
		encoders[Side.FRONT_RIGHT.ordinal()] = frontRight;
		encoders[Side.BACK_LEFT.ordinal()] = backLeft;
		encoders[Side.BACK_RIGHT.ordinal()] = backRight;
		
		initialValues[Side.FRONT_LEFT.ordinal()] = frontLeft.getEncPosition();
		initialValues[Side.FRONT_RIGHT.ordinal()] = frontRight.getEncPosition();
		initialValues[Side.BACK_LEFT.ordinal()] = backLeft.getEncPosition();
		initialValues[Side.BACK_RIGHT.ordinal()] = backRight.getEncPosition();
		
		if (instance == null)
			instance = this;
	}


	public static DriveEncoders Get()
	{
		return instance;
	}

	public double GetRawForwardSpeed()
	{
		double leftSpeed = (encoders[Side.FRONT_LEFT.ordinal()].getEncVelocity()
				+ encoders[Side.BACK_LEFT.ordinal()].getEncVelocity()) / 2;
		
		double rightSpeed =  (encoders[Side.FRONT_RIGHT.ordinal()].getEncVelocity()
				+ encoders[Side.BACK_RIGHT.ordinal()].getEncVelocity()) / 2;
		
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

	public double GetRawTurningSpeed()
	{
		int rightVel = (encoders[Side.FRONT_RIGHT.ordinal()].getEncVelocity() + encoders[Side.BACK_RIGHT.ordinal()].getEncVelocity()) /2;
		int leftVel =  (encoders[Side.FRONT_LEFT.ordinal()].getEncVelocity() + encoders[Side.BACK_LEFT.ordinal()].getEncVelocity()) /2;
		return rightVel - leftVel;
	}
	
	public double GetRawStrafingSpeed()
	{
		return (encoders[Side.FRONT_LEFT.ordinal()].getEncVelocity() + encoders[Side.BACK_RIGHT.ordinal()].getEncVelocity()) /2;
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
		double frontDist = (GetWheelDist(Side.FRONT_RIGHT) + GetWheelDist(Side.BACK_RIGHT)) / 2.0;
		double backDist =  (GetWheelDist(Side.FRONT_LEFT) + GetWheelDist(Side.BACK_LEFT)) / 2.0;
		
		return frontDist + backDist / 2;
	}

	public int GetTurnTicks()
	{
		int rightTicks = (encoders[Side.FRONT_RIGHT.ordinal()].getEncPosition() + encoders[Side.BACK_RIGHT.ordinal()].getEncPosition()) /2;
		int leftTicks =  (encoders[Side.FRONT_LEFT.ordinal()].getEncPosition() + encoders[Side.BACK_LEFT.ordinal()].getEncPosition()) /2;
		return rightTicks - leftTicks; 
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
		CANTalon e = encoders[side.ordinal()];
		double dist = (double) ((e.getEncPosition() * 1.0) / PULSES_PER_REVOLUTION
				* GEAR_RATIO * WHEEL_DIAMETER * Math.PI); // pulses / ( pulses / encoder revolution ) * encoder to wheel gear ratio * distance / wheel revolution = inch distance
		return dist;
	}

	public double GetNormalizedSpeed(Side side)
	{
		return encoders[side.ordinal()].getEncVelocity() / MAX_ENCODER_RATE;
	}

	public CANTalon GetEncoder(Side side)
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
		if (Math.abs((GetNormalizedSpeed(Side.BACK_LEFT) +GetNormalizedSpeed(Side.FRONT_LEFT)) / 2) > 
			Math.abs( (GetNormalizedSpeed(Side.BACK_RIGHT) +GetNormalizedSpeed(Side.FRONT_RIGHT)) / 2))
		{
			faster = (GetNormalizedSpeed(Side.BACK_RIGHT) +GetNormalizedSpeed(Side.FRONT_RIGHT)) / 2;
			slower = (GetNormalizedSpeed(Side.BACK_LEFT) +GetNormalizedSpeed(Side.FRONT_LEFT)) / 2;
		}
		else
		{
			faster = (GetNormalizedSpeed(Side.BACK_RIGHT) +GetNormalizedSpeed(Side.FRONT_RIGHT)) / 2;
			slower = (GetNormalizedSpeed(Side.BACK_LEFT) +GetNormalizedSpeed(Side.FRONT_LEFT)) / 2;
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


