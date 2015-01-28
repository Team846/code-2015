package componentData;

import edu.wpi.first.wpilibj.Ultrasonic;

public class ElevatorData extends ComponentData{

	public static int maxSPEED = 1;

	public static double speed = 0.0;

	public static final int STAY = 1;
	public static final int UP = 2;
	public static final int DOWN= 3;
	private static int motion = 1;
	public static double distance;
	public static int numberOfTotes;

	public static final int GROUND = 0;
	public final int TOTE1 = 12;
	public final int TOTE2 = 24;
	public final int TOTE3 = 36;
	//public final int TOTE4 = 48;
	//public final int TOTE5 = 60;
	//public final int TOTE6 = 72;
	
	
	private static int state = 1;

	public Ultrasonic ultrasonic;

	public ElevatorData() {
		super("ElevatorData");
		// TODO Auto-generated constructor stub
	}

	//public int getNumberTotes(){
//		return ;
	//}
	
	public int getCurrentMotion(){
		return motion; 
	}

	public int setDesiredMotion(int i){
		if(i == STAY || i == UP || i == DOWN){
			motion = i;
		}
		return motion; 
	}

	public int getMaxSpeed(){
		return maxSPEED;
	}

	public int setMaxSpeed(int desiredSpeed){
		maxSPEED = desiredSpeed;
		return maxSPEED;
	}

	public double getSpeed(){
		//speed = setSpeed();
		return speed;
	}

	public double setSpeed(double desiredSpeed){
		speed = desiredSpeed;
		return speed;
	}

	public int getState(){
		return state;
	}

	public int setDesiredState(int i){
		if(i == GROUND || i == TOTE1 || i == TOTE2 || i == TOTE3){
			state = i;
		}
		return state;
	}

	public double toTheTop(){
		setDesiredState(TOTE3);
		setDesiredMotion(UP);
		//speed = setSpeed();
		return speed;
	}

	@Override
	protected void ResetCommands() {
		// TODO Auto-generated method stub

	}

}
