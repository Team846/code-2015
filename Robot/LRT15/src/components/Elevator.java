package components;
import actuators.LRTSpeedController;
import actuators.LRTTalon;
import componentData.ComponentData;
import componentData.ElevatorData;

public class Elevator extends Component {
	
	private ElevatorData elevatorData;
	
	private boolean upButton;
	private boolean downButton;
	int CHANGEME;
	String changeString;
	int CHANGEME1;
	LRTSpeedController motor;

	public Elevator(String name, int driverStationDigitalIn) {
		super(name, driverStationDigitalIn);
		LRTTalon motor = new LRTTalon(CHANGEME, changeString, CHANGEME1);
		elevatorData = (ElevatorData) ComponentData.GetComponentData("ElevatorData");
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void UpdateEnabled() {
		// TODO Auto-generated method stub
		/*
		 * Receiving commands
		 */

		if (upButton) {
			//move elevator up at desired speed		
			elevatorData.getState();
			elevatorData.getMaxSpeed();
			elevatorData.getCurrentMotion();
			//set motor according to data
		}
		else if (downButton) {
			//move elevator down at desired speed
			elevatorData.getState();
			elevatorData.getMaxSpeed();
			elevatorData.getCurrentMotion();
			//set motor according to data
		}
		else if (!upButton && !downButton) {
			//stops elevator from moving
			elevatorData.getState();
			elevatorData.getMaxSpeed();
			elevatorData.getCurrentMotion();
			//set motor according to data
		}
		
	}

	@Override
	protected void UpdateDisabled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void OnEnabled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void OnDisabled() {
		// TODO Auto-generated method stub
		/*
		 * Reset robot elevator to normal height
		 */
	}

}
