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
	int CHANGEME;
	String changeString;
	int CHANGEME1;
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
		// TODO Auto-generated constructor stub
	}

	public double distanceToTop(){
		return ultrasonic.getRangeMM();
	}
	
	public void toTheTop(){
		speed = elevatorData.toTheTop();
		motor.SetDutyCycle(speed);
	}

	@Override
	protected void UpdateEnabled() {
		// TODO Auto-generated method stub
		/*
		 * Receiving commands
		 */

		setpoint = elevatorData.getState();
		if(elevatorData.numberOfTotes==1){
			distance = ultrasonic.getRangeInches() + elevatorData.TOTE1 - setpoint;
			speed = (distance * elevatorData.getMaxSpeed())/50;
			motor.SetDutyCycle(speed);
		}
		
		else if(elevatorData.numberOfTotes==2){
			distance = ultrasonic.getRangeInches() + elevatorData.TOTE2 - setpoint;
			speed = (distance * elevatorData.getMaxSpeed())/50;
			motor.SetDutyCycle(speed);
		}
		
		else if(elevatorData.numberOfTotes==3){
			distance = ultrasonic.getRangeInches() + elevatorData.TOTE3 - setpoint;
			speed = (distance * elevatorData.getMaxSpeed())/50;
			motor.SetDutyCycle(speed);
		}
		
		/*distance = ultrasonic.getRangeInches() - setpoint;
		speed = (distance * elevatorData.getMaxSpeed())/50;
		motor.SetDutyCycle(speed);*/
	
		
		
		/*if () {
			elevatorData.distance = ultrasonic.getRangeInches() - elevatorData.getState();
			speed = (elevatorData.distance * elevatorData.getMaxSpeed())/50;
			motor.SetDutyCycle(speed);
		}
		else if (downButton) {
			elevatorData.distance = ultrasonic.getRangeInches() - elevatorData.getState();
			speed = (elevatorData.distance * elevatorData.getMaxSpeed())/50;
			motor.SetDutyCycle(speed);
		}
		else if (!upButton && !downButton) {
			motor.SetDutyCycle(0.0);
			elevatorData.getState();
			elevatorData.getMaxSpeed();
			elevatorData.getCurrentMotion();
		}*/
	}

	@Override
	protected void UpdateDisabled() {
		// TODO Auto-generated method stub	
		motor.SetDutyCycle(0.0);
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
