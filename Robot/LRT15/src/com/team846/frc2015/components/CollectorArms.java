package com.team846.frc2015.components;

import com.team846.frc2015.actuators.Pneumatics;
import com.team846.frc2015.actuators.Pneumatics.State;
import com.team846.frc2015.componentData.CollectorArmData;
import com.team846.frc2015.componentData.CollectorArmData.ArmPosition;
import com.team846.frc2015.config.ConfigPortMappings;
import com.team846.frc2015.config.DriverStationConfig;
import com.team846.frc2015.sensors.SensorFactory;
import com.team846.frc2015.utils.AsyncPrinter;

import edu.wpi.first.wpilibj.AnalogInput;

public class CollectorArms extends Component 
{
	Pneumatics arms;
	CollectorArmData armData;
	AnalogInput sensor = SensorFactory.GetAnalogInput(ConfigPortMappings.Instance().get("Analog/COLLECTOR_PROXIMITY"));
	
	public CollectorArms()
	{
		super("CollectorArms", DriverStationConfig.DigitalIns.NO_DS_DI);
		
		 arms = new Pneumatics(
				 ConfigPortMappings.Instance().get("Pneumatics/COLLECTOR_ARMS"), "CollectorArms");
		 
		 armData = CollectorArmData.get();
	}

	@Override
	protected void UpdateEnabled() 
	{
		State state;
		
		if(armData.getDesiredCollectorPosition() == ArmPosition.EXTEND)
			state = State.FORWARD;
		else
			state = State.OFF;
		
		arms.set(state);
		armData.setCurrentCollectorPosition(state == State.FORWARD ? ArmPosition.EXTEND : ArmPosition.STOWED);
		AsyncPrinter.println("Prox value: " + sensor.getAverageValue() );
		
	}

	@Override
	protected void UpdateDisabled() {
		arms.set(State.OFF);
		
		
	}

	@Override
	protected void OnEnabled() {
		
	}

	@Override
	protected void OnDisabled() {
		
	}

}
