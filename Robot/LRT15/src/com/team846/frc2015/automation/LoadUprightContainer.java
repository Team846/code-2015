package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.CarriageExtenderData;
import com.team846.frc2015.componentData.CarriageHooksData;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.componentData.CarriageHooksData.HookState;
import com.team846.frc2015.componentData.ElevatorData.ElevatorControlMode;
import com.team846.frc2015.componentData.ElevatorData.ElevatorSetpoint;

public class LoadUprightContainer extends LoadItem {

	private CarriageHooksData hooksData;
	
	public LoadUprightContainer() {
		super("LoadUprightContainer", ElevatorData.ElevatorSetpoint.COLLECT_UPRIGHT_CONTAINER,
				ElevatorData.ElevatorSetpoint.COLLECT_UPRIGHT_CONTAINER, ElevatorData.ElevatorSetpoint.HOME_UPRIGHT_CONTAINER);

		hooksData = CarriageHooksData.get();
	}

	@Override
	protected boolean Run()
	{
		boolean ret = super.Run();

		// Override hook states
		if (state == State.COLLECT)
		{
			hooksData.setFrontHooksDesiredState(HookState.UP);
			hooksData.setBackHooksDesiredState(HookState.DOWN);
		}
		else if (state == State.GRAB)
		{
			hooksData.setFrontHooksDesiredState(HookState.DOWN);
			hooksData.setBackHooksDesiredState(HookState.DOWN);
		}
		
		return ret;
	}
}

