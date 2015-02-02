package components;

import componentData.CarriageHooksData;
import componentData.CarriageHooksData.Position;
import config.ConfigPortMappings;
import actuators.Pneumatics;
import actuators.Pneumatics.State;

public class CarriageHooks extends Component{

	Pneumatics frontHooks = new Pneumatics(
			ConfigPortMappings.Instance().Get("Pneumatics/FORWARD_HOOKS"), "CarriageHooks");
	Pneumatics backHooks = new Pneumatics(
			ConfigPortMappings.Instance().Get("Pneumatics/REVERSE_HOOKS"), "CarriageHooks");
	CarriageHooksData hooksData = CarriageHooksData.get();
	
	public CarriageHooks(String name, int driverStationDigitalIn) {
		super("CarriageHooks", driverStationDigitalIn);
	}

	@Override
	protected void UpdateEnabled() {
		State state;
		
		if (hooksData.getFrontHooksDesiredState() == Position.ENABLED)
			state = State.FORWARD;
		else
			state = State.FORWARD;
		
		
		if (hooksData.getBackHooksDesiredState() == Position.ENABLED)
			state = State.FORWARD;
		else
			state = State.FORWARD;
		
		frontHooks.Set(state, false);
		backHooks.Set(state, false);
	}

	@Override
	protected void UpdateDisabled() {
		frontHooks.Set(State.FORWARD, false);
		backHooks.Set(State.FORWARD, false);
	}

	@Override
	protected void OnEnabled() {
		
	}

	@Override
	protected void OnDisabled() {
		
	}

}
