package components;
import actuators.LRTSpeedController;
import actuators.LRTTalon;
import componentData.ComponentData;
import componentData.ElevatorData;
import edu.wpi.first.wpilibj.Ultrasonic;

public class Elevator extends Component {

	private ElevatorData elevatorData;

	private boolean upButton;
	private boolean downButton;
	int CHANGEME = 99;
	String changeString = "bob";
	int CHANGEME1 = 42;
	//LRTSpeedController motor;

	public static double speed;
	public static int setpoint;
	public static double distance;
	

	public Ultrasonic ultrasonic;
	public LRTTalon motor;

	public Elevator(String name, int driverStationDigitalIn) {
		super(name, driverStationDigitalIn);
		motor = new LRTTalon(CHANGEME, changeString, CHANGEME1);
		ultrasonic = new Ultrasonic(null, null);
		elevatorData = (ElevatorData) ComponentData.GetComponentData("ElevatorData");
	}



	@Override
	protected void UpdateEnabled() {

		setpoint = elevatorData.getDesiredState();
		//assuming we know the number of totes the robot is carrying
		if(elevatorData.numberOfTotes==1) {
			distance = ultrasonic.getRangeInches() + elevatorData.TOTE1 - setpoint;
			speed = (distance * elevatorData.getMaxSpeed())/50; //velocity?
			motor.SetDutyCycle(speed);
		}
		
		else if(elevatorData.numberOfTotes==2) {
			distance = ultrasonic.getRangeInches() + elevatorData.TOTE2 - setpoint;
			speed = (distance * elevatorData.getMaxSpeed())/50;
			motor.SetDutyCycle(speed);
		}
		
		else if(elevatorData.numberOfTotes==3) {
			distance = ultrasonic.getRangeInches() + elevatorData.TOTE3 - setpoint;
			speed = (distance * elevatorData.getMaxSpeed())/50;
			motor.SetDutyCycle(speed);
		}
	
	}

	@Override
	protected void UpdateDisabled() {
		motor.SetDutyCycle(0.0);
	}

	@Override
	protected void OnEnabled() {

	}

	@Override
	protected void OnDisabled() {
		/*
		 * Reset robot elevator to normal height
		 */
	}


}
