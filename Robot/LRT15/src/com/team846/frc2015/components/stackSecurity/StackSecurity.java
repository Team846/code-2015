package com.team846.frc2015.components.stackSecurity;

import com.team846.frc2015.actuators.Pneumatics;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.components.StyledComponent;
import com.team846.frc2015.driverstation.LRTDriverStation;
import com.team846.frc2015.driverstation.LRTJoystick;
import com.team846.frc2015.oldconfig.ConfigPortMappings;
import com.team846.frc2015.oldconfig.DriverStationConfig;
import com.team846.util.monads.ValueMonad;

public class StackSecurity extends StyledComponent<StackSecurityStyle> {
    private ValueMonad<Boolean> stackSecurityButtonDown = new ValueMonad<Boolean>() {
        LRTJoystick operatorStick = LRTDriverStation.instance().getOperatorStick();

        @Override
        public Boolean get() {
            boolean downRequirement = true;//ElevatorData.get().getSpeed() == 0;
            return downRequirement && operatorStick.isButtonDown(DriverStationConfig.JoystickButtons.STACK_SECURITY);
        }
    };

    private Pneumatics securityArms = new Pneumatics(
        ConfigPortMappings.Instance().get("Pneumatics/STACK_SECURITY"),
        "StackSecurity"
    );

    @Override
    protected StackSecurityStyle defaultDisabledStyle() {
        return new ConstantStackSecurityStyle(false);
    }

    @Override
    protected StackSecurityStyle defaultAutoStyle() {
        return new ConstantStackSecurityStyle(false);
    }

    @Override
    protected StackSecurityStyle defaultTeleopStyle() {
        return new VariableStackSecurityStyle(stackSecurityButtonDown);
    }

    @Override
    protected void setOperation(StackSecurityStyle currentStyle) {
        System.out.println("ARMS DOWN: " + currentStyle.isSecurityDown());
        securityArms.set(
            currentStyle.isSecurityDown() ?
                Pneumatics.PneumaticState.FORWARD :
                Pneumatics.PneumaticState.OFF
        );
    }
}
