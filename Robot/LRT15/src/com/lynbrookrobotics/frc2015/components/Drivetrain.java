package com.lynbrookrobotics.frc2015.components;

import com.lynbrookrobotics.frc2015.config.DriverStationConfig;
import com.lynbrookrobotics.frc2015.driverstation.LRTDriverStation;
import com.lynbrookrobotics.frc2015.driverstation.LRTJoystick;
import com.lynbrookrobotics.frc2015.log.AsyncPrinter;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;

public class Drivetrain extends Component{
	
    static final int kFrontLeft_val = 0;
    static final int kFrontRight_val = 2;
    static final int kRearLeft_val = 1;
    static final int kRearRight_val = 3;
    
    LRTJoystick wheel, stick;
    
    RobotDrive testDrive;
	
    protected static final int kMaxNumberOfMotors = 4;
	
	
	public Drivetrain() 
	{
		super("Drivetrain", DriverStationConfig.DigitalIns.NO_DS_DI);
		
		wheel = LRTDriverStation.Instance().GetDriverWheel();
		stick = LRTDriverStation.Instance().GetDriverStick();
		
		testDrive = new RobotDrive(kFrontLeft_val, kRearLeft_val, kFrontRight_val, kRearRight_val);
		AsyncPrinter.info("Test Drivetrain Constructed");
	} 
	
	private static double limit(double limiter)
	{
		if(limiter > 1)
		{
			limiter = 1;
		} else if(limiter < -1)
		{
			limiter = -1;
		}
		return limiter;
	}
	
	

	@Override
	protected void UpdateEnabled() 
	{
		double twist = wheel.getAxis(AxisType.kX);
		double direction = Math.toDegrees(Math.atan2(stick.getAxis(AxisType.kX), - stick.getAxis(AxisType.kY) ));
		double magnitude = Math.sqrt(stick.getAxis(AxisType.kX) * stick.getAxis(AxisType.kX) 
				+ stick.getAxis(AxisType.kY) * stick.getAxis(AxisType.kY));
		
		testDrive.mecanumDrive_Polar(magnitude, direction, twist);
	}

	@Override
	protected void UpdateDisabled() 
	{

	}

	@Override
	protected void OnEnabled() 
	{	
	}

	@Override
	protected void OnDisabled() 
	{
	}
}
