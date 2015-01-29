package componentData;

import java.util.ArrayList;

import actuators.Pneumatics;
import actuators.Pneumatics.State;
import utils.Pair;


public class ContainerHookData extends ComponentData{
	private ArrayList<Pair<Integer, Pneumatics.State>> solenoids = new ArrayList<Pair<Integer, Pneumatics.State>>();
	public ContainerHookData(){
		super("ContainerHook");
		ResetCommands();
	}
	
	public static ContainerHookData get(){
		return (ContainerHookData) ComponentData.GetComponentData("ContainerHook");
	}

	@Override
	protected void ResetCommands() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * @param solenoid number of the solenoid you want to control
	 * @param state state of the solenoid you want
	 */
	public void setState(int solenoid, Pneumatics.State state){
		Pair pair = new Pair(solenoid, state);
		solenoids.add(pair);
	}
	/**
	 * 
	 * @param solenoidNumber the number of the solenoid you want to get
	 * @return solenoid state in the form of pneumatics.state
	 */
	public Pneumatics.State getStateFromSolenoid(int solenoidNumber){
		for (int i=1; i<solenoids.size(); i++) {
			if(solenoids.get(i).getFirst() == solenoidNumber) {
				return solenoids.get(i).getSecond();
			}
		}
		return State.OFF;
	}
}
