package com.lynbrookrobotics.frc2015.componentData;

import com.lynbrookrobotics.frc2015.sensors.LRTGyro;

public class DrivetrainData extends ComponentData
{
	public static final int FORWARD = 0;
	public static final int STRAFE = 1;
	public static final int TURN = 2;

	private double[] desiredRates = new double[3];
	private double[] desiredOpenLoopOutputs = new double[3];
	private boolean[] resetPositionSetpoint = new boolean[3];
	
	boolean fieldCentric;

	 private double[] positionSetpoints = new double[3];
	 private double[] maxSpeeds = new double[3];

	public DrivetrainData()
	{
		super("DrivetrainData");
		ResetCommands();
	}
	
	public void setFieldCentric(boolean state)
	{
		fieldCentric = state;
	}
	
	public boolean getFieldCentric()
	{
		return fieldCentric;
	}

	public double getVelocity(int type)
	{
		return desiredRates[type];
	}

	public void setVelocity(int type, double newValue)
	{
		desiredRates[type] = newValue;
	}

	public double getOpenLoopOutput(int type)
	{
		return desiredOpenLoopOutputs[type];
	}

	public void setOpenLoopOutput(int type, double newValue)
	{
		desiredOpenLoopOutputs[type] = newValue;
	}

	// TODO: Evaluate usage of an optical sensor
	// public double getPositionSetpoint(int type) {
	// return positionSetpoints[type];
	// }
	//
	// public void setPositionSetpoint(int type, double newValue) {
	// resetPositionSetpoint[type] = false;
	// positionSetpoints[type] = newValue;
	// }
	//
	// public void setRelativePositionSetpoint(int type, double newValue) {
	// resetPositionSetpoint[type] = false;
	// positionSetpoints[type] += newValue;
	// }
	//
	// public double getPositionControlMaxSpeed(int type) {
	// return maxSpeeds[type];
	// }
	//
	// public void setPositionControlMaxSpeed(int type, double newValue) {
	// maxSpeeds[type] = newValue;
	// }

	@Override
	protected void ResetCommands()
	{
		int[] types = { FORWARD, STRAFE, TURN };
		for (int type : types)
		{
			desiredRates[type] = 0;
			desiredOpenLoopOutputs[type] = 0;
			resetPositionSetpoint[type] = true;
			positionSetpoints[type] = 0;
			maxSpeeds[type] = 0;
		}
		fieldCentric = true;
	}
	
	
}
