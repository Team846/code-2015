package org.usfirst.frc.team846.robot;

import java.util.Arrays;

import com.team846.frc2015.sensors.LRTGyro;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SPI;

public class Robot extends IterativeRobot {
	SPI gyro;
	byte[] inputFromSlave = new byte[7];
	byte[] outputToSlave = new byte[6];
	
	private final byte L3GD20_REGISTER_OUT_X_L = 0x28;//TODO put in binary and check correctness
	private final byte L3GD20_REGISTER_CTRL_REG1 = 0x20;//TODO put in binary and check correctness
	private final byte L3GD20_REGISTER_WHO_AM_I = 0x0F;//TODO put in binary and check correctness
	
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
    public void setUpGyro()
    {
    	gyro = new SPI(SPI.Port.kOnboardCS3);
    	gyro.setClockRate(2000000);
//    	gyro.setSampleDataOnRising();
    	gyro.setSampleDataOnFalling(); // Reversed
    	
    	gyro.setMSBFirst(); //set most significant bit first (see pg. 29)
    	

		gyro.setChipSelectActiveLow();
		gyro.setClockActiveLow();
		

        byte[] out = new byte[2];
        out[0] = (byte) (L3GD20_REGISTER_CTRL_REG1);
        out[1] = (byte) (0xCF);//disables x and y axis TODO check if correct 
        byte[] in = new byte[2];
        gyro.transaction(out, in, 2);
        System.out.println(Integer.toBinaryString(in[0]));

		/*if (instance == null)
		{
			instance = this;
		}*/
    }
    public void setByte(byte toSet, byte... modifiers)
    {
    	toSet = modifiers[0];
    	for(int i =0; i<modifiers.length; i++)//for each loop
    	{
    		toSet = (byte) (toSet | modifiers[i]);
    	}
    }
    public short generateUsefullVallues(byte gyroData1, byte gyroData2)
    {
    	return (short) ((short)(gyroData1 & 0xFF) | gyroData2<<8));
    }
	public void updateGyro()
	{
		byte[] out = new byte[2];
        byte[] in = new byte[2];
        Arrays.fill(outputToSlave, (byte)0x00);
        //out[0] = (byte) (L3GD20_REGISTER_WHO_AM_I | (byte)0x80); replaced by next line
        setByte(out[0], L3GD20_REGISTER_WHO_AM_I, (byte)0x80);
//        System.out.println(Byte.toString(out[0]) + " " + Integer.toBinaryString(Byte.toUnsignedInt(out[0])));
        gyro.transaction(out, in, 2);
        out = new byte[7];
        Arrays.fill(outputToSlave, (byte)0x00);
        //out[0] = (byte) (L3GD20_REGISTER_OUT_X_L | (byte)0x80 | (byte)0x40);
        setByte(out[0], L3GD20_REGISTER_OUT_X_L, (byte)0x80, (byte)0x40);
//        System.out.println(Byte.toString(out[0]) + " " + Integer.toBinaryString(Byte.toUnsignedInt(out[0])));
        gyro.transaction(out, inputFromSlave, 7);
        
        final float factor = 0.00875F;
        x = (short)((inputFromSlave[1] & 0xFF )| (inputFromSlave[2] << 8));
        y = (short)((inputFromSlave[3] & 0xFF) | (inputFromSlave[4] << 8));
        z = (short)((inputFromSlave[5] & 0xFF) | (inputFromSlave[6] << 8));
        
        sumX += x * factor / 50 - driftX;
        sumY += y * factor / 50 - driftY;
        sumZ += z * factor / 50 - driftZ;
	}
	@Override
	public void robotInit() {
		setUpGyro();
		 
	}	
	
	@Override
	public void disabledPeriodic() {
		
	};

}
