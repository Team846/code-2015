package componentData;

import componentData.RakeArmData.Arm;
import componentData.RakeArmData.ArmState;

public class RakeExtenderData extends ComponentData {
	
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

	public RakeExtenderData(String name) {
		super("RakeHookData");
		armStates = new ArmState[2];
		armStates[0] = ArmState.RETRACT;
		armStates[1] = ArmState.RETRACT;
		ResetCommands();
	}
	
	public static RakeExtenderData get(){
		return (RakeExtenderData) ComponentData.GetComponentData("RakeExtenderData");
	}
	
	public ArmState getDesiredExtenderState(Arm arm)
	{
		return armStates[arm.ordinal()];
	}
	
	public void setDesiredExtenderState(Arm arm, ArmState state)
	{
		armStates[arm.ordinal()] = state;	
	}

	@Override
	protected void ResetCommands() 
	{ }

}
