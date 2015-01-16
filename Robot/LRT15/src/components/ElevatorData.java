package components;

public class ElevatorData extends ComponentData{
	
	public static int SPEED = 5;

	
	public static final int STAY = 1;
	public static final int UP= 2;
	public static final int DOWN= 3;
	private static int state = 1;

	public ElevatorData(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public int getState(){
		return state; 
	}
	
	public int setDesiredState(int i){
		if(i == STAY || i == UP || i == DOWN){
			state = i;
		}
		return state; 
	}
	
	public int getMaxSpeed(){
		return SPEED;
	}
	
	public int setMaxSpeed(int desiredSpeed){
		SPEED = desiredSpeed;
		return SPEED;
	}
	
	
	
	@Override
	protected void ResetCommands() {
		// TODO Auto-generated method stub
		
	}

}
