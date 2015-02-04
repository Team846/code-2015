package com.lynbrookrobotics.frc2015.components;

import com.lynbrookrobotics.frc2015.actuators.Pneumatics;
import com.lynbrookrobotics.frc2015.actuators.Pneumatics.State;
import com.lynbrookrobotics.frc2015.componentData.CollectorArmData;
import com.lynbrookrobotics.frc2015.componentData.CollectorArmData.Position;

public class CollectorArm extends Component 
{
	private final static int CHANGEME = 22;
	Pneumatics arms = new Pneumatics(1, "CollectorArm");
	CollectorArmData armData = CollectorArmData.get();
	
	public CollectorArm()
	{
		super("Collector", CHANGEME);
	}

	@Override
	protected void UpdateEnabled() 
	{
		State state;
		
		if(armData.getDesiredCollectorState() == Position.EXTEND)
			state = State.FORWARD;
		else
			state = State.OFF;
		
		arms.Set(state, false);
		
	}

	@Override
	protected void UpdateDisabled() {
		arms.Set(State.OFF, false);
		
	}

	@Override
	protected void OnEnabled() {
		
	}

	@Override
	protected void OnDisabled() {
		
	}

}
