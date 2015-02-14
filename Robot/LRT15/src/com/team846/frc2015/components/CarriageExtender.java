package com.team846.frc2015.components;

import com.team846.frc2015.componentData.CarriageExtenderData;
import com.team846.frc2015.componentData.CarriageExtenderData.ControlMode;
import com.team846.frc2015.componentData.CarriageExtenderData.Setpoint;
import com.team846.frc2015.config.ConfigPortMappings;
import com.team846.frc2015.config.ConfigRuntime;
import com.team846.frc2015.config.Configurable;
import com.team846.frc2015.config.DriverStationConfig;
import com.team846.frc2015.log.AsyncPrinter;
import com.team846.frc2015.sensors.SensorFactory;
import com.team846.frc2015.utils.MathUtils;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;

public class CarriageExtender extends Component implements Configurable
{
	CANTalon motor;
	
	AnalogInput carriagePot;
	
	private int retractSetpoint;
	private int extendSetpoint;
	
	private int retractSoftLimit;
	private int extendSoftLimit;
	
	private double positionGain;
	private int errorThreshold;
	
	private CarriageExtenderData extenderData;

	public CarriageExtender()
	{
		super("CarriageExtender", DriverStationConfig.DigitalIns.NO_DS_DI);
		
		carriagePot = SensorFactory.GetAnalogInput(
				ConfigPortMappings.Instance().get("Analog/CARRIAGE_POT"));
		
		motor = new CANTalon(ConfigPortMappings.Instance().get("CAN/CARRIAGE_MOTOR"));
		
		positionGain = errorThreshold = retractSetpoint = extendSetpoint = retractSoftLimit = extendSoftLimit = 0;
		
		extenderData = CarriageExtenderData.get();	
		ConfigRuntime.Register(this);
	}

	@Override
	protected void UpdateEnabled() 
	{
		int position = carriagePot.getAverageValue();
		
		if(position <= retractSoftLimit || position >= extendSoftLimit)
		{
			motor.set(0.0);
			AsyncPrinter.error("Carriage out of bounds! Disable and fix");
			return;
		}
		
		if(extenderData.getControlMode() == ControlMode.AUTOMATED)
		{
			if(extenderData.getAutomatedSetpoint() == Setpoint.RETRACT)
			{
				double error = Math.abs(retractSetpoint - position) <  errorThreshold ? 0.0 : retractSetpoint - position ;
				motor.set(error * positionGain);
			}
			else
			{
				double error = Math.abs(extendSetpoint - position) <  errorThreshold ? 0.0 : extendSetpoint - position ;
				motor.set(error * positionGain);
			}
		}
			
		else if(extenderData.getControlMode() == ControlMode.POSITION)
		{
			
			int desiredPos = (int) Rescale(extenderData.getPositionSetpoint(), 0, 1, retractSetpoint, extendSetpoint);
			double error = Math.abs(desiredPos - position) <  errorThreshold ? 0.0 : desiredPos - position ;
			motor.set(error*positionGain);
		}
		else
			motor.set(extenderData.getSpeed()); //open loop velocity
		
		extenderData.setCurrentPosition((int)Rescale(position, retractSetpoint, extendSetpoint, 0, 1));
		
	}

	@Override
	protected void UpdateDisabled() {
		motor.set(0.0);
		
	}

	@Override
	protected void OnEnabled() {
	}

	@Override
	protected void OnDisabled() {
	}

	@Override
	public void Configure()
	{
		retractSetpoint = GetConfig("retractSetpoint", 10);
		extendSetpoint = GetConfig("extendSetpoint", 200);
		
		retractSoftLimit = GetConfig("retractSoftLimit", 10);
		extendSoftLimit = GetConfig("extendSoftLimit", 1000);
		
		errorThreshold = GetConfig("errorThreshold", 5);
		
		positionGain = GetConfig("positionGain", 1.0);
		
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
