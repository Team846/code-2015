package componentData;

import componentData.ManipulatorArmData.Arm;
import componentData.ManipulatorArmData.ArmState;

public class ManipulatorExtenderData extends ComponentData {
	
private ArmState[] armStates;
	
	public enum ArmState
	{
		EXTEND,
		RETRACT
	}
	
	public enum Arm
	{
		LEFT,
		RIGHT
	}

	public ManipulatorExtenderData(String name) {
		super("RakeHookData");
		armStates = new ArmState[2];
		ResetCommands();
	}
	
	public static ManipulatorExtenderData get(){
		return (ManipulatorExtenderData) ComponentData.GetComponentData("RakeExtenderData");
	}
	
	public ArmState getExtenderState(Arm arm)
	{
		return armStates[arm.ordinal()];
	}
	
	public void setExtenderState(Arm arm, ArmState state)
	{
		armStates[arm.ordinal()] = state;	
	}

	@Override
	protected void ResetCommands() 
	{
		armStates[0] = ArmState.RETRACT;
		armStates[1] = ArmState.RETRACT;
	}

}
