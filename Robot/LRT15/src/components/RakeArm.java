package components;

import componentData.ComponentData;
import componentData.RakeArmData;
import componentData.RakeArmData.Arm;
import componentData.RakeArmData.ArmState;
import edu.wpi.first.wpilibj.Solenoid;
import actuators.Pneumatics;
import actuators.Pneumatics.State;

public class RakeArm extends Component{
	
	private Pneumatics leftArm = null;
	private Pneumatics rightArm = null;

	private RakeArmData armData;
	private static final int CHANGEME = 42;
	public static final int solenoidLength = 6;
	
	public RakeArm() {
		super("ContainerHook", CHANGEME);
		armData = RakeArmData.get();
		
		leftArm = new Pneumatics(CHANGEME, "RakeA");
		rightArm = new Pneumatics(CHANGEME, "RakeB");
		
	}

	@Override
	protected void UpdateEnabled() 
	{
		Pneumatics.State leftArmState;
		Pneumatics.State rightArmState;
		
		if(armData.getDesiredArmState(Arm.LEFT) == ArmState.DEPLOYED)
			leftArmState = State.FORWARD;
		else
			leftArmState = State.OFF;
		if(armData.getDesiredArmState(Arm.RIGHT) == ArmState.DEPLOYED)
			rightArmState = State.FORWARD;
		else
			rightArmState = State.OFF;
		
		leftArm.Set(leftArmState, false);
		rightArm.Set(rightArmState, false);	

	}

	@Override
	protected void UpdateDisabled() 
	{
		leftArm.Set(State.OFF, false);
		rightArm.Set(State.OFF, false);
	}

	@Override
	protected void OnEnabled() {
		
	}

	@Override
	protected void OnDisabled() {
		
	}

}
