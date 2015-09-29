package com.team846.frc2015.components.stackSecurity;

import com.team846.util.monads.Monad;

public class VariableStackSecurityStyle extends StackSecurityStyle {
    Monad<Boolean> isSecurityDown;

    @Override
    public boolean isSecurityDown() {
        System.out.println(isSecurityDown.get());
        return isSecurityDown.get();
    }

    public VariableStackSecurityStyle(Monad<Boolean> isSecurityDown) {
        this.isSecurityDown = isSecurityDown;
    }
}
