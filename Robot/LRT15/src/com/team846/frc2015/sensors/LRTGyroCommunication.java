package com.team846.frc2015.sensors;

/* DO NOT DELETE THIS COMMENT.
this is the gyro communications class for the gyro L3GD20H by adafruit. Datasheet  http://www.adafruit.com/datasheets/L3GD20H.pdf
This is a digital gyro.
Because the class LRTGyro should be able to use this class regardless of what gyro we use, All future digital gyro communication classes must have the following methods:
private void setupGyroCommunciation(int mode)
public void updateGyro(boolean calibrate, int streamOrBypass, double driftY)
public double getYVel()
If the gyro does not have an inbedded queue or stack, use BYPASSMODE. For analog gyros, comment out the updateGyro() method in  the LRTGyro class, but DO NOT DELETE IT!!!
Make sure that all state and mode variables that are ued in both LRTGyro and LRTGyroCommunication are EXACTLY THE SAME
*/
import com.team846.frc2015.driverstation.GameState;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SPI.Port;

import java.util.Arrays;

public class LRTGyroCommunication extends SensorFactory
{
    SPI gyro;

    private final byte L3GD20_REGISTER_OUT_X_L   = 0x28;
    private final byte L3GD20_REGISTER_CTRL_REG1 = 0x20;
    private final byte L3GD20_REGISTER_CTRL_REG4 = 0x23;
    private final byte L3GD20_REGISTER_CTRL_REG5 = 0x24;
    private final byte L3GD20_REGISTER_FIFO_CTRL = 0x2E;
    private final byte L3GD20_REGISTER_FIFO_SRC  = 0x2F;

    private final int BYPASSMODE = 0;
    private final int STREAMMODE = 1;
    private final int mode = STREAMMODE;

    final double conversionFactor = 0.070F;

    public static final boolean CALIBRATE = true;
    public static final boolean DONT_CALIBRATE = false;


    byte[] inputFromSlave = new byte[7];
    byte[] outputToSlave  = new byte[7];

    double yVel = 0;
    double yFIFO = 0;

    double yFIFOValues[] = new double[32];//gyro queue only stores 32 values

    long tickNum = 0;

    private static LRTGyroCommunication instance = null;

    private LRTGyroCommunication() //initialization code, ensures there is only one gyro communication object
    {
        setupGyroCommunciation(mode);
    }

    public static LRTGyroCommunication getInstance()
    {
        if(instance == null)//ensures that there is only one gyro object
        {
            instance = new LRTGyroCommunication();
        }

        return instance;
    }

    private void setupGyroCommunciation(int mode)
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
        out[1] = (byte) (0b11001010);//byte to disable x and z axis
        byte[] in = new byte[2];
        gyro.transaction(out, in, 2);

        out = new byte[2];
        out[0] = (byte) (L3GD20_REGISTER_CTRL_REG4);
        out[1] = (byte) (0b00110000);//set sensitivity
        in = new byte[2];
        gyro.transaction(out, in, 2);

        if(mode==STREAMMODE)
        {
            out[0] = (byte) (L3GD20_REGISTER_CTRL_REG5);
            out[1] = (byte) 0b01000000;//byte to enable FIFO
            gyro.transaction(out, in, 2);

            out[0] = (byte) (L3GD20_REGISTER_FIFO_CTRL);
            out[1] = (byte) (0b01000000);//byte that sets to stream mode
            gyro.transaction(out, in, 2);
        }

    }

    public void updateGyro(boolean calibrate, int streamOrBypass, double driftY)
    {
        switch(streamOrBypass)
        {
            case STREAMMODE:
                if(isFIFOFull() == true)
                {
                    int FIFOCount = 0;

                    for(int i =0; i<yFIFOValues.length; i++)
                    {
                        Arrays.fill(outputToSlave, (byte)0x00);

                        outputToSlave[0] = setByte(outputToSlave[0], L3GD20_REGISTER_OUT_X_L, (byte)0b10000000, (byte)0b01000000);//do not change

                        gyro.transaction(outputToSlave, inputFromSlave, 7);

                        yFIFO = ((inputFromSlave[3] & 0xFF) | (inputFromSlave[4] << 8)) * conversionFactor;

                        if(!calibrate)//if not currently calibrating
                        {
                            yFIFOValues[FIFOCount] = yFIFO - driftY;
                        }
                        else //if currently calibrating
                        {
                            yFIFOValues[(int)FIFOCount] = yFIFO;
                        }

                        FIFOCount++;

                    }

                    yVel = average(yFIFOValues);

                }

                break;
            case BYPASSMODE:
                Arrays.fill(outputToSlave, (byte)0b00000000);

                outputToSlave[0] = L3GD20_REGISTER_OUT_X_L | (byte)0x80 | (byte)0x40;
                gyro.transaction(outputToSlave, inputFromSlave, 7);

                yVel = (short)((inputFromSlave[3] & 0xFF) | (inputFromSlave[4] << 8)) *  conversionFactor;

                if(!calibrate)//if not currently calibrating
                {
                    yVel = yVel - driftY;
                }

                break;
        }

    }

    private boolean isFIFOFull() //TODO: Check if correct
    {
        byte[] outputToSlave = new byte[2];//from gyro
        outputToSlave[0] = setByte(L3GD20_REGISTER_FIFO_CTRL, (byte) 0b10000000); // set bit 0 (READ bit) to 1 (pg. 31)
        byte[] inputFromSlave = new byte[2];

        gyro.transaction(outputToSlave, inputFromSlave, 2);


        if (((inputFromSlave[1] >> 6) & 0b1) == 0b1)//if second bit from the left is 1 then it is full
        {
            return true;
        }

        return false;
    }

    //All methods below are abstraction methods
    public double getYVel()
    {
        return yVel;
    }

    private byte setByte(byte toSet, byte... modifiers)
    {
        toSet = modifiers[0];

        for(int i =0; i<modifiers.length; i++)//for each loop
        {
            toSet = (byte) (toSet | modifiers[i]);
        }

        return toSet;
    }


    private double average(double[] yFIFOValues2)
    {
        double sum = 0;

        for(int i = 0; i<yFIFOValues.length; i++)
        {
            sum += yFIFOValues[i];
        }
        return sum / yFIFOValues.length;
    }

}
