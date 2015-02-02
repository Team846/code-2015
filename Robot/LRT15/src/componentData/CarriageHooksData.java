package componentData;

import componentData.ComponentData;

public class CarriageHooksData extends ComponentData{

	public enum Position
	{
		ENABLED,
		DISABLED
	}
	
	private Position desiredFrontState;
	private Position desiredBackState;
	private Position currentFrontState;
	private Position currentBackState;
	
	public CarriageHooksData() {
		super("CarriageHooksData");
		desiredFrontState = Position.ENABLED;
		desiredBackState = Position.ENABLED;
		currentFrontState = Position.ENABLED;
		currentBackState = Position.ENABLED;
	}
	
	public static CarriageHooksData get(){
		return (CarriageHooksData) ComponentData.GetComponentData("CollectorArmData");
	}
	
	public Position getFrontHooksState(){
		return currentFrontState;
	}
	
	public Position getFrontHooksDesiredState(){
		return desiredFrontState;
	}
	
	public void setFrontHooksState(Position newState){
		desiredFrontState = newState;
	}
	
	public Position getBackHooksState(){
		return currentBackState;
	}
	
	public Position getBackHooksDesiredState(){
		return desiredBackState;
	}

	public void setBackHooksState(Position newState){
		desiredBackState = newState;
	}

	@Override
	protected void ResetCommands() {
		
	}

}
