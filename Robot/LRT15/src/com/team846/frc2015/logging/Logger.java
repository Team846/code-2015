package com.team846.frc2015.logging;

public class Logger {
    static Logger instance = null;
    private Logger() {

    }

    public void info(String to_print) {
        System.out.println("[INFO]" + to_print);
    }

    public void debug(String to_print) {
        System.out.println("[DEBUG]" + to_print);
    }

    public void error(String to_print) {
        System.out.println("[ERROR]" + to_print);
    }

    public void warning(String to_print) {
        System.out.println("[WARNING]" + to_print);
    }

    public static Logger getInstance() {
        if (instance == null) {
           instance = new Logger();
        }
        return instance;
    }

}

