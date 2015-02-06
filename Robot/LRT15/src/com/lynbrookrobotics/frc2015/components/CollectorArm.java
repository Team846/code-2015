package com.lynbrookrobotics.frc2015.components;

import com.lynbrookrobotics.frc2015.actuators.Pneumatics;
import com.lynbrookrobotics.frc2015.actuators.Pneumatics.State;
import com.lynbrookrobotics.frc2015.componentData.CollectorArmData;
import com.lynbrookrobotics.frc2015.componentData.CollectorArmData.Position;
import com.lynbrookrobotics.frc2015.config.ConfigPortMappings;
import com.lynbrookrobotics.frc2015.config.DriverStationConfig;

public class CollectorArm extends Component 
{
	Pneumatics arms;
	CollectorArmData armData;
	
	public CollectorArm()
	{
		super("Collector", DriverStationConfig.DigitalIns.NO_DS_DI);
		
		 arms = new Pneumatics(
				 ConfigPortMappings.Instance().Get("Pneumatics/COLLECTOR_ARMS"), "CollectorArms");
		 armData = CollectorArmData.get();
	}

	@Override
	protected void UpdateEnabled() 
	{
		State state;
		
		if(armData.getDesiredCollectorState() == Position.EXTEND)
			state = State.FORWARD;
		else
			state = State.OFF;
		
		arms.Set(state);
		
	}

	@Override
	protected void UpdateDisabled() {
		arms.Set(State.OFF);
		
	}

	@Override
	protected void OnEnabled() {
		
	}

	@Override
	protected void OnDisabled() {
		
	}

}
