package com.team846.frc2015.components;

import com.team846.frc2015.actuators.Pneumatics;
import com.team846.frc2015.actuators.Pneumatics.PneumaticState;
import com.team846.frc2015.componentData.CollectorArmData;
import com.team846.frc2015.componentData.CollectorArmData.ArmPosition;
import com.team846.frc2015.config.ConfigPortMappings;
import com.team846.frc2015.config.DriverStationConfig;

import edu.wpi.first.wpilibj.AnalogInput;

public class CollectorArms extends Component 
{
	private final Pneumatics arms;
	private final CollectorArmData armData;
//	DigitalInput leftReed;
//	DigitalInput rightReed;
	AnalogInput sensor;
	
	public CollectorArms()
	{
		
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
		PneumaticState state;
		
		if(armData.getDesiredCollectorPosition() == ArmPosition.EXTEND)
			state = PneumaticState.FORWARD;
		else
			state = PneumaticState.OFF;
		
		arms.set(state);
		armData.setCurrentCollectorPosition(state == PneumaticState.FORWARD ? ArmPosition.EXTEND : ArmPosition.STOWED);
		
//		if(leftReed.get() || rightReed.get())
//			armData.setCurrentCollectorPosition( ArmPosition.EXTEND);
//		else
//			armData.setCurrentCollectorPosition( ArmPosition.STOWED);
		
	//	AsyncPrinter.println("Prox value: " + sensor.getAverageValue() );
		
	}

	@Override
	protected void UpdateDisabled() {
		arms.set(PneumaticState.OFF);
		
		//AsyncPrinter.println("Prox value: " + sensor.getAverageValue() );

	}

	@Override
	protected void OnEnabled() {
		
	}

	@Override
	protected void OnDisabled() {
		
	}

}