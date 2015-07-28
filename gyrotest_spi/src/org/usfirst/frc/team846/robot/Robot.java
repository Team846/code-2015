
package org.usfirst.frc.team846.robot;
 
import java.util.Arrays;
 

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SPI;
 
public class Robot extends IterativeRobot {
    SPI gyro;
    byte[] inputFromSlave = new byte[7];
    byte[] outputToSlave = new byte[7];
    
    final double conversionFactor = 0.00875F;
     
    private final byte L3GD20_REGISTER_OUT_X_L = 0x28;//TODO put in binary and check correctness //correct
    private final byte L3GD20_REGISTER_CTRL_REG1 = 0x20;//TODO put in binary and check correctness //correct
    private final byte L3GD20_REGISTER_CTRL_REG5 = 0x24;
    //private final byte L3GD20_REGISTER_WHO_AM_I = 0x0F;//TODO put in binary and check correctness //correct
    private final byte L3GD20_REGISTER_FIFO_CTRL = (byte) 0x2E;//
 
    private final int BYPASSMODE = 0;
    private final int STREAMMODE = 1;
 
    int c = 0;
   // float //sumX = 0;
    //float //sumY = 0;
    //float //sumZ = 0;
     
    public static final boolean CALIBRATE = true;
    public static final boolean DONT_CALIBRATE = false;
 
    short y = 0;
    double yVel = 0;
 
    double yFIFOValues[] = new double[32];//gyro queue only stores 32 values
    double yCalibValues[] = new double[100];//calib only uses last 100 values to make sure that it doesn't use values when robot is moving
     
    boolean justDisabled = true;
    long disabledCount = 0;
 
    long tickNum = 0;
     
