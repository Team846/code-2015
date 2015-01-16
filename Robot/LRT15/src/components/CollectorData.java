package components;

enum possibleCollectorState{
	INPUT,
	OUTPUT
}

public class CollectorData extends ComponentData {
	private static possibleCollectorState collectorState;
	private static float speed;
	private static boolean running;
	public CollectorData() {
		super("Collector");
		ResetCommands();
		collectorState = null;
		running = false;
	}
	public static CollectorData get(){
		return (CollectorData) ComponentData.GetComponentData("Collector");
	}

	@Override
	protected void ResetCommands() {

	}
	
	public possibleCollectorState getCurrentState(){
		return collectorState;
	}
	public void setCurrentState(possibleCollectorState DesiredState){
		collectorState = DesiredState;
	}
	
	public float getSpeed(){
		return speed;
	}
	public void setSpeed(float wantedSpeed){
		speed = wantedSpeed;
	}
	
	public boolean isRunning(){
		return running;
	}
	
	public void setRunning(boolean desiredState){
		running = desiredState;
	}
	
	public void invertRun(){
		running = !running;
	}

}
