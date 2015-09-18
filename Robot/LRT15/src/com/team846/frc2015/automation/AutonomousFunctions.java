package com.team846.frc2015.automation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class AutonomousFunctions {
    Stack<Automation> routineStack = new Stack<Automation>();

    public ArrayList<Automation> popRoutine() {
        ArrayList<Automation> ret = new ArrayList<>();
        while (!routineStack.isEmpty()) {
            ret.add(0, routineStack.pop());
        }
        return ret;
    }

    public void parallel(Automation[] routines) {
        // the routines have already been added due to the other autonomous functions, pop them out
        for (int i = 0; i < routines.length; i++) {
            routineStack.pop();
        }

        // push the same routines as a parallel
        routineStack.push(
                new Parallel(
                        // TODO: remove name or understand purpos
                        "why are there string name? what is purpose?",
                        new ArrayList<Automation>(Arrays.asList(routines))));
    }

    private Automation pushAndRet(Automation routine) {
        routineStack.push(routine);
        return routine;
    }

    public Automation elevate(int toteLevel) {
        return pushAndRet(new Elevate(toteLevel));
    }

    public Automation extendCarriage() {
        return pushAndRet(new ExtendCarriage());
    }

    public Automation extendCarriage(double position) {
        return pushAndRet(new ExtendCarriage(position));
    }

    // loaders take parameter true to set routine to auto mode
    public Automation loadTote() {

        return pushAndRet(new LoadTote(true));
    }

    public Automation loadAdditional() {
        return pushAndRet(new LoadAdditional(true));
    }

    public Automation loadUprightContainer() {
        return pushAndRet(new LoadUprightContainer(true));
    }

    public Automation loadSidewaysContainer() {
        return pushAndRet(new LoadSidewaysContainer(true));
    }

    public Automation releaseStack() {
        return pushAndRet(new ReleaseStack());
    }

    public Automation drive(double distance) {
        return pushAndRet(new Drive(distance));
    }

    public Automation drive(double distance, double maxSpeed) {
        return pushAndRet(new Drive(distance, maxSpeed));
    }

    public Automation drive(double distance, double maxSpeed, double errThreshold) {
        return pushAndRet(new Drive(distance, maxSpeed, errThreshold));
    }

    public Automation drive(double distance, double maxSpeed, double errThreshold, boolean continuous) {
        return pushAndRet(new Drive(distance, maxSpeed, errThreshold, continuous));
    }

    public Automation turn() {
        return pushAndRet(new Turn());
    }

    public Automation turn(double angle) {
        return pushAndRet(new Turn(angle));
    }

    public Automation turn(double angle, double maxSpeed) {
        return pushAndRet(new Turn(angle, maxSpeed));
    }

    public Automation turn(double angle, double maxSpeed, double errThreshold) {
        return pushAndRet(new Turn(angle, maxSpeed, errThreshold));
    }

    // TODO: implement remaining strafe functions
    public Automation strafe(int ticks, double maxSpeed, double errThreshold) {
        return pushAndRet(new Strafe(ticks, maxSpeed, errThreshold));
    }

    public Automation sweep(Sweep.Direction direction, long ticks) {
        return pushAndRet(new Sweep(direction, ticks));
    }
}