    double driftX = 0;
    double driftY = 0;
    double driftZ = 0;
    public void setupGyro(int mode)
    {
        gyro = new SPI(SPI.Port.kOnboardCS3);
        gyro.setClockRate(2000000);
//      gyro.setSampleDataOnRising();
        gyro.setSampleDataOnFalling(); // Reversed
         
        gyro.setMSBFirst(); //set most significant bit first (see pg. 29)
         
 
        gyro.setChipSelectActiveLow();
        gyro.setClockActiveLow();
         
 
        byte[] out = new byte[2];
        out[0] = (byte) (L3GD20_REGISTER_CTRL_REG1);
        out[1] = (byte) (0b11001010);//WRONG should be 0b11001010 //here, only y axis is enabled
        byte[] in = new byte[2];
        gyro.transaction(out, in, 2);
        
        if(mode==STREAMMODE)
        {
        	out[0] = (byte) (L3GD20_REGISTER_CTRL_REG5);
        	out[1] = (byte) 0b01000000;
        	gyro.transaction(out, in, 2);//enable FIFO
        	
        	out[0] = (byte) (L3GD20_REGISTER_FIFO_CTRL);
        	out[1] = (byte) (0b01000000);//byte that sets to stream mode
        	gyro.transaction(out, in, 2);
        }
        //System.out.println(Integer.toBinaryString(in[0]));
        /*if (instance == null)
        {
            instance = this;
        }*/
    }
    public byte setByte(byte toSet, byte... modifiers)
    {
        toSet = modifiers[0];
        for(int i =0; i<modifiers.length; i++)//for each loop
        {
            toSet = (byte) (toSet | modifiers[i]);
        }
        
        return toSet;
    }
    public short byteToAngVel(byte gyroData1, byte gyroData2)
    {
        final float conversionFactor = 0.00875F;
        return (short)((gyroData1 & 0xFF | gyroData2<<8));// * conversionFactor / 50); //converts bytes given by gyro to meaningful data
    }
    public double calibrateGyro()                   //called after updateGyro()
    {
    	driftY = 0;
        if(tickNum == 1)//if gyro has only been checked one time
        {
            Arrays.fill(yCalibValues, yCalibValues[0]);//avoids driftY from being filled with random values
        } 
        for(int i = 0; i<100; i++)//100 calib values
        {
            driftY += yCalibValues[i];
        }
        return driftY / 100.0;
    }
    boolean isFIFOFull()
    {
        byte[] out = new byte[2];//from gyro
        out[0] = (byte) (L3GD20_REGISTER_FIFO_CTRL | 0x80); // set bit 0 (READ bit) to 1 (pg. 31)
        byte[] in = new byte[2];
         
        gyro.transaction(out, in, 2);
 
        // check value of FIFO empty bit in FIFO_SRC
        if (((in[1] >> 6) & 1) == 1) {//if second bit from the left is 1 then it is empty
         // if 1
          
            return true;
        }
        return false;
     
    }
    boolean isFIFOEmpty()
    {
        //in is what we get from the gyro, out is what we write to the gyro
         byte[] out = new byte[2];//from gyro
         out[0] = (byte) (L3GD20_REGISTER_FIFO_CTRL | 0x80); // set bit 0 (READ bit) to 1 (pg. 31)
         byte[] in = new byte[2];
          
         gyro.transaction(out, in, 2);
 
           // System.out.println(Integer.toBinaryString(in[1]));
         // check value of FIFO empty bit in FIFO_SRC
         if (((in[1] >> 5) & 0b1) == 0b1) {//if third bit from the left is 1 then it is empty
             // if 1
              
             return true;
         }
         
        return false;
    }
    double average(double[] yFIFOValues2)
    {
        short sum = 0;
 
        for(int i = 0; i<32; i++)
        {
            sum += yFIFOValues[i];
        }
        return sum/32;
    }
    public void updateGyro(boolean calibrate, int streamOrBypass)
    {
        /*byte[] out = new byte[2];
        byte[] in = new byte[2];
        Arrays.fill(outputToSlave, (byte)0x00);
        //out[0] = (byte) (L3GD20_REGISTER_WHO_AM_I | (byte)0x80); replaced by next line
        setByte(out[0], L3GD20_REGISTER_WHO_AM_I, (byte)0x80);
//        System.out.println(Byte.toString(out[0]) + " " + Integer.toBinaryString(Byte.toUnsignedInt(out[0])));
        gyro.transaction(out, in, 2);This is just who am I stuff*/
        switch(streamOrBypass)
        {
            case STREAMMODE:
            	if(isFIFOFull() == true)
            	{
            		int FIFOCount = 0;
            		
            		for(int i =0; i<32; i++)//FIFO queue is 32 bit
            		{
            			//byte out = new byte[7];//maybe this should be commented, and outputToSlave should be made 7 bytes
            			Arrays.fill(outputToSlave, (byte)0x00);//change 0x00 to 0b00000000  //so that gyro doesn't receive random, compiler inserted stuff
            			//out[0] = (byte) (L3GD20_REGISTER_OUT_X_L | (byte)0x80 | (byte)0x40);
            			//System.out.println("FIFO count is " + FIFOCount);
            			
            			outputToSlave[0] = setByte(outputToSlave[0], L3GD20_REGISTER_OUT_X_L, (byte)0x80, (byte)0x40);//do not change (?) 
            			
            			gyro.transaction(outputToSlave, inputFromSlave, 7);
                
                     
            			//x = (short)((inputFromSlave[1] & 0xFF )| (inputFromSlave[2] << 8));//axis disabled
            			yVel = ((inputFromSlave[3] & 0xFF) | (inputFromSlave[4] << 8)) * conversionFactor;
            			//z = (short)((inputFromSlave[5] & 0xFF) | (inputFromSlave[6] << 8));//axis disabled
            			//y = byteToAngVel(inputFromSlave[3], inputFromSlave[4]);//two bytes are used to store one datapoint
            			//y = 
                    ////sumX += x * factor / 50 - driftX;
              			if(calibrate == false)//is not currently callibrating
              			{
            				yFIFOValues[(int) FIFOCount] = y - driftY;
            				////sumY += y - driftY;
            			}
            			else //is currently calibrating
            			{
            				yFIFOValues[(int)FIFOCount] = yVel; 
                        //sumY += y;
            			}
 
                    FIFOCount++;
                    
            		}
            		yVel = average(yFIFOValues);
                //sumY = 
            		if(calibrate == true)
            		{
 
            			yCalibValues[(int)tickNum%100] = yVel;
            		}
            	}
            	
            	tickNum++;
            	break;
            case BYPASSMODE:
            	//byte[] out = new byte[2];
            	//byte[] in = new byte[2];
            	//Arrays.fill(outputToSlave, (byte)0x00);
            	//out[0] = (byte) (L3GD20_REGISTER_WHO_AM_I | (byte)0x80); replaced by next line
            	//setByte(out[0], L3GD20_REGISTER_WHO_AM_I, (byte)0x80);
//      	  System.out.println(Byte.toString(out[0]) + " " + Integer.toBinaryString(Byte.toUnsignedInt(out[0])));
            	//gyro.transaction(out, in, 2);
            	//System.out.println(in[1]);
            	//byte[] out = new byte[7];//maybe this should be commented, and outputToSlave should be made 7 bytes
            	Arrays.fill(outputToSlave, (byte)0b00000000);//change 0x00 to 0b00000000 //changed 7 0's to 8 0's
            	//out[0] = (byte) (L3GD20_REGISTER_OUT_X_L | (byte)0x80 | (byte)0x40);
            	// byteHandler bHandler = new  byteHandler();
            	//setByte(bHandler, L3GD20_REGISTER_OUT_X_L, (byte)0x80);//do not change (?) 
            	//out[0] = bHandler.value;
        
            	//outputToSlave[0] = setByte(L3GD20_REGISTER_OUT_X_L, (byte)0b10000000, (byte)0b01000000);//made setByte1 return a value //before, 0b10000000 was 0x80 //sets second byte to 1 
            	outputToSlave[0] = L3GD20_REGISTER_OUT_X_L | (byte)0x80 | (byte)0x40;
            	gyro.transaction(outputToSlave, inputFromSlave, 7);
            	// final float factor = 0.00875F;
            	//x = (short)((inputFromSlave[1] & 0xFF )| (inputFromSlave[2] << 8));//axis disabled
            	yVel = (short)((inputFromSlave[3] & 0xFF) | (inputFromSlave[4] << 8)) *  conversionFactor;  //this has been handled in byteToAnglVel()
            	//z = (short)((inputFromSlave[5] & 0xFF) | (inputFromSlave[6] << 8));//axis disabled
            	//y = byteToAngVel(inputFromSlave[3], inputFromSlave[4]);//two bytes are used to store one datapoint
     
            	////sumX += x * factor / 50 - driftX;
            	if(calibrate == false)//if not currently callibrating
            	{
            		//yFIFOValues[tickNum] = y * factor/50 - driftY;
            		////sumY += y * factor/50 - driftY;
            		//sumY += y - driftY;
            		
            	}
		        else //if currently callibrating
		        {
		            //yFIFOValues[tickNum] = y * factor/50; 
		            ////sumY += y * factor/50;
		            //sumY += y;
		            yCalibValues[(int)tickNum%100] = y;
		        }
		        tickNum++;
		        break;
        }
        ////sumZ += z * factor / 50 - driftZ;
    }
    @Override
    public void robotInit() {
        setupGyro(BYPASSMODE);
    }   
     
