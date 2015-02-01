package components;

import componentData.CarriageExtenderData;
import componentData.CarriageExtenderData.ControlMode;
import componentData.CarriageExtenderData.Setpoint;
import sensors.SensorFactory;
import config.ConfigPortMappings;
import config.Configurable;
import config.DriverStationConfig;
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

	public CarriageExtender(String name, int driverStationDigitalIn) {
		super("CarriageExtender", DriverStationConfig.DigitalIns.NO_DS_DI);
		carriagePot = SensorFactory.GetAnalogInput(
				ConfigPortMappings.Instance().Get("Analog/CARRIAGE_POT"));
		positionGain = retractSetpoint = extendSetpoint = retractLimit = extendLimit = 0;
		motor = new CANTalon(ConfigPortMappings.Instance().Get("CAN/CARRIAGE"));
		extenderData = CarriageExtenderData.get();
	}

	@Override
	protected void UpdateEnabled() 
	{
		int position = carriagePot.getAverageValue();
		if(position <= retractLimit || position >= extendLimit)
		{
			motor.set(0.0);
			System.out.println("[WARNING] Carriage out of bounds! Disable and fix");
			return;
		}
		
		if(extenderData.getControlMode() == ControlMode.AUTOMATED)
		{
			if(extenderData.getSetpoint() == Setpoint.RETRACT)
				motor.set((retractSetpoint - position) * positionGain);
			else
				motor.set((extendSetpoint - position) * positionGain);
		}
			
		else //Manual mode
			motor.set(extenderData.getCarriageSpeed());
			
		
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

}
