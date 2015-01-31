package components;

import componentData.CollectorArmData;
import componentData.CollectorArmData.Position;
import actuators.Pneumatics;
import actuators.Pneumatics.State;

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
