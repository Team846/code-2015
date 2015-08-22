package com.team846.frc2015;

import com.team846.frc2015.logging.AsyncLogger;

public class AsyncLoggerTest {
    public static void main(String[] args) {
        AsyncLogger.info("info message");
        AsyncLogger.debug("debug message");
        AsyncLogger.warn("warning message");
        AsyncLogger.error("error message");
    }
}
