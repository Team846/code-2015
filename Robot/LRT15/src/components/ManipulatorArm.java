package components;

import componentData.ComponentData;
import componentData.ManipulatorArmData;
import componentData.ManipulatorArmData.Arm;
import componentData.ManipulatorArmData.ArmState;
import config.ConfigPortMappings;
import config.DriverStationConfig;
import edu.wpi.first.wpilibj.Solenoid;
import actuators.Pneumatics;
import actuators.Pneumatics.State;

public class ManipulatorArm extends Component{
	
	private Pneumatics leftArm = null;
	private Pneumatics rightArm = null;

	private ManipulatorArmData armData;
	
	public ManipulatorArm() {
		super("ContainerHook", DriverStationConfig.DigitalIns.NO_DS_DI);
		
		armData = ManipulatorArmData.get();
		
		leftArm = new Pneumatics(
				ConfigPortMappings.Instance().Get("Pneumatics/MANIPULATOR_LEFT"), "RakeA");
		rightArm = new Pneumatics(
				ConfigPortMappings.Instance().Get("Pneumatics/MANIPULATOR_RIGHT"), "RakeB");
	}

	@Override
	protected void UpdateEnabled() 
	{
		Pneumatics.State leftArmState;
		Pneumatics.State rightArmState;
		
		if(armData.getDesiredArmState(Arm.LEFT) == ArmState.DEPLOYED)
			leftArmState = State.FORWARD;
		else
			leftArmState = State.REVERSE;
		if(armData.getDesiredArmState(Arm.RIGHT) == ArmState.DEPLOYED)
			rightArmState = State.FORWARD;
		else
			rightArmState = State.REVERSE;
		
		leftArm.Set(leftArmState);
		rightArm.Set(rightArmState);	

	}

	@Override
	protected void UpdateDisabled() 
	{
		leftArm.Set(State.OFF);
		rightArm.Set(State.OFF);
	}

	@Override
	protected void OnEnabled() {
		
	}

	@Override
	protected void OnDisabled() {
		
	}

}
