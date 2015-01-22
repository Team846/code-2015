package automation;

import componentData.CollectorRollersData;
import componentData.CollectorRollersData.Direction;
import config.DriverStationConfig;
import driverstation.LRTDriverStation;
import driverstation.LRTJoystick;

public class CollectorRollersInput extends InputProcessor
{
	LRTJoystick operatorStick;
	CollectorRollersData collectorRollers;
	
	public CollectorRollersInput()
	{
		operatorStick = LRTDriverStation.Instance().GetOperatorStick();
		collectorRollers = CollectorRollersData.get();
	}

	@Override
	public void Update() {
		if(operatorStick.IsButtonDown(DriverStationConfig.JoystickButtons.REVERSE_ROLLERS))
		{
			collectorRollers.setRunning(true);
			collectorRollers.setDirection(Direction.REVERSE);
			collectorRollers.setSpeed(1.0);
		}
			
		
	}

}
