package com.team846.frc2015.components;

import java.util.ArrayList;

import com.team846.frc2015.driverstation.GameState;
import com.team846.frc2015.logging.AsyncLogger;
import com.team846.robot.RobotState;

public abstract class Component {

    private static final ArrayList<Component> component_list = new ArrayList<Component>();
    private final String name;
    private boolean lastEnabled;

    abstract protected void updateEnabled();


    abstract protected void updateDisabled();


    abstract protected void onEnabled();


    abstract protected void onDisabled();

    /**
     * Constructs base component with associated
     * NOTE: As of 2015, digitalIns are not supported
     * All components must be e
     */
    public Component() {
        this.name = this.getClass().getSimpleName();

        lastEnabled = false;
        AsyncLogger.info("Created component: " + name);
    }

    public static void CreateComponents() {
        component_list.add(new OldDrivetrain());
        component_list.add(new CollectorRollers());
        component_list.add(new CollectorArms());
        component_list.add(new CarriageExtender());
        component_list.add(new CarriageHooks());
        component_list.add(new Elevator());
        component_list.add(new ContainerArm());
    }


    public static void UpdateAll() {
        for (Component c : component_list) {
            c.Update();
        }
    }

    void Update() {
        if (RobotState.Instance().GameMode() != GameState.DISABLED) {
            if (!lastEnabled)
                onEnabled();
            updateEnabled();
            lastEnabled = true;
        } else {
            if (lastEnabled)
                onDisabled();
            updateDisabled();
            lastEnabled = false;
        }
    }

    public String getName() {
        return name;
    }
}
