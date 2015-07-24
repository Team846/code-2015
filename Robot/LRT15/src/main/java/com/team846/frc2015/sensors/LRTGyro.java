package com.team846.frc2015.sensors;

import java.util.Arrays;

import com.team846.frc2015.driverstation.GameState;
import com.team846.robot.RobotState;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SPI.Port;

public class LRTGyro extends SensorFactory
{
	SPI gyro;
	byte[] inputFromSlave = new byte[7];
	byte[] outputToSlave = new byte[6];
	
	private final byte L3GD20_REGISTER_OUT_X_L = 0x28;
	private final byte L3GD20_REGISTER_CTRL_REG1 = 0x20;
	private final byte L3GD20_REGISTER_WHO_AM_I = 0x0F;
	
	int c = 0;
	float sumX = 0;
	float sumY = 0;
	float sumZ = 0;
	
	short x = 0;
	short y = 0;
	short z = 0;
	
	boolean justDisabled = true;
	long disabledCount = 0;
	
	float driftX = 0;
	float driftY = 0;
	float driftZ = 0;
	
	private static LRTGyro instance = null;
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public LRTGyro() {
    	gyro = new SPI(SPI.Port.kOnboardCS3);
    	gyro.setClockRate(2000000);
//    	gyro.setSampleDataOnRising();
    	gyro.setSampleDataOnFalling(); // Reversed
    	
    	gyro.setMSBFirst(); //set most significant bit first (see pg. 29)
    	

		gyro.setChipSelectActiveLow();
		gyro.setClockActiveLow();

        /* Set CTRL_REG1 (0x20)
        ====================================================================
        BIT  Symbol    Description                                   Default
        ---  ------    ---------------------------------`------------ -------
        7-6  DR1/0     Output data rate                                   00
        5-4  BW1/0     Bandwidth selection                                00
          3  PD        0 = Power-down mode, 1 = normal/sleep mode          0
          2  ZEN       Z-axis enable (0 = disabled, 1 = enabled)           1
          1  YEN       Y-axis enable (0 = disabled, 1 = enabled)           1
          0  XEN       X-axis enable (0 = disabled, 1 = enabled)           1 */
        /* Switch to normal mode and enable all three channels */
        
        byte[] out = new byte[2];
        out[0] = (byte) (L3GD20_REGISTER_CTRL_REG1);
        out[1] = (byte) (0xCF);
        byte[] in = new byte[2];//in is not defined
        gyro.transaction(out, in, 2);
        System.out.println(Integer.toBinaryString(in[0]));

		if (instance == null)
			instance = this;
    }

	public static LRTGyro Get()
	{
		if (instance == null)
			instance = new LRTGyro();
		return instance;
	}

    public void update()
    {
		if (RobotState.Instance().GameMode() == GameState.DISABLED)
		{
	    	// TODO: time limit for accumulation
	    	if (justDisabled)
	    	{
	    		sumX = 0;
	    		sumY = 0;
	    		sumZ = 0;
	    		driftX = 0;
	    		driftY = 0;
	    		driftZ = 0;
	    		disabledCount = 0;
	    	}
	    	updateGyro();
	    	disabledCount++;
	    	justDisabled = false;
		}
		else
		{
	    	if (!justDisabled)
	    	{
	    		driftX = sumX / disabledCount;
	    		driftY = sumY / disabledCount;
	    		driftZ = sumZ / disabledCount;
	    		sumX = 0;
	    		sumY = 0;
	    		sumZ = 0;
	    	}
	    	updateGyro();
	    	justDisabled = true;	
		}
    }
    
    public float getX()
    {
    	return sumX;
    }
    
    public float getY()
    {
    	return sumY;
    }
    
    public float getZ()
    {
    	return sumZ;
    }
    
    private void updateGyro()
    {
        byte[] out = new byte[2];
        byte[] in = new byte[2];
        Arrays.fill(outputToSlave, (byte)0x00);
        out[0] = (byte) (L3GD20_REGISTER_WHO_AM_I | (byte)0x80);
//        System.out.println(Byte.toString(out[0]) + " " + Integer.toBinaryString(Byte.toUnsignedInt(out[0])));
        gyro.transaction(out, in, 2);
        out = new byte[7];
        Arrays.fill(outputToSlave, (byte)0x00);
        out[0] = (byte) (L3GD20_REGISTER_OUT_X_L | (byte)0x80 | (byte)0x40);
//        System.out.println(Byte.toString(out[0]) + " " + Integer.toBinaryString(Byte.toUnsignedInt(out[0])));
        gyro.transaction(out, inputFromSlave, 7);

        final float factor = 0.00875F;
        x = (short)(Byte.toUnsignedInt(inputFromSlave[1]) | (Byte.toUnsignedInt(inputFromSlave[2]) << 8));
        y = (short)((inputFromSlave[3] & 0xFF) | (inputFromSlave[4] << 8));
        z = (short)((inputFromSlave[5] & 0xFF) | (inputFromSlave[6] << 8));

//        System.out.println(Integer.toBinaryString(inputFromSlave[1] & 0xFF));
//        System.out.println(Integer.toBinaryString(inputFromSlave[2] & 0xFF));
//        System.out.println(Integer.toBinaryString(inputFromSlave[3] & 0xFF));
//        System.out.println(Integer.toBinaryString(inputFromSlave[4] & 0xFF));
//        System.out.println(Integer.toBinaryString(inputFromSlave[5] & 0xFF));
//        System.out.println(Integer.toBinaryString(inputFromSlave[6] & 0xFF));
//        System.out.println("x: " + x);
//        System.out.println("y: " + y);
//        System.out.println("z: " + z);
        sumX += x * factor / 50 - driftX;
        sumY += y * factor / 50 - driftY;
        sumZ += z * factor / 50 - driftZ;
//        if (c++ % 25 == 0)
//        {
//        	System.out.printf("x: %f\n", sumX);
//        	System.out.printf("y: %f\n", sumY);
//        	System.out.printf("z: %f\n", sumZ);
//        }
    }
}
