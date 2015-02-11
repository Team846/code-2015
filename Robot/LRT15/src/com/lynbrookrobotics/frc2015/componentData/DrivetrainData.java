package com.lynbrookrobotics.frc2015.componentData;

import java.lang.reflect.Array;
import java.util.Arrays;

import com.lynbrookrobotics.frc2015.sensors.DriveEncoders;
import com.lynbrookrobotics.frc2015.sensors.LRTGyro;

public class DrivetrainData extends ComponentData
{
	ControlMode[] controlModes;
	double[] desiredOpenLoopOutputs;
	double[] desiredRates;
	double[] positionSetpoints;
	double[] maxSpeeds;
	float zeroHeading;
	boolean overrideCurrentLimitForward;
	boolean overrideCurrentLimitReverse;
	float currentLimitForward;
	float currentLimitReverse;
	
	boolean[] resetPositionSetpoint;
	 
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
		ResetCommands();
		Arrays.fill(resetPositionSetpoint, true);
		Arrays.fill(positionSetpoints, 0);
		Arrays.fill(maxSpeeds, 0);
		Arrays.fill(controlModes, ControlMode.OPEN_LOOP);
	}

	public static DrivetrainData Get()
	{
		return (DrivetrainData) ComponentData.GetComponentData("DrivetrainData");
	}

	public void ResetCommands()
	{
		overrideCurrentLimitForward = false;
		overrideCurrentLimitReverse = false;
		currentLimitForward = 0.5f;
		currentLimitReverse = 0.5f;
		
		Arrays.fill(desiredRates, 0.0);
	}

//	void Log()
//	{
//		LogToFile(&controlModes, "ControlModes");
//		LogToFile(&desiredOpenLoopOutputs, "OpenLoopOutputs");
//		LogToFile(&desiredRates, "DesiredRates");
//		LogToFile(&positionSetpoints, "PositionSetpoints");
//		LogToFile(&maxSpeeds, "MaxSpeeds");
//		LogToFile(&zeroHeading, "ZeroHeading");
//		LogToFile(&overrideCurrentLimitForward, "OverrideForwardCurrentLimit");
//		LogToFile(&overrideCurrentLimitReverse, "OverrideReverseCurrentLimit");
//		LogToFile(&currentLimitForward, "ForwardCurrentLimit");
//		LogToFile(&currentLimitReverse, "ReverseCurrentLimit");
//	}

	public void SetControlMode(Axis axis, ControlMode mode)
	{
		if (controlModes[axis.ordinal()] != ControlMode.POSITION_CONTROL && mode == ControlMode.POSITION_CONTROL && resetPositionSetpoint[0])
			positionSetpoints[axis.ordinal()] = axis == Axis.FORWARD ? DriveEncoders.Get().GetRobotDist() : DriveEncoders.Get().GetTurnAngle();
		if (controlModes[axis.ordinal()] == ControlMode.POSITION_CONTROL && mode != ControlMode.POSITION_CONTROL)
			resetPositionSetpoint[axis.ordinal()] = true;
		controlModes[axis.ordinal()] = mode;
	}

	public void SetVelocitySetpoint(Axis axis, float velocity)
	{
		desiredRates[axis.ordinal()] = velocity;
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
		if (limit < 0)
			limit = 0;
		else if (limit > 1.0)
			limit = 1.0f;
		currentLimitForward = limit;
	}

	public void OverrideReverseCurrentLimit(float limit)
	{
		overrideCurrentLimitReverse = true;
		if (limit < 0)
			limit = 0;
		else if (limit > 1.0)
			limit = 1.0f;
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
}
