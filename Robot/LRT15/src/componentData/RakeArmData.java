package componentData;

import java.util.ArrayList;

import actuators.Pneumatics;
import actuators.Pneumatics.State;
import utils.Pair;


public class RakeArmData extends ComponentData{

	private ArmState[] armStates;
	
	public enum ArmState
	{
		UNDEPLOYED,
		DEPLOYED
	}
	
	public enum Arm
	{
		LEFT,
		RIGHT
	}
	
	public RakeArmData(){
		super("ContainerHook");
		armStates = new ArmState[2];
		armStates[0] = ArmState.UNDEPLOYED;
		armStates[1] = ArmState.UNDEPLOYED;
		ResetCommands();
	}
	
	public static RakeArmData get(){
		return (RakeArmData) ComponentData.GetComponentData("ContainerHook");
	}

	@Override
	protected void ResetCommands() 
	{ }
	
	public ArmState getDesiredArmState(Arm arm)
	{
		return armStates[arm.ordinal()];
		
	}
	
	public void setDesiredArmState(Arm arm, ArmState state)
	{
		armStates[arm.ordinal()] = state;	
	}
	
}
