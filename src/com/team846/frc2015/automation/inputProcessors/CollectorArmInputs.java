package com.team846.frc2015.automation.inputProcessors;

import com.team846.frc2015.automation.ControlResource;
import com.team846.frc2015.componentData.CollectorArmData;
import com.team846.frc2015.componentData.CollectorArmData.ArmPosition;
import com.team846.frc2015.oldconfig.DriverStationConfig;
import com.team846.frc2015.driverstation.LRTDriverStation;
import com.team846.frc2015.driverstation.LRTJoystick;

public class CollectorArmInputs extends InputProcessor {

    private final CollectorArmData armData;
    private final LRTJoystick driverStick;
    private final LRTJoystick operatorStick;

    public CollectorArmInputs() {
        RegisterResource(ControlResource.COLLECTOR_ARMS);
        armData = CollectorArmData.get();
        driverStick = LRTDriverStation.instance().getDriverStick();
        operatorStick = LRTDriverStation.instance().getOperatorStick();
    }

    @Override
    public void Update() {
//        System.out.println("COLLECTOR OPERATOR ON: " + operatorStick.isButtonDown(DriverStationConfig.JoystickButtons.COLLECT_OPERATOR));
        if (driverStick.isButtonDown(DriverStationConfig.JoystickButtons.COLLECT) || operatorStick.isButtonDown(DriverStationConfig.JoystickButtons.COLLECT_OPERATOR)) {
            armData.setDesiredPosition(ArmPosition.EXTEND);
            //AsyncPrinter.info("Extend Collector");
        }

    }

}
