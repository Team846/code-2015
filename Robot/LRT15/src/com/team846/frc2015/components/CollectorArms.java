package com.team846.frc2015.components;

import com.team846.frc2015.actuators.Pneumatics;
import com.team846.frc2015.actuators.Pneumatics.State;
import com.team846.frc2015.componentData.CollectorArmData;
import com.team846.frc2015.componentData.CollectorArmData.ArmPosition;
import com.team846.frc2015.config.ConfigPortMappings;
import com.team846.frc2015.config.DriverStationConfig;

import edu.wpi.first.wpilibj.AnalogInput;

public class CollectorArms extends Component 
{
	private Pneumatics arms;
	private CollectorArmData armData;
//	DigitalInput leftReed;
//	DigitalInput rightReed;
	AnalogInput sensor;
	
	public CollectorArms()
	{
		super("CollectorArms", DriverStationConfig.DigitalIns.NO_DS_DI);
		
		 arms = new Pneumatics(
				 ConfigPortMappings.Instance().get("Pneumatics/COLLECTOR_ARMS"), "CollectorArms");
		 
		 armData = CollectorArmData.get();
		 
//		 leftReed = SensorFactory.GetDigitalInput(
//				 ConfigPortMappings.Instance().get("Digital/LEFT_REED"));
//		 rightReed = SensorFactory.GetDigitalInput(
//				 ConfigPortMappings.Instance().get("Digital/RIGHT_REED"));
		 
//		 sensor = SensorFactory.GetAnalogInput(
//				 ConfigPortMappings.Instance().get("Analog/COLLECTOR_PROXIMITY"));
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
		
//		if(leftReed.get() || rightReed.get())
//			armData.setCurrentCollectorPosition( ArmPosition.EXTEND);
//		else
//			armData.setCurrentCollectorPosition( ArmPosition.STOWED);
		
	//	AsyncPrinter.println("Prox value: " + sensor.getAverageValue() );
		
	}

	@Override
	protected void UpdateDisabled() {
		arms.set(State.OFF);
		
		//AsyncPrinter.println("Prox value: " + sensor.getAverageValue() );

	}

	@Override
	protected void OnEnabled() {
		
	}

	@Override
	protected void OnDisabled() {
		
	}

}
