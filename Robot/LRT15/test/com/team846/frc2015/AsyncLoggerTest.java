package com.team846.frc2015;

import com.team846.frc2015.logging.AsyncLogger;
import com.team846.frc2015.logging.Logger;

public class AsyncLoggerTest {
    public static void main(String[] args) {
        Logger.initialize();

        AsyncLogger.info("info message");
        AsyncLogger.debug("debug message");
        AsyncLogger.warn("warning message");
        AsyncLogger.error("error message");

        System.out.println("System.out message");
        System.err.println("System.err message");
    }
}
