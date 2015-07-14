package org.usfirst.frc.team846.robot;

import java.util.Arrays;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SPI;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	SPI gyro = new SPI(SPI.Port.kOnboardCS3);
	byte[] inputFromSlave = new byte[7];
	byte[] outputToSlave = new byte[6];
	
	private final byte L3GD20_REGISTER_OUT_X_L = 0x28;
	private final byte L3GD20_REGISTER_CTRL_REG1 = 0x20;
	private final byte L3GD20_REGISTER_CTRL_REG5 = 0x24;
	private final byte L3GD20_REGISTER_WHO_AM_I = 0x0F;
	private final byte L3GD20_REGISTER_FIFO_CTRL = 0x2E;
	private final byte L3GD20_REGISTER_FIFO_SRC = 0x2F;
	private final byte L3GD20_REGISTER_OUT_Y_L = 0x2A;
	private final byte L3GD20_REGISTER_OUT_Z_L = 0x2C;
	
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
	
	
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
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
        7-6  DR1/0     Output data rate                                   00 <- set to 00 (see pg.37)
        5-4  BW1/0     Bandwidth selection                                00
          3  PD        0 = Power-down mode, 1 = normal/sleep mode          0 <- set to 1
          2  ZEN       Z-axis enable (0 = disabled, 1 = enabled)           1 <- set to 1
          1  YEN       Y-axis enable (0 = disabled, 1 = enabled)           1 <- set to 0
          0  XEN       X-axis enable (0 = disabled, 1 = enabled)           1 <- set to 0 */
        /* Switch to normal mode and enable all three channels */
        
		
		// set REG1 to settings specified above
        byte[] out = new byte[2];
        out[0] = (byte) (L3GD20_REGISTER_CTRL_REG1);
        out[1] = (byte) (0x0C);//x and y disabled
        byte[] in = new byte[2];
        gyro.transaction(out, in, 2);

        
        // set FIFO_CTRL register 
        int fifoThreshold = 16 - 1; // size of FIFO (zero count)
        
        out[0] = (byte) (L3GD20_REGISTER_FIFO_CTRL);//sets FIFO to FIFO
        out[1] = (byte) (0x20 + fifoThreshold); // FM2:0 = 001 (pg. 42)
        gyro.transaction(out, in, 2);	
        
        // enable FIFO
        int FIFO_EN = 0x40; // turn on FIFO
        int StopOnFTH = 0x20; // enable threshold
        int command = FIFO_EN + StopOnFTH;
        
        out[0] = (byte) (L3GD20_REGISTER_CTRL_REG5);//enable FIFO
        out[1] = (byte) (command);
        gyro.transaction(out, in, 2);
        
        
        
        
        //System.out.println(Integer.toBinaryString(in[0]));
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }
    
    public void disabledPeriodic()
    {
//    	// TODO: time limit for accumulation
//    	if (justDisabled)
//    	{
//    		sumX = 0;
//    		sumY = 0;
//    		sumZ = 0;
//    		driftX = 0;
//    		driftY = 0;
//    		driftZ = 0;
//    		disabledCount = 0;
//    	}
    	updateGyro();
//    	disabledCount++;
//    	justDisabled = false;
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
//    	if (!justDisabled && disabledCount > 50)
//    	{
//    		driftX = sumX / disabledCount;
//    		driftY = sumY / disabledCount;
//    		driftZ = sumZ / disabledCount;
//    		sumX = 0;
//    		sumY = 0;
//    		sumZ = 0;
//    	}
    	updateGyro();
//    	justDisabled = true;
    }
    
    boolean isFIFOEmpty()
    {
    	//in is what we get from the gyro, out is what we write to the gyro
    	 byte[] out = new byte[2];//from gyro
         out[0] = (byte) (L3GD20_REGISTER_FIFO_SRC | 0x80); // set bit 0 (READ bit) to 1 (pg. 31)
         byte[] in = new byte[2];
         
         gyro.transaction(out, in, 2);

	        System.out.println(Integer.toBinaryString(in[1]));
         // check value of FIFO empty bit in FIFO_SRC
         if (((in[1] >> 5) & 0x1) == 0x01) {//if third bit from the left is 1 then it is empty
        	 // if 1
        	 
        	 return true;
         }
    	
    	return false;
    }
    boolean isFIFOFull()
    {
    	byte[] out = new byte[2];//from gyro
        out[0] = (byte) (L3GD20_REGISTER_FIFO_SRC | 0x80); // set bit 0 (READ bit) to 1 (pg. 31)
        byte[] in = new byte[2];
        
        gyro.transaction(out, in, 2);

        // check value of FIFO empty bit in FIFO_SRC
        if (((in[1] >> 6) & 1) == 1) {//if third bit from the left is 1 then it is empty
       	 // if 1
       	 
       	 return true;
        }
        return false;
    
    }
    private void updateGyro()
    {
    	Stack<Short> zValues = new Stack<Short>();
    	
    	//while (!(isFIFOEmpty())) {
	    	// read from FIFO
	        byte[] out = new byte[2];
	       /* if(isFIFOFull())
	        {
	        	while(isFIFOEmpty())
	        	{
	        		//        System.out.println(Byte.toString(out[0]) + " " + Integer.toBinaryString(Byte.toUnsignedInt(out[0])));
	    	        gyro.transaction(out, in, 2);
	    	        out = new byte[7];
	    	        Arrays.fill(outputToSlave, (byte)0x00);
	    	        out[0] = (byte) (L3GD20_REGISTER_OUT_X_L | (byte)0x80 | (byte)0x40); // do not change
	    	//        System.out.println(Byte.toString(out[0]) + " " + Integer.toBinaryString(Byte.toUnsignedInt(out[0])));
	    	        gyro.transaction(out, inputFromSlave, 7);//7 bytes is one block of data, 1 byte read, 2 for each axis
	    	        
	    	        final float factor = 0.00875F;
	    	        z = (short)((inputFromSlave[5] & 0xFF) | (inputFromSlave[6] << 8));
	    	        System.out.println(z);
	        	}
	        }*/
	    	//        System.out.println(Byte.toString(out[0]) + " " + Integer.toBinaryString(Byte.toUnsignedInt(out[0])));
	        out = new byte[7];
	        Arrays.fill(outputToSlave, (byte)0x00);
	        System.out.println(isFIFOEmpty());
	        out[0] = (byte) (L3GD20_REGISTER_OUT_X_L | (byte)0x80 | (byte)0x40); // do not change
	//        System.out.println(Byte.toString(out[0]) + " " + Integer.toBinaryString(Byte.toUnsignedInt(out[0])));
	        gyro.transaction(out, inputFromSlave, 7);//7 bytes is one block of data, 1 byte read, 2 for each axis
	        
	        final float factor = 0.00875F;
	        z = (short)((inputFromSlave[5] & 0xFF) | (inputFromSlave[6] << 8));
	        System.out.println(z);
	        //zValues.push(z);
	        
    	//}

    
//        System.out.println(Integer.toBinaryString(inputFromSlave[1] & 0xFF));
//        System.out.println(Integer.toBinaryString(inputFromSlave[2] & 0xFF));
//        System.out.println(Integer.toBinaryString(inputFromSlave[3] & 0xFF));
//        System.out.println(Integer.toBinaryString(inputFromSlave[4] & 0xFF));
//        System.out.println(Integer.toBinaryString(inputFromSlave[5] & 0xFF));
//        System.out.println(Integer.toBinaryString(inputFromSlave[6] & 0xFF));
    	//for ( Short zStackValue : zValues ) {
    		//System.out.println(zStackValue + ";");
    	//}
    	//System.out.println(zValues.isEmpty());
        
//        sumX += x * factor / 50 - driftX;
//        sumY += y * factor / 50 - driftY;
//        sumZ += z * factor / 50 - driftZ;
//        if (c++ % 25 == 0)
//        {
////        	sumX += 2150;
//        	System.out.printf("x: %f\n", sumX);
//        	System.out.printf("y: %f\n", sumY);
//        	System.out.printf("z: %f\n", sumZ);
//        }
    	
    }
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
}
