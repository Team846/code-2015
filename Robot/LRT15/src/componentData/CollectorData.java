package componentData;

import componentData.ComponentData;


public class CollectorData extends ComponentData {
	private static double speed;
	private static boolean running;
	private static boolean collectorIO;
	public CollectorData() {
		super("Collector");
		ResetCommands();
		running = false;
	}
	public static CollectorData get(){
		return (CollectorData) ComponentData.GetComponentData("Collector");
	}

	@Override
	protected void ResetCommands() {
		
	}
	
	public double getSpeed(){
		return speed;
	}
	public void setSpeed(float desiredSpeed){
		speed = desiredSpeed;
	}
	
	public boolean isRunning(){
		return running;
	}
	
	public void setRunning(boolean desiredRunningState){
		running = desiredRunningState;
	}
	
	public boolean getIO(){
		return collectorIO;
	}
	
	public void setIO(boolean desiredIO){
		collectorIO = desiredIO;
	}


}
