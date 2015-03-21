package com.team846.frc2015.components;

import java.util.Arrays;

import com.team846.frc2015.actuators.DriveESC;
import com.team846.frc2015.componentData.DrivetrainData;
import com.team846.frc2015.componentData.DrivetrainData.Axis;
import com.team846.frc2015.config.ConfigPortMappings;
import com.team846.frc2015.config.ConfigRuntime;
import com.team846.frc2015.config.Configurable;
import com.team846.frc2015.config.DriverStationConfig;
import com.team846.frc2015.config.RobotConfig;
import com.team846.frc2015.control.PID;
import com.team846.frc2015.sensors.DriveEncoders;
import com.team846.frc2015.utils.AsyncPrinter;
import com.team846.frc2015.utils.MathUtils;

import edu.wpi.first.wpilibj.CANTalon;

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
	
	private final PID[][] PIDs;
	
	private final PID[] mecanumDrivePIDs;
	private final DrivetrainData drivetrainData;
	private final DriveEncoders driveEncoders;
	private final DriveESC[] escs = new DriveESC[4];
	
	private final CANTalon frontLeft;
	private final CANTalon backLeft;
	private final CANTalon frontRight;
	private final CANTalon backRight;
	
	public Drivetrain() 
	{	
		super("Drivetrain", DriverStationConfig.DigitalIns.NO_DS_DI);
	
		PIDs = new PID[2][4];
		
		for(PID[] row : PIDs)
			Arrays.fill(row, new PID());
		
		mecanumDrivePIDs = new PID[4]; 
		Arrays.fill(mecanumDrivePIDs, new PID());
		
		ConfigPortMappings portMapping = ConfigPortMappings.Instance(); //reduce verbosity
		
		frontLeft = new CANTalon(portMapping.get("CAN/DRIVE_FRONT_LEFT"));
		backLeft = new CANTalon(portMapping.get("CAN/DRIVE_BACK_LEFT"));
		
		frontRight = new CANTalon(portMapping.get("CAN/DRIVE_FRONT_RIGHT"));
		backRight = new CANTalon(portMapping.get("CAN/DRIVE_BACK_RIGHT"));
		
		driveEncoders = new DriveEncoders(frontLeft, frontRight, backLeft, backRight);
		
		escs[Side.FRONT_LEFT.ordinal()] = new DriveESC(frontLeft);
		escs[Side.FRONT_RIGHT.ordinal()] = new DriveESC(frontRight);
		escs[Side.BACK_LEFT.ordinal()] = new DriveESC(backLeft);
		escs[Side.BACK_RIGHT.ordinal()] = new DriveESC(backRight);
		
		drivetrainData = DrivetrainData.get();
		
		ConfigRuntime.Register(this);
	}

	private double ComputeOutput(DrivetrainData.Axis axis)
	{
		double positionSetpoint = drivetrainData.GetPositionSetpoint(axis);
		System.out.println("position drivetrain setpoint:" + positionSetpoint);
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
			AsyncPrinter.warn("RAW PID OUTPUT: " + velocitySetpoint);
			rawOutput = velocitySetpoint;
			
			break;
		case VELOCITY_CONTROL:
			if (Math.abs(velocitySetpoint) < 2.0E-2)
				PIDs[VELOCITY][axis.ordinal()].SetIIREnabled(true);
			else
				PIDs[VELOCITY][axis.ordinal()].SetIIREnabled(false);

			if (axis == Axis.FORWARD)
				PIDs[VELOCITY][axis.ordinal()].SetInput(
						driveEncoders.GetNormalizedForwardSpeed());
			else if (axis == Axis.TURN)
				PIDs[VELOCITY][axis.ordinal()].SetInput(
						driveEncoders.GetNormalizedTurningSpeed());
			else
				PIDs[VELOCITY][axis.ordinal()].SetInput(
						driveEncoders.GetNormalizedStrafingSpeed());

			PIDs[VELOCITY][axis.ordinal()].SetSetpoint(velocitySetpoint);

			rawOutput = PIDs[VELOCITY][axis.ordinal()].Update(1.0 / RobotConfig.LOOP_RATE);
			break;
		case OPEN_LOOP:
			break;
		default:
			AsyncPrinter.warn("Invalid control mode for axis: "+ axis);
			break;
		}
		return rawOutput;
	}

	public void UpdateEnabled()
	{
		double leftFrontOutput;
		double rightFrontOutput;
		
		double leftBackOutput;
		double rightBackOutput;
		
//		AsyncPrinter.println("CURRENT TURN ANGLE: " +DriveEncoders.Get().GetTurnAngle() );
		
//		AsyncPrinter.println("Encoder Turn: " + driveEncoders.GetTurnTicks());
		AsyncPrinter.println("Encoder Dist: " + driveEncoders.GetRobotDist());
		
//		System.out.println("Turn input: " + drivetrainData.GetPositionSetpoint(Axis.TURN));
//		System.out.println("Turn vel: " + drivetrainData.GetOpenLoopOutput(Axis.TURN));
//		
		if(drivetrainData.getClassicDrive()) //hack to keep nice closed loop position/velocity on drive/turn axes, should fix later
		{
			double fwdOutput = ComputeOutput(Axis.FORWARD); //positive means forward
			double turnOutput = ComputeOutput(Axis.TURN); //positive means turning counter-clockwise. Matches the way driveEncoders work.
			System.out.println("fwd: " + fwdOutput);
			System.out.println("turn: " + turnOutput);
	
			leftFrontOutput = fwdOutput - turnOutput;
			rightFrontOutput = fwdOutput + turnOutput;
			
			leftBackOutput = leftFrontOutput;
			rightBackOutput = rightFrontOutput;
		}
		else
		{
			double fwdOutput = drivetrainData.GetOpenLoopOutput(Axis.FORWARD); 
			double turnOutput =  drivetrainData.GetOpenLoopOutput(Axis.TURN); 
			double strafeOutput = drivetrainData.GetOpenLoopOutput(Axis.STRAFE); 
			AsyncPrinter.warn("strafe out: " + strafeOutput);
			
			double leftFrontRawOutput = fwdOutput + turnOutput + strafeOutput;
			double rightFrontRawOutput = fwdOutput - turnOutput - strafeOutput;
			double leftBackRawOutput = fwdOutput + turnOutput - strafeOutput;
			double rightBackRawOutput = fwdOutput - turnOutput + strafeOutput;
			
			leftFrontOutput = ComputeMecanumOutput(DriveEncoders.Side.LEFT_FRONT, leftFrontRawOutput);
			rightFrontOutput = ComputeMecanumOutput(DriveEncoders.Side.RIGHT_FRONT, rightFrontRawOutput);
			leftBackOutput = ComputeMecanumOutput(DriveEncoders.Side.LEFT_BACK, leftBackRawOutput);
			rightBackOutput = ComputeMecanumOutput(DriveEncoders.Side.RIGHT_BACK, rightBackRawOutput);
		}

		leftFrontOutput = MathUtils.clamp(leftFrontOutput, -1.0, 1.0);
		rightFrontOutput = MathUtils.clamp(rightFrontOutput, -1.0, 1.0);
		leftBackOutput = MathUtils.clamp(leftBackOutput, -1.0, 1.0);
		rightBackOutput = MathUtils.clamp(rightBackOutput, -1.0, 1.0);

//		DashboardLogger.getInstance().logDouble("drivetrain-leftFront", leftFrontOutput);
//		DashboardLogger.getInstance().logDouble("drivetrain-rightFront", rightFrontOutput);
//		DashboardLogger.getInstance().logDouble("drivetrain-leftBack", leftBackOutput);
//		DashboardLogger.getInstance().logDouble("drivetrain-rightBack", rightBackOutput);
		
//		 frontLeft.set(leftFrontOutput);
//		 frontRight.set(rightFrontOutput);
//		
//		 backLeft.set(leftBackOutput);
//		 backRight.set(rightBackOutput);
		
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

		frontLeft.enableBrakeMode(true);
		backLeft.enableBrakeMode(true);
		frontRight.enableBrakeMode(true);
		backRight.enableBrakeMode(true);
	}

	public void UpdateDisabled()
	{
		escs[Side.FRONT_LEFT.ordinal()].SetDutyCycle(0.0);
		escs[Side.BACK_LEFT.ordinal()].SetDutyCycle(0.0);
		
		escs[Side.FRONT_RIGHT.ordinal()].SetDutyCycle(0.0);
		escs[Side.BACK_RIGHT.ordinal()].SetDutyCycle(0.0);
		
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
	}

	public void Configure()
	{
//		Kv = GetConfig("Kv", 1.0);
		
		ConfigurePIDObject(PIDs[VELOCITY][Axis.TURN.ordinal()], "velocity_turn", true);
		ConfigurePIDObject(PIDs[VELOCITY][Axis.FORWARD.ordinal()], "velocity_fwd", true);

		ConfigurePIDObject(PIDs[POSITION][Axis.TURN.ordinal()], "position_turn", false);
		ConfigurePIDObject(PIDs[POSITION][Axis.FORWARD.ordinal()], "position_fwd", false);
		
		ConfigurePIDObject(mecanumDrivePIDs[Side.BACK_LEFT.ordinal()], "mecanum", true);
		ConfigurePIDObject(mecanumDrivePIDs[Side.BACK_RIGHT.ordinal()], "mecanum", true);
		ConfigurePIDObject(mecanumDrivePIDs[Side.FRONT_LEFT.ordinal()], "mecanum", true);
		ConfigurePIDObject(mecanumDrivePIDs[Side.FRONT_RIGHT.ordinal()], "mecanum", true);

//		ConfigureForwardCurrentLimit();
//		ConfigureReverseCurrentLimit();
	}

	private void ConfigurePIDObject(PID pid, String pidName, boolean feedForward)
	{
		double p = GetConfig(pidName + "_P", 1.0);
		double i = GetConfig(pidName + "_I", 0.0);
		double d = GetConfig(pidName + "_D", 0.0);

		pid.SetParameters(p, i, d, 1.0, 0.87, feedForward,0.0);
	}
	
	private double ComputeMecanumOutput(DriveEncoders.Side wheel, double desiredOutput)
	{
		return desiredOutput;
//		mecanumDrivePIDs[wheel.ordinal()].SetSetpoint(desiredOutput);
//		mecanumDrivePIDs[wheel.ordinal()].SetInput(driveEncoders.GetNormalizedSpeed(wheel));
//		return mecanumDrivePIDs[wheel.ordinal()].Update(1/RobotConfig.LOOP_RATE);		
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