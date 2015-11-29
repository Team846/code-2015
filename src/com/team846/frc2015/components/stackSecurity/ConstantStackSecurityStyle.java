package com.team846.frc2015.components.stackSecurity;

import com.team846.util.monads.Monad;

public class ConstantStackSecurityStyle extends StackSecurityStyle {
    boolean isSecurityDown;

    @Override
    public boolean isSecurityDown() {
        return isSecurityDown;
    }

    public ConstantStackSecurityStyle(boolean isSecurityDown) {
        this.isSecurityDown = isSecurityDown;
    }
}
