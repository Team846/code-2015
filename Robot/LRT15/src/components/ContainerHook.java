package components;

import componentData.ComponentData;
import componentData.ContainerHookData;
import actuators.Pneumatics;
import actuators.Pneumatics.State;

public class ContainerHook extends Component{
	
	private Pneumatics[] Solenoids;

	private ContainerHookData hook;
	private static final int CHANGEME = 42;
	public static final int solenoidLength = 6;
	
	public ContainerHook(String name, int driverStationDigitalIn) {
		super("ContainerHook", CHANGEME);
		hook = ContainerHookData.get();
		Solenoids[0] = new Pneumatics(CHANGEME, CHANGEME, "CHANGEME");
		for(int i = 1; i<solenoidLength; i++){
			Solenoids[i] = Solenoids[0];
		}
	}

	@Override
	protected void UpdateEnabled() {
		for(int i = 0; i<Solenoids.length; i++){
			Solenoids[i].Set(hook.getStateFromSolenoid(i), false);
		}
	}

	@Override
	protected void UpdateDisabled() {
		for(int i = 0; i<Solenoids.length; i++){
			Solenoids[i].Set(State.OFF, true);
		}
	}

	@Override
	protected void OnEnabled() {
		
	}

	@Override
	protected void OnDisabled() {
		
	}

}
