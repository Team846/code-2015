package componentData;

import sensors.LRTGyro;

public class DrivetrainData extends ComponentData
{
	public static final int VELOCITY_X = 0;
	public static final int VELOCITY_Y = 1;
	public static final int VELOCITY_ROTATION = 2;
	
	LRTGyro gyro;

	private double[] desiredRates = new double[3];
	private double[] desiredOpenLoopOutputs = new double[3];
	private boolean[] resetPositionSetpoint = new boolean[3];

	// private double[] positionSetpoints = new double[3];
	// private double[] maxSpeeds = new double[3];

	public DrivetrainData()
	{
		super("DrivetrainData");
		ResetCommands();
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
		int[] types = { VELOCITY_X, VELOCITY_Y, VELOCITY_ROTATION };
		for (int type : types)
		{
			desiredRates[type] = 0;
			desiredOpenLoopOutputs[type] = 0;
			resetPositionSetpoint[type] = true;
			// positionSetpoints[type] = 0;
			// maxSpeeds[type] = 0;
		}
	}
	
	//going forwards in respect to the field regardless of robot's
	protected void universalForward(){
		
		double dirInRad = gyro.getAngle() * Math.PI / 180.0;
	
		double desiredYVelocity;
		double desiredXVelocity;
		
		double compAngle = (Math.PI / 2) - dirInRad;
		
		desiredYVelocity = Math.sin(compAngle);
		desiredYVelocity = Math.cos(compAngle);
	}
	
}
