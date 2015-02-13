package com.lynbrookrobotics.frc2015.components;

import com.lynbrookrobotics.frc2015.actuators.DriveESC;
import com.lynbrookrobotics.frc2015.componentData.DrivetrainData;
import com.lynbrookrobotics.frc2015.componentData.DrivetrainData.Axis;
import com.lynbrookrobotics.frc2015.config.ConfigPortMappings;
import com.lynbrookrobotics.frc2015.config.Configurable;
import com.lynbrookrobotics.frc2015.config.DriverStationConfig;
import com.lynbrookrobotics.frc2015.config.RobotConfig;
import com.lynbrookrobotics.frc2015.control.PID;
import com.lynbrookrobotics.frc2015.driverstation.LRTDriverStation;
import com.lynbrookrobotics.frc2015.driverstation.LRTJoystick;
import com.lynbrookrobotics.frc2015.log.AsyncPrinter;
import com.lynbrookrobotics.frc2015.sensors.DriveEncoders;
import com.lynbrookrobotics.frc2015.utils.MathUtils;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;

public class Drivetrain extends Component implements Configurable {
	
	public enum Side
	{
		FRONT_LEFT,
		FRONT_RIGHT,
		BACK_LEFT,
		BACK_RIGHT
	}
	
	private final static int POSITION = 0;
	private final static int VELOCITY = 1; 
	
	PID PIDs[][];
	DrivetrainData drivetrainData;
	DriveEncoders driveEncoders;
	DriveESC[] escs;
	
	CANTalon frontLeft;
	CANTalon backLeft;
	CANTalon frontRight;
	CANTalon backRight;
	
	
	public Drivetrain() 
	{	
		super("Drivetrain", DriverStationConfig.DigitalIns.NO_DS_DI);
		PIDs = new PID[2][4];
		
		ConfigPortMappings portMapping = ConfigPortMappings.Instance();
		
		frontLeft = new CANTalon(portMapping.get("CAN/DRIVE_FRONT_LEFT"));
		backLeft = new CANTalon(portMapping.get("CAN/DRIVE_BACK_LEFT"));
		
		frontRight = new CANTalon(portMapping.get("CAN/DRIVE_FRONT_RIGHT"));
		backRight = new CANTalon(portMapping.get("CAN/DRIVE_BACK_RIGHT"));
		
		driveEncoders = new DriveEncoders(frontLeft, frontRight, backLeft, backRight);
		
		escs[Side.FRONT_LEFT.ordinal()] = new DriveESC(frontLeft);
		escs[Side.FRONT_RIGHT.ordinal()] = new DriveESC(frontRight);
		escs[Side.BACK_LEFT.ordinal()] = new DriveESC(frontLeft);
		escs[Side.BACK_RIGHT.ordinal()] = new DriveESC(frontRight);
		
		drivetrainData = DrivetrainData.Get();
	}

	private double ComputeOutput(DrivetrainData.Axis axis)
	{
		double positionSetpoint = drivetrainData.GetPositionSetpoint(axis);
		double velocitySetpoint = drivetrainData.GetVelocitySetpoint(axis);
		double rawOutput = drivetrainData.GetOpenLoopOutput(axis);

		switch (drivetrainData.GetControlMode(axis))
		{
		case POSITION_CONTROL:
			PIDs[POSITION][axis.ordinal()].SetInput(axis == Axis.FORWARD ? driveEncoders.GetRobotDist() : driveEncoders.GetTurnAngle());
			PIDs[POSITION][axis.ordinal()].SetSetpoint(positionSetpoint);
			velocitySetpoint += PIDs[POSITION][axis.ordinal()].Update(1.0 / RobotConfig.LOOP_RATE);
			if (Math.abs(velocitySetpoint) > drivetrainData.GetPositionControlMaxSpeed(axis))
				velocitySetpoint = MathUtils.Sign(velocitySetpoint) * drivetrainData.GetPositionControlMaxSpeed(axis);
		case VELOCITY_CONTROL:
			if (Math.abs(velocitySetpoint) < 2.0E-2)
				PIDs[VELOCITY][axis.ordinal()].SetIIREnabled(true);
			else
				PIDs[VELOCITY][axis.ordinal()].SetIIREnabled(false);

			if (axis == Axis.FORWARD)
				PIDs[VELOCITY][axis.ordinal()].SetInput(
						driveEncoders.GetNormalizedForwardSpeed());
			else
				PIDs[VELOCITY][axis.ordinal()].SetInput(
						driveEncoders.GetNormalizedTurningSpeed());

			PIDs[VELOCITY][axis.ordinal()].SetSetpoint(velocitySetpoint);

			rawOutput = PIDs[VELOCITY][axis.ordinal()].Update(1.0 / RobotConfig.LOOP_RATE);
			break;
		case OPEN_LOOP:
			break;
		}
		return rawOutput;
	}

