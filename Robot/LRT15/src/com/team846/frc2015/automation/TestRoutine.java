package com.team846.frc2015.automation;

import com.team846.frc2015.logging.AsyncLogger;

public class TestRoutine extends Automation {

    @Override
    public void AllocateResources() {

    }

    @Override
    protected boolean Start() {
        AsyncLogger.info("Started routine");
        return true;
    }

    @Override
    protected boolean Abort() {
        AsyncLogger.info("aborted routine");
        return true;
    }

    @Override
    protected boolean Run() {
        AsyncLogger.info("Running Routine");
        return false;
    }

}
