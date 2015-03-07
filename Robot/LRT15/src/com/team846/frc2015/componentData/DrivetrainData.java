package com.team846.frc2015.componentData;

import java.lang.reflect.Array;
import java.util.Arrays;

import com.team846.frc2015.log.AsyncPrinter;
import com.team846.frc2015.sensors.DriveEncoders;
import com.team846.frc2015.sensors.LRTGyro;
import com.team846.frc2015.utils.MathUtils;

public class DrivetrainData extends ComponentData
{
	ControlMode[] controlModes;
	double[] desiredOpenLoopOutputs;
	double[] desiredRates;
	double[] positionSetpoints;
	double[] maxSpeeds;
	boolean overrideCurrentLimitForward;
	boolean overrideCurrentLimitReverse;
	float currentLimitForward;
	float currentLimitReverse;
	
	boolean[] resetPositionSetpoint;
	private boolean classic;
	 
	public enum ControlMode
	{
		POSITION_CONTROL,
		VELOCITY_CONTROL, 
		OPEN_LOOP
	}

	public enum Axis
	{
		FORWARD,
		STRAFE,
		TURN
	}

	public enum Side
	{
		LEFT,
		RIGHT
	}

	public DrivetrainData() 
	{
		super("DrivetrainData");
		
		classic = false;
		
		controlModes = new ControlMode[3];
		desiredOpenLoopOutputs = new double[3];
		desiredRates = new double[3];
		positionSetpoints = new double[3];
		maxSpeeds = new double[3];
		resetPositionSetpoint = new boolean[3];
		
		ResetCommands();
		Arrays.fill(desiredOpenLoopOutputs, 0.0);
		Arrays.fill(resetPositionSetpoint, true);
		Arrays.fill(positionSetpoints, 0);
		Arrays.fill(maxSpeeds, 0);
		Arrays.fill(controlModes, ControlMode.OPEN_LOOP);
	}

	public static DrivetrainData get()
	{
		return (DrivetrainData) ComponentData.GetComponentData("DrivetrainData");
	}

	public void ResetCommands()
	{
		classic = false;
		overrideCurrentLimitForward = false;
		overrideCurrentLimitReverse = false;
		currentLimitForward = 0.5f;
		currentLimitReverse = 0.5f;
		
		Arrays.fill(desiredRates, 0.0);
		Arrays.fill(desiredOpenLoopOutputs, 0.0);
	}

	public void SetControlMode(Axis axis, ControlMode mode)
	{
		if (controlModes[axis.ordinal()] != ControlMode.POSITION_CONTROL && mode == ControlMode.POSITION_CONTROL && resetPositionSetpoint[0])
		{
			if(axis == Axis.FORWARD)
				positionSetpoints[axis.ordinal()] = DriveEncoders.Get().GetRobotDist();
			else if(axis == Axis.TURN)
				positionSetpoints[axis.ordinal()] = DriveEncoders.Get().GetTurnAngle();
			else
				AsyncPrinter.warn("Position setpoint for strafing not supported");
		}
		if (controlModes[axis.ordinal()] == ControlMode.POSITION_CONTROL && mode != ControlMode.POSITION_CONTROL)
			resetPositionSetpoint[axis.ordinal()] = true;
		controlModes[axis.ordinal()] = mode;
	}

	public void SetVelocitySetpoint(Axis axis, double d)
	{
		desiredRates[axis.ordinal()] = d;
	}

	public void SetOpenLoopOutput(Axis axis, double setpoint)
	{
		desiredOpenLoopOutputs[axis.ordinal()] = setpoint;
	}

	public void SetPositionSetpoint(Axis axis, double setpoint)
	{
		resetPositionSetpoint[axis.ordinal()] = false;
		positionSetpoints[axis.ordinal()] = setpoint;
	}

	public void SetRelativePositionSetpoint(Axis axis, double setpoint)
	{
		resetPositionSetpoint[axis.ordinal()] = false;
		positionSetpoints[axis.ordinal()] += setpoint;
	}

	public void SetPositionControlMaxSpeed(Axis axis, double speed)
	{
		maxSpeeds[axis.ordinal()] = speed;
	}

	public ControlMode GetControlMode(Axis axis)
	{
		return controlModes[axis.ordinal()];
	}

	public double GetOpenLoopOutput(Axis axis)
	{
		return desiredOpenLoopOutputs[axis.ordinal()];
	}

	public double GetVelocitySetpoint(Axis axis)
	{
		return desiredRates[axis.ordinal()];
	}

	public double GetPositionSetpoint(Axis axis)
	{
		return positionSetpoints[axis.ordinal()];
	}

	public double GetPositionControlMaxSpeed(Axis axis)
	{
		return maxSpeeds[axis.ordinal()];
	}

	public void OverrideForwardCurrentLimit(float limit)
	{
		overrideCurrentLimitForward = true;
		limit = MathUtils.clamp(limit, 0.0f, 1.0f);
		currentLimitForward = limit;
	}

	public void OverrideReverseCurrentLimit(float limit)
	{
		overrideCurrentLimitReverse = true;
		limit = MathUtils.clamp(limit, 0.0f, 1.0f);
		currentLimitReverse = limit;
	}

	public float GetForwardCurrentLimit()
	{
		overrideCurrentLimitForward = false;
		return currentLimitForward;
	}

	public float GetReverseCurrentLimit()
	{
		overrideCurrentLimitReverse = false;
		return currentLimitReverse;
	}

	public boolean ShouldOverrideForwardCurrentLimit()
	{
		return overrideCurrentLimitForward;
	}

	public boolean ShouldOverrideReverseCurrentLimit()
	{
		return overrideCurrentLimitReverse;
	}
	
	public void setClassicDrive(boolean on)
	{
		classic = on;
	}
	
	public boolean getClassicDrive()
	{
		return classic;
	}
}
