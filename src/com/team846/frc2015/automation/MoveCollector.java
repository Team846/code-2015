package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.CollectorArmData;
import com.team846.frc2015.componentData.CollectorArmData.ArmPosition;

public class MoveCollector extends Automation {

    private CollectorArmData armData;
    private final ArmPosition desiredCollectorState;

    public MoveCollector(boolean state) {
        desiredCollectorState = state ? ArmPosition.EXTEND : ArmPosition.STOWED;
    }

    @Override
    public void AllocateResources() {
        AllocateResource(ControlResource.COLLECTOR_ARMS);
        armData = CollectorArmData.get();
    }

    @Override
    protected boolean Start() {
        return true;
    }

    @Override
    protected boolean Abort() {
        armData.setDesiredPosition(ArmPosition.STOWED);
        return true;
    }

    @Override
    protected boolean Run() {
        armData.setDesiredPosition(desiredCollectorState);
        return armData.getCurrentPosition() == desiredCollectorState;
    }

}
