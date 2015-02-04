package com.lynbrookrobotics.frc2015.sensors;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;

public class LRTGyro extends SensorFactory 
{
	
	private byte OUT_X_L = 0B00101000;
	private byte OUT_X_H = 0B00101001;
	private byte OUT_Y_L = 0B00101010;
	private byte OUT_Y_H = 0B00101011;
	
	private double angle = 0.0;
	
	SPI digitalInput;
	
	private float minVoltage;
	private float maxVoltage;
	
	public LRTGyro(int portA)
	{
		digitalInput = new SPI(Port.kOnboardCS0);
		
	}
	
	public void update()
	{
		int X_L = read(OUT_X_L);
		int X_H = read(OUT_X_H);
		int Y_L = read(OUT_Y_L);
		int Y_H = read(OUT_Y_H);
		
		int velX = X_H << 8 | X_L;
		int velY = Y_H << 8 | Y_L;
		
		System.out.println("X: " + velX + " Y: " + velY);
	}
	
	public double getAngle()
	{
		return angle;
	}
	
	public int read(byte adress)
	{
		int retval;
		
		byte[] read = new byte[1];
		byte[] send = new byte[1];
		
		send[1] = adress;
		
		retval = digitalInput.transaction(send, read, 1);
		
		return retval;
	}
}
