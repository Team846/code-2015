package com.team846.frc2015.automation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.team846.frc2015.logging.AsyncLogger;

public class Sequential extends Automation {
    private ArrayList<Automation> routines = new ArrayList<Automation>();
    private final Queue<Automation> queued = new LinkedList<Automation>();
    private boolean started;

    Sequential(String name, RoutineOption... options) {
        super(name, options);
        if (routineOptions.contains(RoutineOption.REQUIRES_ABORT_CYCLES))
            routineOptions.remove(RoutineOption.REQUIRES_ABORT_CYCLES);
        started = false;
    }

    Sequential(String name, Automation... sequence) {
        super(name);
        for (Automation auto: sequence) {
            AddAutomation(auto);
        }
    }

    Sequential(String name, ArrayList<Automation> sequence, boolean restartable, RoutineOption... options) {
        super(name, options);

        if (routineOptions.contains(RoutineOption.REQUIRES_ABORT_CYCLES))
            routineOptions.remove(RoutineOption.REQUIRES_ABORT_CYCLES);
        routines = sequence;
        started = false;
    }

    public void AllocateResources() {
        for (Automation auto : routines)
            auto.AllocateResources();
    }

    protected boolean Start() {
        if (routines.isEmpty())
            return false;
        while (!queued.isEmpty())
            queued.remove();
        for (Automation auto : routines)
            queued.add(auto);
        started = false;
        return true;
    }

    protected boolean Run() {
        if (!started && ContinueNextStep()) {
            AsyncLogger.info("[SEQUENTIAL] [" + GetName() + "] is currently running: " + queued.peek().GetName());
            boolean res = queued.peek().StartAutomation(GetStartEvent());
            if (res)
                started = true;
            else
                return true;
        }
        if (started) {
            boolean completed = queued.peek().Update();
            if (completed) {
                queued.remove();
                started = false;
            }
        }

        return queued.isEmpty();
    }

    public boolean Abort() {
        AsyncLogger.info("Abort Event: " + GetAbortEvent().getClass().getName());
        if (!queued.isEmpty()) {
            boolean res = queued.peek().AbortAutomation(GetAbortEvent());
            if (res) {
                while (!queued.isEmpty())
                    queued.remove();
            }
            return res;
        }
        return true;
    }

    public void AddAutomation(Automation automation) {
        routines.add(automation);
    }

    public void AddAutomation(ArrayList<Automation> automation) {
        routines.ensureCapacity(routines.size() + automation.size());
        routines.addAll(automation);
    }

    void ClearSequence() {
        routines.clear();
    }

    boolean ContinueNextStep() {
        return true;
    }

    protected Automation GetCurrentAutomation() {
        return queued.peek();
    }
}
