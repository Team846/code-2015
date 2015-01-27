package components;

import actuators.LRTCANTalon;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;

public class Drivetrain extends Component{
	
	
	
	private double SPEED;
	private double DIRECTION;
	private double ROTATION;

    protected final int m_invertedMotors[] = new int[4];
	
    static final int kFrontLeft_val = 0;
    static final int kFrontRight_val = 1;
    static final int kRearLeft_val = 2;
    static final int kRearRight_val = 3;
    
    protected double m_maxOutput = 1.0;
    
	LRTCANTalon m_frontLeftMotor = new LRTCANTalon("CHANGEME", kFrontLeft_val);
	LRTCANTalon m_frontRightMotor = new LRTCANTalon("CHANGEME", kFrontRight_val);
	LRTCANTalon m_rearLeftMotor = new LRTCANTalon("CHANGEME", kRearLeft_val);
	LRTCANTalon m_rearRightMotor = new LRTCANTalon("CHANGEME", kRearRight_val);
	
    protected static final int kMaxNumberOfMotors = 4;
	
	
	public Drivetrain(String CHANGEME, int driverStationDigitalIn) 
	{
		super(CHANGEME, driverStationDigitalIn);
		// TODO Auto-generated constructor stub
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
		
		// TODO Auto-generated method stub
		
	        // Normalized for full power along the Cartesian axes.
	        SPEED = limit(SPEED) * Math.sqrt(2.0);
	        // The rollers are at 45 degree angles.
	        double dirInRad = (DIRECTION + 45.0) * Math.PI / 180.0;
	        double cosD = Math.cos(dirInRad);
	        double sinD = Math.sin(dirInRad);

	        double wheelSpeeds[] = new double[kMaxNumberOfMotors];
	        wheelSpeeds[kFrontLeft_val] = (sinD * SPEED + ROTATION);
	        wheelSpeeds[kFrontRight_val] = (cosD * SPEED - ROTATION);
	        wheelSpeeds[kRearLeft_val] = (cosD * SPEED + ROTATION);
	        wheelSpeeds[kRearRight_val] = (sinD * SPEED - ROTATION);
	        
	        for(int n = 0; n < wheelSpeeds.length ; n++){
	        	wheelSpeeds[n] = limit(wheelSpeeds[n]);
	        }

	        byte syncGroup = (byte)0x80;
	        	
	        // TODO: Implement wrapper class for motors
	        m_frontLeftMotor.SetDutyCycle(wheelSpeeds[kFrontLeft_val] * m_invertedMotors[kFrontLeft_val] * m_maxOutput);
	        m_frontRightMotor.SetDutyCycle(wheelSpeeds[kFrontRight_val] * m_invertedMotors[kFrontRight_val] * m_maxOutput);
	        m_rearLeftMotor.SetDutyCycle(wheelSpeeds[kRearLeft_val] * m_invertedMotors[kRearLeft_val] * m_maxOutput);
	        m_rearRightMotor.SetDutyCycle(wheelSpeeds[kRearRight_val] * m_invertedMotors[kRearRight_val] * m_maxOutput);

	}

	@Override
	protected void UpdateDisabled() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void OnEnabled() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void OnDisabled() 
	{
		// TODO Auto-generated method stub
		
	}
}
