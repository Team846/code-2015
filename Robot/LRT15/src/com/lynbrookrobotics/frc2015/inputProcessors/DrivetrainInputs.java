package com.lynbrookrobotics.frc2015.inputProcessors;

import com.lynbrookrobotics.frc2015.componentData.DrivetrainData;
import com.lynbrookrobotics.frc2015.componentData.DrivetrainData.ControlMode;
import com.lynbrookrobotics.frc2015.config.Configurable;
import com.lynbrookrobotics.frc2015.config.DriverStationConfig;
import com.lynbrookrobotics.frc2015.driverstation.LRTDriverStation;
import com.lynbrookrobotics.frc2015.driverstation.LRTJoystick;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.AxisType;

public class DrivetrainInputs extends InputProcessor implements Configurable {
	
	LRTJoystick driverStick;
	LRTJoystick driverWheel;
	
	boolean lastStop;
	double negInertiaScalar;
	double negInertiaAccumulator;
	double lastTurn;
	double stoppedForward;
	double stoppedTurn;
	boolean constRadius;
	int blendExponent;
	int turnExponent;
	int constRadiusTurnExponent;
	int throttleExponent;
	double deadband;
	
	Axis axis;
	private DrivetrainData drivetrainData;
	
	public enum Axis
	{
		DRIVE,
		TURN,
		STRAFE
	}
	public DrivetrainInputs() {
		 driverStick = LRTDriverStation.Instance().GetDriverStick();
		 driverWheel = LRTDriverStation.Instance().GetDriverWheel();
		 
		 drivetrainData = DrivetrainData.Get();
	}

	public void Update()
	{
		double forward = -Math.pow(driverStick.getAxis(Joystick.AxisType.kY), throttleExponent);
	
		int signForward = forward > 0 ? 1 : -1;
	
		if (Math.abs(forward) < deadband)
			forward = 0.0;
		else
		{
			forward -= signForward * deadband;
			forward /= 1.0 - deadband;
		}
	
		if (axis == Axis.DRIVE)
		{
			drivetrainData.SetControlMode(DrivetrainData.Axis.FORWARD, ControlMode.VELOCITY_CONTROL);
			drivetrainData.SetVelocitySetpoint(DrivetrainData.Axis.FORWARD, (float)forward);
		}
		else if (axis == Axis.TURN)
		{
			double turn = 0.0;
			turn = -driverWheel.getAxis(AxisType.kX);
		//	turn = -m_driver_stick.GetAxis(Joystick.kZAxis);
			
			int sign = turn > 0 ? 1 : -1;
			
			if (constRadius)
			{
				if (constRadiusTurnExponent % 2 == 0)
					turn = sign * Math.pow(turn, constRadiusTurnExponent);
				else
					turn = Math.pow(turn, constRadiusTurnExponent);
			}
			else
			{
				if (turnExponent % 2 == 0)
					turn = sign * Math.pow(turn, turnExponent);
				else
					turn = Math.pow(turn, turnExponent);
			}
	
//			// Negative Inertia routine
//			double negInertia = turn - lastTurn;
//			lastTurn = turn;
//			
//			double negInertiaPower = negInertia * negInertiaScalar;
//			negInertiaAccumulator += negInertiaPower;
//			turn += negInertiaAccumulator;
//			
//			if (negInertiaAccumulator > 1) {
//				negInertiaAccumulator -= 1;
//			} else if (negInertiaAccumulator < -1) {
//				negInertiaAccumulator += 1;
//			} else {
//				negInertiaAccumulator = 0;
//			}
			
			// Blending routine
			double absForward = Math.abs(forward); // To ensure correct arc when switching direction
		
			double blend = Math.pow((1 - absForward), blendExponent); // Always between 0 and 1, raised to an exponent to adjust transition between in place and arc.
		
			 double turnInPlace = turn; // Normal turn
			 double constRadiusTurn = turn * absForward; // Arc turn
		
			double turnComposite;
			
			if (constRadius && !driverWheel.IsButtonDown(DriverStationConfig.JoystickButtons.QUICK_TURN))
				turnComposite = constRadiusTurn;
			else
				turnComposite = turnInPlace * (blend) + constRadiusTurn * (1 - blend); // Blended function
	
			drivetrainData.SetControlMode(DrivetrainData.Axis.TURN, DrivetrainData.ControlMode.VELOCITY_CONTROL);
			drivetrainData.SetVelocitySetpoint(DrivetrainData.Axis.TURN, (float)turnComposite);
		}
		if(axis == Axis.STRAFE)
		{
			double strafe = Math.pow(driverStick.getAxis(Joystick.AxisType.kX), throttleExponent);
			//TODO: implement strafe deadband
			
			drivetrainData.SetControlMode(DrivetrainData.Axis.STRAFE, DrivetrainData.ControlMode.VELOCITY_CONTROL);
			drivetrainData.SetVelocitySetpoint(DrivetrainData.Axis.STRAFE, (float)strafe);
		}
	
		if (driverWheel.IsButtonJustPressed(DriverStationConfig.JoystickButtons.REVERSE_DRIVE))
		{
			constRadius = !constRadius;
		}
	}
	
	public void Configure()
	{
		blendExponent = GetConfig("blend_exponent", 1);
		turnExponent = GetConfig("turn_exponent", 2);
		constRadiusTurnExponent = GetConfig("const_radius_turn_exponent", 1);
		throttleExponent = GetConfig("throttle_exponent", 1);
		deadband = GetConfig("deadband", 0.01);
		negInertiaScalar = GetConfig("neg_inertia_scalar", 5.0);
	}

}
