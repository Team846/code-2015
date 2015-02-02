package components;

import componentData.CarriageHooksData;
import componentData.CarriageHooksData.Position;
import actuators.Pneumatics;
import actuators.Pneumatics.State;

public class CarriageHooks extends Component{

	Pneumatics frontHooks = new Pneumatics(1, "CarriageHooks");
	Pneumatics backHooks = new Pneumatics(1, "CarriageHooks");
	CarriageHooksData hooksData = CarriageHooksData.get();
	
	public CarriageHooks(String name, int driverStationDigitalIn) {
		super("CarriageHooks", driverStationDigitalIn);
	}

	@Override
	protected void UpdateEnabled() {
		State state;
		
		if (hooksData.getFrontHooksDesiredState() == Position.ENABLED)
			state = State.FORWARD;
		else if (hooksData.getFrontHooksDesiredState() == Position.DISABLED)
			state = State.REVERSE;
		else
			state = State.FORWARD;
		
		frontHooks.Set(state, false);
		
		if (hooksData.getBackHooksDesiredState() == Position.ENABLED)
			state = State.FORWARD;
		else if (hooksData.getBackHooksDesiredState() == Position.DISABLED)
			state = State.REVERSE;
		else
			state = State.FORWARD;
		
		backHooks.Set(state, false);
		
		
		
		
	}

	@Override
	protected void UpdateDisabled() {
		frontHooks.Set(State.FORWARD, false);
		
	}

	@Override
	protected void OnEnabled() {
		
	}

	@Override
	protected void OnDisabled() {
		
	}

}
