package com.team846.frc2015.logging;

import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AsyncLogger {
    private static abstract class Loggable {
        String message;

        abstract public void log();

        public Loggable(String message) {
            this.message = message;
        }
    }

    static class InfoLog extends Loggable {
        public InfoLog(String message) {
            super(message);
        }

        @Override
        public void log() {
            Logger.info(message);
        }
    }

    static class DebugLog extends Loggable {
        public DebugLog(String message) {
            super(message);
        }

        @Override
        public void log() {
            Logger.debug(message);
        }
    }

    static class ErrorLog extends Loggable {
        public ErrorLog(String message) {
            super(message);
        }

        @Override
        public void log() {
            Logger.error(message);
        }
    }

    static class WarningLog extends Loggable {
        public WarningLog(String message) {
            super(message);
        }

        @Override
        public void log() {
            Logger.warning(message);
        }
    }

    private static final long gapBetweenFlush = 20; // ms

    private static final Queue<Loggable> toLog = new ConcurrentLinkedQueue<>();

    public static void debug(String msg) {
        toLog.add(new DebugLog(msg));
    }

    public static void warn(String msg) {
        toLog.add(new WarningLog(msg));
    }

    public static void error(String msg) {
        toLog.add(new ErrorLog(msg));
    }

    public static void info(String msg) {
        toLog.add(new InfoLog(msg));
    }

    static {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                while (!toLog.isEmpty()) {
                    toLog.remove().log();
                }
            }
        }, 0, gapBetweenFlush);
    }
}