    @Override
    public void disabledPeriodic() {
        tickNum = 0;
        if(justDisabled)
        {
            ////sumY = 0; think about this
            disabledCount = 0;
            justDisabled = false;
        }
        if(disabledCount>=50)//if robot loses connection on the field, it won't start calibrating
        {
           updateGyro(CALIBRATE, BYPASSMODE);//do calibrate
 
           driftY = calibrateGyro(); 
        }
        System.out.println("Tick num: " + tickNum);
        System.out.print("Y is ");
        System.out.println(y);
        System.out.print("DriftY is : ");
        System.out.println(driftY);
        disabledCount++;
    }
 
    @Override
    public void teleopPeriodic(){
//        if (!justDisabled && disabledCount > 50)
//      {
//          driftX = //sumX / disabledCount;
//          driftY = //sumY / disabledCount;
//          driftZ = //sumZ / disabledCount;
//          //sumX = 0;
//          //sumY = 0;
//          //sumZ = 0;
//      }
        updateGyro(DONT_CALIBRATE, BYPASSMODE);
 
        justDisabled = true;
        System.out.println("Tick num: " + tickNum);
        System.out.print("Y is ");
        System.out.print("Y is : ");
        System.out.println(y - driftY);
        System.out.print("DriftY is : ");
        System.out.println(driftY);
    }   
 
}