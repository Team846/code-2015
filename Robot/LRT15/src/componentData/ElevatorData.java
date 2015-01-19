package componentData;

public class ElevatorData extends ComponentData{
	
	public static int SPEED = 5;
	
	public static final int STAY = 1;
	public static final int UP = 2;
	public static final int DOWN= 3;
	private static int motion = 1;
	
	public static final int GROUND = 0;
	public static final int TOTE1 = 5;
	public static final int TOTE2 = 10;
	public static final int TOTE3 = 15;
	private static int state = 1;
	

	public ElevatorData() {
		super("ElevatorData");
		// TODO Auto-generated constructor stub
	}
	
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
		return SPEED;
	}
	
	public int setMaxSpeed(int desiredSpeed){
		SPEED = desiredSpeed;
		return SPEED;
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
	
	
	
	@Override
	protected void ResetCommands() {
		// TODO Auto-generated method stub
		
	}

}
