package com.team846.frc2015.components;

import com.team846.frc2015.driverstation.GameState;
import com.team846.robot.RobotState;

public abstract class StyledComponent<OperationStyle> extends Component {
    public StyledComponent() {}

    protected RobotState robotState = RobotState.Instance();
    private OperationStyle disabledStyle;
    private OperationStyle autoStyle;
    private OperationStyle teleopStyle;

    private OperationStyle currentStyle;

    protected abstract OperationStyle defaultDisabledStyle();

    protected abstract OperationStyle defaultAutoStyle();

    protected abstract OperationStyle defaultTeleopStyle();

    protected OperationStyle currentDefaultStyle() {
        if (disabledStyle == null) {
            disabledStyle = defaultDisabledStyle();
        }

        if (autoStyle == null) {
            autoStyle = defaultAutoStyle();
        }

        if (teleopStyle == null) {
            teleopStyle = defaultTeleopStyle();
        }

        if (robotState.GameMode() == GameState.DISABLED) {
            return disabledStyle;
        } else if (robotState.GameMode() == GameState.AUTONOMOUS) {
            return autoStyle;
        } else { // (robotState.GameMode() == GameState.TELEOPERATED)
            return teleopStyle;
        }
    }

    public void enterDefaultStyle() {
        setCurrentStyle(currentDefaultStyle());
    }

    public void setCurrentStyle(OperationStyle currentStyle) {
        this.currentStyle = currentStyle;
    }

    protected abstract void setOperation(OperationStyle currentStyle);

    @Override
    protected void updateEnabled() {
        setOperation(currentStyle);
    }

    @Override
    protected void updateDisabled() {
        enterDefaultStyle();
        setOperation(currentStyle);
    }

    @Override
    protected void onEnabled() {
        enterDefaultStyle();
    }

    @Override
    protected void onDisabled() {
        enterDefaultStyle();
    }
}
