package com.team846.frc2015.components;

import com.team846.frc2015.actuators.Pneumatics;
import com.team846.frc2015.actuators.Pneumatics.State;
import com.team846.frc2015.componentData.CollectorArmData;
import com.team846.frc2015.componentData.CollectorArmData.ArmPosition;
import com.team846.frc2015.config.ConfigPortMappings;
import com.team846.frc2015.config.DriverStationConfig;

public class CollectorArms extends Component 
{
	Pneumatics arms;
	CollectorArmData armData;
	
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
		armData.setCurrentCollectorPosition(state == State.OFF ? ArmPosition.STOWED : ArmPosition.EXTEND);
		
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