	public void UpdateEnabled()
	{
		double fwdOutput = ComputeOutput(Axis.FORWARD); //positive means forward
		double turnOutput = -ComputeOutput(Axis.TURN); //positive means turning counter-clockwise. Matches the way driveEncoders work.
		double strafeOutput = ComputeOutput(Axis.STRAFE); //positive is to the right
		
		double leftFrontOutput = fwdOutput + turnOutput + strafeOutput;
		double rightFrontOutput = fwdOutput - turnOutput - strafeOutput;
		
		double leftBackOutput = fwdOutput + turnOutput - strafeOutput;
		double rightBackOutput = fwdOutput - turnOutput + strafeOutput;

		leftFrontOutput = MathUtils.clamp(leftFrontOutput, -1.0, 1.0);
		rightFrontOutput = MathUtils.clamp(rightFrontOutput, -1.0, 1.0);
		leftBackOutput = MathUtils.clamp(leftBackOutput, -1.0, 1.0);
		rightBackOutput = MathUtils.clamp(rightBackOutput, -1.0, 1.0);

//		if (drivetrainData.ShouldOverrideForwardCurrentLimit())
//		{
//			escs[LEFT].SetForwardCurrentLimit(drivetrainData.GetForwardCurrentLimit());
//			escs[RIGHT].SetForwardCurrentLimit(drivetrainData.GetForwardCurrentLimit());
//		}
//		else
//		{
//			ConfigureForwardCurrentLimit();
//		}
//		if (drivetrainData.ShouldOverrideReverseCurrentLimit())
//		{
//			escs[LEFT].SetReverseCurrentLimit(drivetrainData.GetReverseCurrentLimit());
//			escs[RIGHT].SetReverseCurrentLimit(drivetrainData.GetReverseCurrentLimit());
//		}
//		else
//		{
//			ConfigureReverseCurrentLimit();
//		}
		
		escs[Side.FRONT_LEFT.ordinal()].SetDutyCycle(leftFrontOutput);
		escs[Side.FRONT_RIGHT.ordinal()].SetDutyCycle(rightFrontOutput);
		
		escs[Side.BACK_LEFT.ordinal()].SetDutyCycle(leftBackOutput);
		escs[Side.BACK_RIGHT.ordinal()].SetDutyCycle(rightBackOutput);
	}

	public void UpdateDisabled()
	{
		escs[Side.FRONT_LEFT.ordinal()].SetDutyCycle(0.0);
		escs[Side.BACK_LEFT.ordinal()].SetDutyCycle(0.0);
		
		escs[Side.FRONT_RIGHT.ordinal()].SetDutyCycle(0.0);
		escs[Side.BACK_RIGHT.ordinal()].SetDutyCycle(0.0);
		
		//coast when disabled
		frontLeft.enableBrakeMode(false);
		backLeft.enableBrakeMode(false);
		frontRight.enableBrakeMode(false);
		backRight.enableBrakeMode(false);
	}

	public void OnEnabled()
	{
	}

	public void OnDisabled()
	{
		escs[Side.FRONT_LEFT.ordinal()].SetDutyCycle(0.0);
		escs[Side.FRONT_RIGHT.ordinal()].SetDutyCycle(0.0);
		
		escs[Side.FRONT_RIGHT.ordinal()].SetDutyCycle(0.0);
		escs[Side.BACK_RIGHT.ordinal()].SetDutyCycle(0.0);
	}

	public void Configure()
	{
//		Kv = GetConfig("Kv", 1.0);
		
		ConfigurePIDObject(PIDs[VELOCITY][Axis.TURN.ordinal()], "velocity_turn", true);
		ConfigurePIDObject(PIDs[VELOCITY][Axis.FORWARD.ordinal()], "velocity_fwd", true);

		ConfigurePIDObject(PIDs[POSITION][Axis.TURN.ordinal()], "position_turn", false);
		ConfigurePIDObject(PIDs[POSITION][Axis.FORWARD.ordinal()], "position_fwd", false);

//		ConfigureForwardCurrentLimit();
//		ConfigureReverseCurrentLimit();
	}

	void ConfigurePIDObject(PID pid, String objName, boolean feedForward)
	{
		double p = GetConfig(objName + "_P", 2.0);
		double i = GetConfig(objName + "_I", 0.0);
		double d = GetConfig(objName + "_D", 0.0);

		pid.SetParameters(p, i, d, 1.0, 0.87, feedForward,0.0);
	}

//	void ConfigureForwardCurrentLimit()
//	{
//		escs[LEFT].SetForwardCurrentLimit(GetConfig("forwardCurrentLimit", 50.0 / 100.0));
//		escs[RIGHT].SetForwardCurrentLimit(GetConfig("forwardCurrentLimit", 50.0 / 100.0));
//	}
//
//	void ConfigureReverseCurrentLimit()
//	{
//		escs[LEFT].SetReverseCurrentLimit(GetConfig("reverseCurrentLimit", 50.0 / 100.0));
//		escs[RIGHT].SetReverseCurrentLimit(GetConfig("reverseCurrentLimit", 50.0 / 100.0));
//	}
}