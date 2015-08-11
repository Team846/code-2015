import com.team846.frc2015.driverstation.GameState;

import java.lang.System;
import java.util.Arrays;

public class LRTGyro extends SensorFactory
{

	private final int BYPASSMODE = 0; //Make sure that this is the same as in gyroCommunications class
	private final int STREAMMODE = 1;//Make sure that this is the same as in gyroCommunications class
	private final int mode = STREAMMODE;

	public static final boolean CALIBRATE = true; //Make sure that this is the same as in gyroCommunications class
	public static final boolean DONT_CALIBRATE = false; //Make sure that this is the same as in gyroCommunications class

	boolean calibrated = false;

	double yVel = 0;
	double previousYVel = 0;
	double angle = 0;

	double driftY = 0;

	double yCalibValues[] = new double[100];//calib only uses last 100 values to make sure that it doesn't use values when robot is moving

	long previousTime = System.currentTimeMillis();
	int timePassed = previousTime;

	long disabledCount = 0;
	long calibCount = 0;
	long tickNum = 0;

	boolean justDisabled = true;
	boolean justEnabled  = true;

	private static LRTGyro instance = null;
	private LRTGyroCommunication gyroCom = null;//gyroCom is short for gyro Communications

	private LRTGyro() //initialization code, ensures there is only one gyrp object
	{
		gyroCom = LRTGyroCommunication.getInstance();
	}

	public static LRTGyro getInstance()
	{
		if(instance == null)//ensures there is only one gyrp object
		{
			instance = new LRTGyro();
		}

		return instance;
	}

	public void update()
	{

		if(Robot.state.Instance().GameMode == GameState.DISABLED) //TODO: include && Math.abs(encoderVelocity) == 0
		{

			if(!calibrated)
			{
				gyroCom.updateGyro(CALIBRATE, mode, 0);//do calibrate
				calibCount++;
				yVel = gyroCom.getYVel();

				yCalibValues[(int)(calibCount % yCalibValues.getLength)] = yVel;

				driftY = calibrateGyro(yCalibValues);
			}
			else//if already calibrated
			{
				gyroCom.updateGyro(CALIBRATE, mode, driftY);
				previousYVel = yVel;
				yVel = gyroCom.getYVel();

				timePassed = getTimePassed();

				angle = trapaziodalIntegration(angle, yVel, previousYVel);

				System.out.print("Tick: " + tickNum);
				System.out.println("Angle is :" + this.getAngle());
			}

			tickNum++;
			disabledCount++;
		}

		if(Robot.state.Instance().GameMode == GameState.ENABBLED || Robot.state.Instance().GameMode == GameState.AUTONOMOUS)
		{
			if(justEnabled)
			{
				resetTimer();//ensures that timePassed is accurate
				justEnabled = false;
				justDisabled = true;
			}

			gyroCom.updateGyro(DONT_CALIBRATE, mode, driftY);//all returned values are calibrated
			previousYVel = yVel;
			yVel = gyroCom.getYVel();

			timePassed = getTimePassed();

			angle = trapaziodalIntegration(angle, yVel, previousYVel);

			System.out.print("Tick: " + tickNum);
			System.out.println("Angle is :" + this.getAngle());

		}

		tickNum++;
	}

	private double calibrateGyro(double[] yCalibValues)//called after updateGyro()
	{
		double sumY = 0;

		if (calibCount < yCalibValues.getLength)//if calibraating for less than 2 seconds
		{
			for (int i = 0; i < calibCount; i++)
			{
				sumY += yCalibValues[i];
			}

			return sumY / calibCount;
		}

		for (int i = 0; i < yCalibValues.getLength; i++)//100 calib values
		{
			sumY += yCalibValues[i];
		}

		return sumY / yCalibValues.getLength;
	}

	public double getAngle()
	{
		if(angle < -180)
		{
			return (angle + 360);
		}
		else if(angle > 180)
		{
			return (angle - 360);
		}
		else  //above 2 statements ensure that |angle returned| <= 180 degrees
		{
			return angle;
		}
	}

	//All below are abstraction methods
	public double getVel()
	{
		return yVel;
	}

	private double average(double[] yFIFOValues2)
	{
		double sum = 0;

		for(int i = 0; i<yFIFOValues.getLength; i++)
		{
			sum += yFIFOValues[i];
		}
		return sum / yFIFOValues.getLength;
	}

	private void resetTimer()
	{
		previousTime = System.currentTimeMillis();
	}

	private void resetcalibCount()
	{
		calibCount = 0;
	}

	public void resetAngle()
	{
		angle = 0;
	}

	private void resetDisabledCount()
	{
		disabledCount = 0;
	}

	private int getTimePassed()
	{
		return (System.currentTimeMillis() - previousTime);//gets time in between gyro readings
	}

	private double riemannSum(double previousAngle, double yVel)
	{
		return (previousAngle + timePassed * yVel/1000);
	}

	private double trapaziodalIntegration(double previousAngle, double yVel, double previousYVel)
	{
		return (  previousAngle + timePassed * ( (yVel + previousYVel)/2 )/1000  );
	}
}