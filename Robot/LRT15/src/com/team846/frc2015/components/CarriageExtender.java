package com.team846.frc2015.components;

import com.team846.frc2015.componentData.CarriageExtenderData;
import com.team846.frc2015.componentData.CarriageExtenderData.CarriageControlMode;
import com.team846.frc2015.componentData.CarriageExtenderData.Setpoint;
import com.team846.frc2015.config.ConfigPortMappings;
import com.team846.frc2015.config.ConfigRuntime;
import com.team846.frc2015.config.Configurable;
import com.team846.frc2015.config.DriverStationConfig;
import com.team846.frc2015.dashboard.DashboardLogger;
import com.team846.frc2015.sensors.SensorFactory;
import com.team846.frc2015.utils.AsyncPrinter;
import com.team846.frc2015.utils.MathUtils;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.StatusFrameRate;

public class CarriageExtender extends Component implements Configurable
{
	CANTalon carriageMotor;
	
	AnalogInput carriagePot;
	
	private int retractSetpoint;
	private int extendSetpoint;
	
	private int retractSoftLimit;
	private int extendSoftLimit;
	
	private double maxRange;
	
	private double maxSpeed;
	
	private double positionGain;
	private int errorThreshold;
	
	private CarriageExtenderData extenderData;

	public CarriageExtender()
	{
		super("CarriageExtender", DriverStationConfig.DigitalIns.NO_DS_DI);
		
		carriagePot = SensorFactory.GetAnalogInput(
				ConfigPortMappings.Instance().get("Analog/CARRIAGE_POT"));
		
		carriageMotor = new CANTalon(ConfigPortMappings.Instance().get("CAN/CARRIAGE_MOTOR"));
		//carriageMotor.setStatusFrameRateMs(StatusFrameRate.AnalogTempVbat, 20);
		
		maxRange = positionGain = errorThreshold = retractSetpoint = extendSetpoint = retractSoftLimit = extendSoftLimit =  0;
		
		extenderData = CarriageExtenderData.get();	
		
		ConfigRuntime.Register(this);
	}

	@Override
	protected void UpdateEnabled() 
	{
		//AsyncPrinter.println("CarriageExtender Position: " + carriagePot.getAverageValue());
		int position = carriagePot.getAverageValue();
		
		
		if(extenderData.getControlMode() == CarriageControlMode.SETPOINT)
		{
			double error = 0.0;
			if(extenderData.getSetpoint() == Setpoint.RETRACT)
				 error = Math.abs(retractSetpoint - position) <  errorThreshold ? 0.0 : (retractSetpoint - position ) / maxRange;
			else
				 error = Math.abs(extendSetpoint - position) <  errorThreshold ? 0.0 : (extendSetpoint - position) / maxRange;
				
			carriageMotor.set(error * positionGain);
		}
		else if(extenderData.getControlMode() == CarriageControlMode.POSITION)
		{
			int desiredPos = (int) Rescale(extenderData.getPositionSetpoint(), 0, 1, retractSetpoint, extendSetpoint);
			DashboardLogger.getInstance().logInt("extender-desiredPos", desiredPos);
			double error = Math.abs(desiredPos - position) <  errorThreshold ? 0.0 : ( desiredPos - position ) / maxRange ;
			carriageMotor.set(MathUtils.clamp(error*positionGain, -extenderData.getMaxSpeed(), extenderData.getMaxSpeed()));
		}
		else
			carriageMotor.set(extenderData.getSpeed()); //open loop velocity
		
		extenderData.setCurrentPosition((int)Rescale(position, retractSetpoint, extendSetpoint, 0, 1));
		
	}

	@Override
	protected void UpdateDisabled() {
		carriageMotor.set(0.0);
		AsyncPrinter.println("CarriageExtender Position: " + carriagePot.getAverageValue());
	}

	@Override
	protected void OnEnabled() {
	}

	@Override
	protected void OnDisabled() {
	}
	
	private void sendOutput(double value)
	{
		int position = carriagePot.getAverageValue();
		if(position <= extendSoftLimit)
		{
			if (value > 0)
				value = 0;
		}
		else if(position >= retractSoftLimit)
		{
			if (value < 0)
				value = 0;
		}
		carriageMotor.set(value);
	}

	@Override
	public void Configure()
	{
		retractSetpoint = GetConfig("retractSetpoint", 10);
		extendSetpoint = GetConfig("extendSetpoint", 200);
		
		retractSoftLimit = GetConfig("retractSoftLimit", 10);
		extendSoftLimit = GetConfig("extendSoftLimit", 1000);
		
		errorThreshold = GetConfig("errorThreshold", 15);
		
		positionGain = GetConfig("positionGain", 1.0);
		
		maxSpeed = GetConfig("maxSpeed",0.8);
		
		maxRange = extendSetpoint - retractSetpoint;
		
	}
	//TODO: put into math util
	private static double Rescale(double d, double min0, double max0, double min1, double max1)
	{
		if (max0 == min0)
			return min1;
		d = MathUtils.clamp(d, min0, max0);
		return (d - min0) * (max1 - min1) / (max0 - min0) + min1;
	}
}
