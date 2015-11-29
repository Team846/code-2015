package com.team846.frc2015.logging;

import java.io.PrintStream;

public class Logger {
    private static PrintStream originalOut = System.out;

    public static void initialize() {
        System.setOut(new PrintStream(originalOut) {
            private String toString(Object obj) {
                return obj.toString();
            }

            @Override
            public void println() {
                debug("");
            }

            @Override
            public void println(boolean x) {
                debug(toString(x));
            }

            @Override
            public void println(char x) {
                debug(toString(x));
            }

            @Override
            public void println(int x) {
                debug(toString(x));
            }

            @Override
            public void println(long x) {
                debug(toString(x));
            }

            @Override
            public void println(float x) {
                debug(toString(x));
            }

            @Override
            public void println(double x) {
                debug(toString(x));
            }

            @Override
            public void println(char[] x) {
                debug(toString(x));
            }

            @Override
            public void println(String x) {
                debug(toString(x));
            }

            @Override
            public void println(Object x) {
                debug(toString(x));
            }
        });

        System.setErr(new PrintStream(originalOut) {
            private String toString(Object obj) {
                return obj.toString();
            }

            @Override
            public void println() {
                error("");
            }

            @Override
            public void println(boolean x) {
                error(toString(x));
            }

            @Override
            public void println(char x) {
                error(toString(x));
            }

            @Override
            public void println(int x) {
                error(toString(x));
            }

            @Override
            public void println(long x) {
                error(toString(x));
            }

            @Override
            public void println(float x) {
                error(toString(x));
            }

            @Override
            public void println(double x) {
                error(toString(x));
            }

            @Override
            public void println(char[] x) {
                error(toString(x));
            }

            @Override
            public void println(String x) {
                error(toString(x));
            }

            @Override
            public void println(Object x) {
                error(toString(x));
            }
        });
    }

    public static void info(String message) {
        originalOut.println("[INFO] " + message);
    }

    public static void debug(String message) {
        originalOut.println("[DEBUG] " + message);
    }

    public static void error(String message) {
        originalOut.println("[ERROR] " + message);
    }

    public static void warning(String message) {
        originalOut.println("[WARNING] " + message);
    }
}

