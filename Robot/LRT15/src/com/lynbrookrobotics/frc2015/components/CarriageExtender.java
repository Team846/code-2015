package com.lynbrookrobotics.frc2015.components;

import com.lynbrookrobotics.frc2015.componentData.CarriageExtenderData;
import com.lynbrookrobotics.frc2015.componentData.CarriageExtenderData.ControlMode;
import com.lynbrookrobotics.frc2015.componentData.CarriageExtenderData.Setpoint;
import com.lynbrookrobotics.frc2015.config.ConfigPortMappings;
import com.lynbrookrobotics.frc2015.config.ConfigRuntime;
import com.lynbrookrobotics.frc2015.config.Configurable;
import com.lynbrookrobotics.frc2015.config.DriverStationConfig;
import com.lynbrookrobotics.frc2015.log.AsyncPrinter;
import com.lynbrookrobotics.frc2015.sensors.SensorFactory;
import com.lynbrookrobotics.frc2015.utils.MathUtils;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;

public class CarriageExtender extends Component implements Configurable
{
	CANTalon motor;
	
	AnalogInput carriagePot;
	
	private int retractSetpoint;
	private int extendSetpoint;
	
	private int retractLimit;
	private int extendLimit;
	
	private double positionGain;
	
	private CarriageExtenderData extenderData;

	public CarriageExtender()
	{
		super("CarriageExtender", DriverStationConfig.DigitalIns.NO_DS_DI);
		
		carriagePot = SensorFactory.GetAnalogInput(
				ConfigPortMappings.Instance().Get("Analog/CARRIAGE_POT"));
		
		motor = new CANTalon(ConfigPortMappings.Instance().Get("CAN/CARRIAGE"));
		
		positionGain = retractSetpoint = extendSetpoint = retractLimit = extendLimit = 0;
		
		extenderData = CarriageExtenderData.get();	
		ConfigRuntime.Register(this);
	}

	@Override
	protected void UpdateEnabled() 
	{
		int position = carriagePot.getAverageValue();
		if(position <= retractLimit || position >= extendLimit)
		{
			motor.set(0.0);
			AsyncPrinter.error("Carriage out of bounds! Disable and fix");
			return;
		}
		
		if(extenderData.getControlMode() == ControlMode.AUTOMATED)
		{
			if(extenderData.getAutomatedSetpoint() == Setpoint.RETRACT)
				motor.set((retractSetpoint - position) * positionGain);
			else
				motor.set((extendSetpoint - position) * positionGain);
		}
			
		else if(extenderData.getControlMode() == ControlMode.MANUAL_POSITION)//Manual mode
		{
			
			int desiredPos = (int) Rescale(extenderData.getDesiredPositionSetpoint(), 0, 1, retractSetpoint, extendSetpoint);
			motor.set((desiredPos - position)*positionGain);
		}
		else
			motor.set(extenderData.getSpeed()); //open loop velocity
		
		extenderData.setCurrentPosition(Rescale(position, retractSetpoint, extendSetpoint, 0, 1));
		
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
		
		retractLimit = GetConfig("retractLimit", 5);
		extendLimit = GetConfig("extendLimit", 10);
		
		positionGain = GetConfig("positionGain", 1.0);
		
	}
	
	//TODO: put into math util
	private static float Rescale(float val, float min0, float max0, float min1, float max1)
	{
		if (max0 == min0)
			return min1;
		val = MathUtils.clamp(val, min0, max0);
		return (val - min0) * (max1 - min1) / (max0 - min0) + min1;
	}

}
